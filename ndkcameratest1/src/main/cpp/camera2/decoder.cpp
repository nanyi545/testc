//
// Created by wei wang on 2021-08-04.
//
#include <assert.h>
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <limits.h>

#include "looper.h"
#include "media/NdkMediaCodec.h"
#include "media/NdkMediaExtractor.h"
#include "native_debug.h"

// for native window JNI
#include <android/native_window_jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>


typedef struct {
    int fd;
    ANativeWindow* window;
    AMediaExtractor* ex;
    AMediaCodec *codec;
    int64_t renderstart;
    bool sawInputEOS;
    bool sawOutputEOS;
    bool isPlaying;
    bool renderonce;
} workerdata;

workerdata data = {-1, NULL, NULL, NULL, 0, false, false, false, false};

enum {
    kMsgCodecBuffer,
    kMsgPause,
    kMsgResume,
    kMsgPauseAck,
    kMsgDecodeDone,
    kMsgSeek,
};


class playerlooper: public looper {
    virtual void handle(int what, void* obj);
};

static playerlooper *mlooper = NULL;

int64_t systemnanotime() {
    timespec now;
    clock_gettime(CLOCK_MONOTONIC, &now);
    return now.tv_sec * 1000000000LL + now.tv_nsec;
}


void doCodecWork(workerdata *d) {

    /**
     * 解码 ....
     */
    ssize_t bufidx = -1;
    if (!d->sawInputEOS) {
        bufidx = AMediaCodec_dequeueInputBuffer(d->codec, 2000);
        LOGI("doCodecWork  input buffer %zd", bufidx);
        if (bufidx >= 0) {
            size_t bufsize;
            auto buf = AMediaCodec_getInputBuffer(d->codec, bufidx, &bufsize);
            auto sampleSize = AMediaExtractor_readSampleData(d->ex, buf, bufsize);
            if (sampleSize < 0) {
                sampleSize = 0;
                d->sawInputEOS = true;
                LOGI("EOS");
            }
            auto presentationTimeUs = AMediaExtractor_getSampleTime(d->ex);

            AMediaCodec_queueInputBuffer(d->codec, bufidx, 0, sampleSize, presentationTimeUs,
                                         d->sawInputEOS ? AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM : 0);
            AMediaExtractor_advance(d->ex);
        }
    }

    if (!d->sawOutputEOS) {
        AMediaCodecBufferInfo info;
        auto status = AMediaCodec_dequeueOutputBuffer(d->codec, &info, 0);
        LOGI("doCodecWork  out buffer status:%d", status);

        if (status >= 0) {
            if (info.flags & AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM) {
                LOGI("output EOS");
                d->sawOutputEOS = true;
            }
            int64_t presentationNano = info.presentationTimeUs * 1000;
            LOGI("doCodecWork  presentationTimeUs :%" PRId64" |--", info.presentationTimeUs); // 1s =  1000*1000 us

            if (d->renderstart < 0) {
                d->renderstart = systemnanotime() - presentationNano;
            }
            int64_t delay = (d->renderstart + presentationNano) - systemnanotime();
            if (delay > 0) {
                usleep(delay / 1000);   // sleep in us
                // sleep 1000000 ---> for 1 sec
            }

            AMediaCodec_releaseOutputBuffer(d->codec, status, info.size != 0);
            if (d->renderonce) {
                d->renderonce = false;
                return;
            }
        } else if (status == AMEDIACODEC_INFO_OUTPUT_BUFFERS_CHANGED) {
            LOGI("output buffers changed");
        } else if (status == AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED) {
            auto format = AMediaCodec_getOutputFormat(d->codec);
            LOGI("format changed to: %s", AMediaFormat_toString(format));
            AMediaFormat_delete(format);
        } else if (status == AMEDIACODEC_INFO_TRY_AGAIN_LATER) {
            LOGI("no output buffer right now");
        } else {
            LOGI("unexpected info code: %zd", status);
        }
    }

    if (!d->sawInputEOS || !d->sawOutputEOS) {
        LOGI("doCodecWork  ------  post");
        mlooper->post(kMsgCodecBuffer, d);
    }
}

void playerlooper::handle(int what, void* obj) {
    switch (what) {
        case kMsgCodecBuffer:
            doCodecWork((workerdata*)obj);
            break;

        case kMsgDecodeDone:
        {
            workerdata *d = (workerdata*)obj;
            AMediaCodec_stop(d->codec);
            AMediaCodec_delete(d->codec);
            AMediaExtractor_delete(d->ex);
            d->sawInputEOS = true;
            d->sawOutputEOS = true;
        }
            break;

        case kMsgSeek:
        {
            workerdata *d = (workerdata*)obj;
            AMediaExtractor_seekTo(d->ex, 0, AMEDIAEXTRACTOR_SEEK_NEXT_SYNC);
            AMediaCodec_flush(d->codec);
            d->renderstart = -1;
            d->sawInputEOS = false;
            d->sawOutputEOS = false;
            if (!d->isPlaying) {
                d->renderonce = true;
                post(kMsgCodecBuffer, d);
            }
            LOGI("seeked");
        }
            break;

        case kMsgPause:
        {
            workerdata *d = (workerdata*)obj;
            if (d->isPlaying) {
                // flush all outstanding codecbuffer messages with a no-op message
                d->isPlaying = false;
                post(kMsgPauseAck, NULL, true);
            }
        }
            break;

        case kMsgResume:
        {
            workerdata *d = (workerdata*)obj;
            if (!d->isPlaying) {
                d->renderstart = -1;
                d->isPlaying = true;
                post(kMsgCodecBuffer, d);
            }
        }
            break;
    }
}




