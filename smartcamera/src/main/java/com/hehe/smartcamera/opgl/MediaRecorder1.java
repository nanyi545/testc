package com.hehe.smartcamera.opgl;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import com.hehe.smartcamera.GlRecordActivity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class MediaRecorder1 {


    private static final String TAG = "MediaRecorder1_David";


    private MediaCodec videoEncoder;
    private   int mWidth;
    private   int mHeight;
    private String mPath;
    private Surface mSurface;
    private Handler mHandler;
//    编码封装格式  h264
    private MediaMuxer mMuxer;
    private EGLContext mGlContext;
    private EGLEnv eglEnv;
    private boolean isStart;
    private Context mContext;

    private long mLastTimeStamp;
    private int videotrack;
    private int metadataTrackIndex;

    /**
     * meta track only works for API>=26
     *
     * https://developer.android.com/reference/android/media/MediaMuxer#History
     *
     */
    private boolean skipMetaTrack = true;

    private float mSpeed;

    String getFormat(){
//        return MediaFormat.MIMETYPE_VIDEO_AVC;
        return MediaFormat.MIMETYPE_VIDEO_HEVC;
    }

    public MediaRecorder1(Context context, String path, EGLContext glContext, int width, int
            height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }
    public void start(float speed) throws IOException {
        mSpeed = speed;
        MediaFormat format = MediaFormat.createVideoFormat(getFormat(), mWidth, mHeight);

        //颜色空间 从 surface当中获得
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities
                .COLOR_FormatSurface);
        //码率
        format.setInteger(MediaFormat.KEY_BIT_RATE, mWidth * mHeight/2);
        //帧率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
        //关键帧间隔
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 15);
        //创建编码器
        videoEncoder = MediaCodec.createEncoderByType(getFormat());

        //配置编码器
        videoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//输入数据     byte[]    gpu  mediaprojection

        mSurface= videoEncoder.createInputSurface();

//        视频  编码一个可以播放的视频
        //混合器 (复用器) 将编码的h.264封装为mp4
        mMuxer = new MediaMuxer(mPath,
                MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        //开启编码
        videoEncoder.start();
//        重点    opengl   gpu里面的数据画面   肯定要调用   opengl 函数
//线程
        //創建OpenGL 的 環境
        HandlerThread handlerThread = new HandlerThread("codec-gl");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eglEnv = new EGLEnv(mContext,mGlContext, mSurface,mWidth, mHeight);
                isStart = true;
            }
        });

    }


    /**
     *   will queue size grow indefinitely ??? .....
     */
    ConcurrentHashMap<Long,Object> queueMap = new ConcurrentHashMap<>();