extern "C" {

jboolean Java_com_tvtb_ndkcameratest1_MainActivity_createStreamingMediaPlayer(JNIEnv *env,
                                                                             jclass clazz,
                                                                             jobject assetMgr,
                                                                             jstring filename) {
    LOGI("@@@ create");

    // convert Java string to UTF-8
    const char *utf8 = env->GetStringUTFChars(filename, NULL);
    LOGI("opening %s", utf8);

    off_t outStart, outLen;

    /**
     *  打开视频文件
     */
    int fd = AAsset_openFileDescriptor(
            AAssetManager_open(AAssetManager_fromJava(env, assetMgr), utf8, 0),
            &outStart, &outLen);

    env->ReleaseStringUTFChars(filename, utf8);
    if (fd < 0) {
        LOGE("failed to open file: %s %d (%s)", utf8, fd, strerror(errno));
        return JNI_FALSE;
    }

    data.fd = fd;

    workerdata *d = &data;

//    // start ???
//    // outLen --> 930185 : file size in bytes
//    LOGI("outStart:%d  outLen:%d", outStart, outLen);


    /**
     *
     *
     *  AMediaExtractor_new
     *
     *  AMediaExtractor_setDataSourceFd
     *
     *
     */

    AMediaExtractor *ex = AMediaExtractor_new();
    media_status_t err = AMediaExtractor_setDataSourceFd(ex, d->fd,
                                                         static_cast<off64_t>(outStart),
                                                         static_cast<off64_t>(outLen));
    close(d->fd);
    if (err != AMEDIA_OK) {
        LOGI("setDataSource error: %d", err);
        return JNI_FALSE;
    }

    /**
     *  获取trackCount
     *  找到不同track
     *
     */
    int numtracks = AMediaExtractor_getTrackCount(ex);

    AMediaCodec *codec = NULL;

    LOGI("input has %d tracks", numtracks);
    for (int i = 0; i < numtracks; i++) {
        AMediaFormat *format = AMediaExtractor_getTrackFormat(ex, i);
        const char *s = AMediaFormat_toString(format);
        LOGI("track %d format: %s", i, s);
        const char *mime;
        if (!AMediaFormat_getString(format, AMEDIAFORMAT_KEY_MIME, &mime)) {
            LOGI("no mime type");
            return JNI_FALSE;
        } else if (!strncmp(mime, "video/", 6)) {

            LOGI("mime:%s", mime);

            // Omitting most error handling for clarity.
            // Production code should check for errors.

            /**
             * 视频track处理
             * AMediaExtractor_selectTrack ...
             * AMediaCodec_createDecoderByType ...
             * AMediaCodec_configure ... 设置 codec / format / 输出的window / ....
             *
             */
            AMediaExtractor_selectTrack(ex, i);
            codec = AMediaCodec_createDecoderByType(mime);
            AMediaCodec_configure(codec, format, d->window, NULL, 0);
            d->ex = ex;
            d->codec = codec;
            d->renderstart = -1;
            d->sawInputEOS = false;
            d->sawOutputEOS = false;
            d->isPlaying = false;


            /**
             *  renderonce  --> render once and quit decode .....
             */
            d->renderonce = true;
            d->renderonce = false;

            AMediaCodec_start(codec);
        }
        AMediaFormat_delete(format);
    }

    mlooper = new playerlooper();
    mlooper->post(kMsgCodecBuffer, d);

    return JNI_TRUE;
}

// set the playing state for the streaming media player
void Java_com_example_nativecodec_NativeCodec_setPlayingStreamingMediaPlayer(JNIEnv *env,
                                                                             jclass clazz,
                                                                             jboolean isPlaying) {
    LOGI("@@@ playpause: %d", isPlaying);
    if (mlooper) {
        if (isPlaying) {
            mlooper->post(kMsgResume, &data);
        } else {
            mlooper->post(kMsgPause, &data);
        }
    }
}


// shut down the native media system
void Java_com_example_nativecodec_NativeCodec_shutdown(JNIEnv *env, jclass clazz) {
    LOGI("@@@ shutdown");
    if (mlooper) {
        mlooper->post(kMsgDecodeDone, &data, true /* flush */);
        mlooper->quit();
        delete mlooper;
        mlooper = NULL;
    }
    if (data.window) {
        ANativeWindow_release(data.window);
        data.window = NULL;
    }
}


// set the surface
void Java_com_tvtb_ndkcameratest1_MainActivity_setSurface(JNIEnv *env, jclass clazz, jobject surface) {
    // obtain a native window from a Java surface
    if (data.window) {
        ANativeWindow_release(data.window);
        data.window = NULL;
    }
    // get NativeWindow from java surface object
    data.window = ANativeWindow_fromSurface(env, surface);
    LOGI("@@@ setsurface %p", data.window);
}

}