//    编码   textureId数据  并且编码
//byte[]
    public void fireFrame(final int textureId, final long timestamp) {

        Log.d(TAG,"fireFrame    isStart:"+isStart+"   timestamp:"+timestamp);

//        主动拉去openglfbo数据
        if (!isStart) {
            return;
        }
        //录制用的opengl已经和handler的线程绑定了 ，所以需要在这个线程中使用录制的opengl
        mHandler.post(new Runnable() {
            public void run() {

                queueMap.put(timestamp,"");

                Message msg = Message.obtain(GlRecordActivity.getMainHandler(),10087);
                msg.arg1 = queueMap.size();
                GlRecordActivity.getMainHandler().sendMessage(msg);


//                opengl   能 1  不能2  draw  ---》surface
                eglEnv.draw(textureId,timestamp);
//                获取对应的数据
                codec(false,timestamp);
            }
        });

    }

    private void codec(boolean endOfStream,long timeStamp) {
//        数据什么时候
//        编码
        //给个结束信号
        if (endOfStream) {
            videoEncoder.signalEndOfInputStream();
        }
        while (true) {

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int index = videoEncoder.dequeueOutputBuffer(bufferInfo, 10_000);
//            编码的地方
            //需要更多数据
            Log.d(TAG,"index:"+index+"  timeStamp:"+timeStamp);
            if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //如果是结束那直接退出，否则继续循环
                if (!endOfStream) {
                    queueMap.remove(timeStamp);
                    Message msg = Message.obtain(GlRecordActivity.getMainHandler(),10087);
                    msg.arg1 = queueMap.size();
                    GlRecordActivity.getMainHandler().sendMessage(msg);
                    break;
                }
            } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //输出格式发生改变  第一次总会调用所以在这里开启混合器
                MediaFormat newFormat = videoEncoder.getOutputFormat();
                videotrack = mMuxer.addTrack(newFormat);
                Log.d(TAG,"videotrack:"+videotrack);

                if(skipMetaTrack){

                } else {
//                    MediaFormat metadataFormat = MediaFormat.createSubtitleFormat(MediaFormat.MIMETYPE_TEXT_CEA_608,"und");

                    MediaFormat metadataFormat = new MediaFormat();
                    metadataFormat.setString(MediaFormat.KEY_MIME,"application/info");

                    metadataTrackIndex = mMuxer.addTrack(metadataFormat);
                    Log.d(TAG,"videotrack:"+videotrack+"  metadataTrackIndex:"+metadataTrackIndex);
                }

                mMuxer.start();
            } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                //可以忽略
            } else {
                //调整时间戳
                bufferInfo.presentationTimeUs = (long) (bufferInfo.presentationTimeUs / mSpeed);
                //有时候会出现异常 ： timestampUs xxx < lastTimestampUs yyy for Video videotrack
                if (bufferInfo.presentationTimeUs <= mLastTimeStamp) {
                    bufferInfo.presentationTimeUs = (long) (mLastTimeStamp + 1_000_000 / 25 / mSpeed);
                }
                mLastTimeStamp = bufferInfo.presentationTimeUs;

                //正常则 index 获得缓冲区下标
                ByteBuffer encodedData = videoEncoder.getOutputBuffer(index);
                //如果当前的buffer是配置信息，不管它 不用写出去
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    //设置从哪里开始读数据(读出来就是编码后的数据)
                    encodedData.position(bufferInfo.offset);
                    //设置能读数据的总长度
                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
                    //写出为mp4
                    mMuxer.writeSampleData(videotrack, encodedData, bufferInfo);




                    /**
                     * 写入meta info
                     */

                    if(skipMetaTrack){

                    }  else {
                        int metaInfoSize = 3 * 4;  // 3 floats /  4 bytes per float
                        ByteBuffer metaData = ByteBuffer.allocate(metaInfoSize);
                        metaData.putFloat(1f);
                        metaData.putFloat(2f);
                        metaData.putFloat(3f);
                        MediaCodec.BufferInfo metaInfo = new MediaCodec.BufferInfo();
                        // Associate this metadata with the video frame by setting
                        // the same timestamp as the video frame.
                        metaInfo.presentationTimeUs = bufferInfo.presentationTimeUs;
                        metaInfo.offset = 0;
                        metaInfo.flags = 0;
                        metaInfo.size = metaInfoSize;
                        mMuxer.writeSampleData(metadataTrackIndex, metaData, metaInfo);
                    }

                }




                // 释放这个缓冲区，后续可以存放新的编码后的数据啦
                videoEncoder.releaseOutputBuffer(index, false);
                // 如果给了结束信号 signalEndOfInputStream
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }

        }
    }
    public void stop() {
        Log.d("timer","media recorder stop:");
        // 释放
        isStart = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                codec(true,-1);
                videoEncoder.stop();
                videoEncoder.release();
                videoEncoder = null;
                mMuxer.stop();
                mMuxer.release();
                eglEnv.release();
                eglEnv = null;
                mMuxer = null;
                mSurface = null;
                mHandler.getLooper().quitSafely();
                mHandler = null;
                Message msg = Message.obtain(GlRecordActivity.getMainHandler(),10088);
                msg.arg1 = queueMap.size();
                GlRecordActivity.getMainHandler().sendMessage(msg);

            }
        });
    }
}
