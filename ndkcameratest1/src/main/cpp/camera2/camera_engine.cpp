/**
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** Description
 *   Demonstrate NDK Camera interface added to android-24
 */

#include <cstdio>
#include "camera_engine.h"
#include "native_debug.h"

#include "media/NdkMediaCodec.h"
#include "media/NdkMediaMuxer.h"

/**
 * constructor and destructor for main application class
 * @param app native_app_glue environment
 * @return none
 */
CameraEngine::CameraEngine(android_app* app)
    : app_(app),
      cameraGranted_(false),
      rotation_(0),
      cameraReady_(false),
      camera_(nullptr),
      yuvReader_(nullptr),
      jpgReader_(nullptr) {
  memset(&savedNativeWinRes_, 0, sizeof(savedNativeWinRes_));
}

CameraEngine::~CameraEngine() {
  cameraReady_ = false;
  DeleteCamera();
}

struct android_app* CameraEngine::AndroidApp(void) const {
  return app_;
}

/**
 * Create a camera object for onboard BACK_FACING camera
 */
void CameraEngine::CreateCamera(void) {
  // Camera needed to be requested at the run-time from Java SDK
  // if Not granted, do nothing.
  if (!cameraGranted_ || !app_->window) {
    LOGW("Camera Sample requires Full Camera access");
    return;
  }

  int32_t displayRotation = GetDisplayRotation();
  rotation_ = displayRotation;

  camera_ = new NDKCamera();
  ASSERT(camera_, "Failed to Create CameraObject");

  int32_t facing = 0, angle = 0, imageRotation = 0;
  if (camera_->GetSensorOrientation(&facing, &angle)) {
    if (facing == ACAMERA_LENS_FACING_FRONT) {
      imageRotation = (angle + rotation_) % 360;
      imageRotation = (360 - imageRotation) % 360;
    } else {
      imageRotation = (angle - rotation_ + 360) % 360;
    }
  }
  LOGI("Phone Rotation: %d, Present Rotation Angle: %d", rotation_,imageRotation);


  ImageFormat view{0, 0, 0}, capture{0, 0, 0};
  camera_->MatchCaptureSizeRequest(app_->window, &view, &capture);

  ASSERT(view.width && view.height, "Could not find supportable resolution");

  // Request the necessary nativeWindow to OS
  bool portraitNativeWindow =
      (savedNativeWinRes_.width < savedNativeWinRes_.height);
  ANativeWindow_setBuffersGeometry(
      app_->window, portraitNativeWindow ? view.height : view.width,
      portraitNativeWindow ? view.width : view.height, WINDOW_FORMAT_RGBA_8888);

  yuvReader_ = new ImageReader(&view, AIMAGE_FORMAT_YUV_420_888);
  yuvReader_->SetPresentRotation(imageRotation);
  jpgReader_ = new ImageReader(&capture, AIMAGE_FORMAT_JPEG);
  jpgReader_->SetPresentRotation(imageRotation);
  jpgReader_->RegisterCallback(this, [this](void* ctx, const char* str) -> void {
    reinterpret_cast<CameraEngine* >(ctx)->OnPhotoTaken(str);
  });

  ANativeWindow* window1 = yuvReader_->GetNativeWindow();
  if(window1 == NULL){
    LOGI("window1 null");
  } else {
    LOGI("window1 not null");
  };
  ANativeWindow* window2 = jpgReader_->GetNativeWindow();
  if(window1 == window2){
    LOGI("window12 equal");
  } else {
    LOGI("window12 not equal");
  };



  /**
   *  print address of a pointer ....
   */
  LOGI("1--window1:%p    window2:%p", &window1, &window2);

  // now we could create session
  camera_->CreateSession(yuvReader_->GetNativeWindow(),
                         jpgReader_->GetNativeWindow(), imageRotation);

  LOGI("2--window1:%p    window2:%p", &window1, &window2);

}

void CameraEngine::DeleteCamera(void) {
  cameraReady_ = false;
  if (camera_) {
    delete camera_;
    camera_ = nullptr;
  }
  if (yuvReader_) {
    delete yuvReader_;
    yuvReader_ = nullptr;
  }
  if (jpgReader_) {
    delete jpgReader_;
    jpgReader_ = nullptr;
  }
}

/**
 * Initiate a Camera Run-time usage request to Java side implementation
 *  [ The request result will be passed back in function
 *    notifyCameraPermission()]
 */
void CameraEngine::RequestCameraPermission() {
  if (!app_) return;

  JNIEnv* env;
  ANativeActivity* activity = app_->activity;
  activity->vm->GetEnv((void**)&env, JNI_VERSION_1_6);

  activity->vm->AttachCurrentThread(&env, NULL);

  jobject activityObj = env->NewGlobalRef(activity->clazz);
  jclass clz = env->GetObjectClass(activityObj);
  env->CallVoidMethod(activityObj,
                      env->GetMethodID(clz, "RequestCamera", "()V"));
  env->DeleteGlobalRef(activityObj);

  activity->vm->DetachCurrentThread();
  LOGI("RequestCameraPermission-----");

}
/**
 * Process to user's sensitivity and exposure value change
 * all values are represented in int64_t even exposure is just int32_t
 * @param code ACAMERA_SENSOR_EXPOSURE_TIME or ACAMERA_SENSOR_SENSITIVITY
 * @param val corresponding value from user
 */
void CameraEngine::OnCameraParameterChanged(int32_t code, int64_t val) {
  camera_->UpdateCameraRequestParameter(code, val);
}

/**
 * The main function rendering a frame. In our case, it is yuv to RGBA8888
 * converter
 */
 int pcount = 0;
int64_t cout2 = 0;
int cout3 = 0;

AMediaCodec* mCodec;

int64_t getNowUs(){
  timeval tv;
  gettimeofday(&tv, 0);
  return (int64_t)tv.tv_sec * 1000000 + (int64_t)tv.tv_usec;
}

int mWidth1 = 640;
int mHeight1 = 480;
FILE *fp;
AMediaMuxer* mMuxer;
bool mMuxerStarted = false;
ssize_t mTrackIndex;

void callOnFirstFrame(CameraEngine* engine , bool mp4 ){
//     int mWidth1 = engine->GetSavedNativeWinWidth();
//     int mHeight1 = engine->GetSavedNativeWinHeight();

//   LOGW("w640  h480");
   LOGI("first frame w:%d  h:%d", mWidth1,mHeight1);

   std::string mStrMime = "video/avc";

   AMediaFormat *format = AMediaFormat_new();
   AMediaFormat_setString(format, AMEDIAFORMAT_KEY_MIME, mStrMime.c_str());
   AMediaFormat_setInt32(format, AMEDIAFORMAT_KEY_WIDTH, mWidth1);
   AMediaFormat_setInt32(format, AMEDIAFORMAT_KEY_HEIGHT, mHeight1);

//    AMediaFormat_setInt32(format, AMEDIAFORMAT_KEY_BITRATE_MODE, 2);

//    android.media.MediaCodecInfo
//    public static final int COLOR_FormatYUV420Flexible = 0x7F420888;

//    AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_COLOR_FORMAT,0x7F420888);

//   AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_COLOR_FORMAT,19);
   AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_COLOR_FORMAT,21); // this works


    AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_FRAME_RATE,30);
   AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_BIT_RATE,mWidth1 * mHeight1 * 2);
//    AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_BIT_RATE,50000);

    AMediaFormat_setInt32(format,AMEDIAFORMAT_KEY_I_FRAME_INTERVAL,4);

   const char *s = AMediaFormat_toString(format);
   LOGI("encoder video format: %s", s);


   mCodec = AMediaCodec_createEncoderByType(mStrMime.c_str());
   ASSERT(mCodec, "encoder fail ")

   media_status_t status = AMediaCodec_configure(mCodec, format, NULL, NULL,
                                                 AMEDIACODEC_CONFIGURE_FLAG_ENCODE);





   if (status != 0) {
     LOGI( "encoder    AMediaCodec_configure() failed with error %i ",
               (int) status);
   } else {

   }


//   fp = fopen("/sdcard/Download/hehe/out_1.h264", "wb");  //  wb / w+
//    fp = fopen("/sdcard/aaa/record4_.h264", "wb");  //  wb / w+

    if(mp4){
        fp = fopen("/sdcard/Download/aaa/a1.mp4", "wb");
    } else {
        fp = fopen("/sdcard/Download/aaa/record4_.h264", "wb");
    }


   int mFd;

    if(NULL == fp) {
        LOGI("encoder fopen erro: %s \n",strerror(errno));

    } else {
        LOGI("encoder open f success");
      mFd = fileno(fp);
    }


    if(mp4){
        if (fp != NULL) {
            mFd = fileno(fp);
        } else {
            mFd = -1;
        }
        if(mMuxer == NULL)
            mMuxer = AMediaMuxer_new(mFd, AMEDIAMUXER_OUTPUT_FORMAT_MPEG_4);
        mMuxerStarted = false;
    }

    // start encoding
    AMediaCodec_start(mCodec);

}

void writeYuv(CameraEngine* engine, AImage* image ) {


    FILE *yuv;
    yuv = fopen("/sdcard/Download/aaa/t1.yuv", "wb");  //  wb / w+

    uint8_t *yPixel, *uPixel, *vPixel;
    int32_t yLen, uLen, vLen;
    AImage_getPlaneData(image, 0, &yPixel, &yLen);
    AImage_getPlaneData(image, 1, &vPixel, &vLen);
    AImage_getPlaneData(image, 2, &uPixel, &uLen);

    LOGI("ffff --- yLen:%d",yLen);  // 307200
    LOGI("ffff --- vLen:%d",vLen);  // 153599
    LOGI("ffff --- uLen:%d",uLen);  // 153599


    // write YUV img to   t1.yuv
    fwrite(yPixel,1,yLen,yuv);
    fwrite(uPixel,1,uLen,yuv);
    fwrite(vPixel,1,vLen,yuv);



//    uint8_t const1[1]   = { '|' };
//    uint8_t *buf[5];
//    int32_t yLen = 5;
//    uint8_t arrayOne[12]   = { 'a','b','c','d','e','b',0x01, 0xC1, 0x00, 0x01 };
//    for (int i=0;i<yLen;i++){
////        fwrite(arrayOne+i,1,1,yuv);
//        // write as int ...
//        int cast = arrayOne[i];
//        fwrite(&cast,sizeof(int),1,yuv);
//        LOGI("ffff --- %d",cast);
//        fwrite(const1,1,1,yuv);
//    }
//    LOGI("ffff --- 1 frame ");


    fflush(yuv);
    fclose(yuv);

}


void encodeFrame2mp4(CameraEngine* engine, AImage* image ) {

    bool eos = false;
    if(pcount>900){
        LOGI("encoder mp4 --- stop ");
        eos = true;
    }

    if(!eos){
        LOGI("encoder mp4 --- 1 frame ");
        ssize_t bufidx = AMediaCodec_dequeueInputBuffer(mCodec,0);
        if(bufidx>=0) {
            size_t bufsize;
            int64_t pts = getNowUs();
            uint8_t *buf = AMediaCodec_getInputBuffer(mCodec, bufidx, &bufsize);

            //填充yuv数据
            int frameLenYuv = mWidth1 * mHeight1 * 3 / 2;
            memset (buf, 1 ,frameLenYuv);

//  put YUV data in en-coder
            uint8_t *yPixel, *uPixel, *vPixel;
            int32_t yLen, uLen, vLen;
            AImage_getPlaneData(image, 0, &yPixel, &yLen);
            AImage_getPlaneData(image, 1, &vPixel, &vLen);
            AImage_getPlaneData(image, 2, &uPixel, &uLen);


            memcpy(buf, yPixel, yLen);
            int uIndex = 0, vIndex = 0;
            for( int i=yLen ; i<frameLenYuv-2; i+=2 ){
                buf[i+1] = uPixel[vIndex];
                buf[i+2] = vPixel[uIndex];
                vIndex+=2;
                uIndex+=2;
            }
//        LOGI("encoder --- yLen:%d  vLen:%d   uLen:%d",yLen,vLen,uLen );
            AMediaCodec_queueInputBuffer(mCodec, bufidx, 0, frameLenYuv, pts, 0);
        }
        LOGI("encoder  mp4 --- in index:%d",bufidx);
    }


    if(eos){
        media_status_t eosRet = AMediaCodec_signalEndOfInputStream(mCodec);
        LOGI("encoder  mp4 ---  eosRet:%d",eosRet);
        media_status_t muxStopRet = AMediaMuxer_stop(mMuxer);
        LOGI("encoder  mp4 ---  muxStopRet:%d",muxStopRet);

    }


    AMediaCodecBufferInfo info;


    while (true){
        //取输出buffer
        auto outindex = AMediaCodec_dequeueOutputBuffer(mCodec, &info, 0);
        LOGI("encoder  mp4 --- out index:%d",outindex);

        if (outindex == AMEDIACODEC_INFO_TRY_AGAIN_LATER) {
            if (!eos) {
                break;
            } else {
                LOGI("encoder  mp4     video no output available, spinning to await EOS");
            }
        }  else if(outindex == AMEDIACODEC_INFO_OUTPUT_BUFFERS_CHANGED ){

        } else if (outindex == AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED) {
            LOGI("encoder  mp4 --- out index AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED");

            AMediaFormat *fmt = AMediaCodec_getOutputFormat(mCodec);
            const char *s = AMediaFormat_toString(fmt);

            mTrackIndex = AMediaMuxer_addTrack(mMuxer, fmt);

            if(  mTrackIndex != -1 ) {
                LOGI("encoder  mp4    AMediaMuxer_start");
                AMediaMuxer_start(mMuxer);
                mMuxerStarted = true;
            }

        } else {
            size_t outsize;
            uint8_t *buf = AMediaCodec_getOutputBuffer(mCodec , outindex, &outsize);

            size_t dataSize = info.size;
            if (dataSize != 0) {
                if (!mMuxerStarted) {
                    LOGI("encoder  mp4   muxer has't started");
                }
                AMediaMuxer_writeSampleData(mMuxer, mTrackIndex, buf, &info);
            }

            LOGI("encoder  mp4 --- out info.flags :%d",info.flags);



            AMediaCodec_releaseOutputBuffer(mCodec, outindex, false);


            if ((info.flags & AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM) != 0) {
                if (!eos) {
                    LOGI("encoder  mp4   reached end of stream unexpectly");
                } else {
                    LOGI("encoder  mp4    video end of stream reached");
                }
                break;
            }
        }


    }

    //取输出buffer
//    auto outindex = AMediaCodec_dequeueOutputBuffer(mCodec, &info, 0);
//    while (outindex != AMEDIACODEC_INFO_TRY_AGAIN_LATER) {
//        if (outindex == AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED) {
//            LOGI("encoder  mp4 --- out index AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED");
//            AMediaFormat *fmt = AMediaCodec_getOutputFormat(mCodec);
//            const char *s = AMediaFormat_toString(fmt);
//            mTrackIndex = AMediaMuxer_addTrack(mMuxer, fmt);
//            if(  mTrackIndex != -1 ) {
//                LOGI("encoder  mp4    AMediaMuxer_start");
//                AMediaMuxer_start(mMuxer);
//                mMuxerStarted = true;
//            }
//        }
//        if (outindex == AMEDIACODEC_INFO_OUTPUT_BUFFERS_CHANGED) {
//        }
//        if(outindex>=0){
//            //在这里取走编码后的数据
//            //释放buffer给编码器
//            size_t outsize;
//            uint8_t *buf = AMediaCodec_getOutputBuffer(mCodec , outindex, &outsize);
//            size_t dataSize = info.size;
//            if (dataSize != 0) {
//                if (!mMuxerStarted) {
//                    LOGI("encoder  mp4   muxer has't started");
//                }
//                AMediaMuxer_writeSampleData(mMuxer, mTrackIndex, buf, &info);
//            }
//        }
//        LOGI("encoder mp4 --- out index:%d   size:%d   frame:%d   flag:%d",outindex,info.size,pcount ,info.flags);
//        AMediaCodec_releaseOutputBuffer(mCodec, outindex, false);
//        outindex = AMediaCodec_dequeueOutputBuffer(mCodec, &info, 0);
//    }


}



void encodeFrame2H264(CameraEngine* engine, AImage* image ) {
    LOGI("encoder --- 1 frame ");
    ssize_t bufidx = AMediaCodec_dequeueInputBuffer(mCodec,0);
    //LOGD("input buffer %zd\n",bufidx);
    if(bufidx>=0) {

        LOGI("encoder --- in pts1:%" PRId64,cout2);
        size_t bufsize;
        int64_t pts = getNowUs();
//        int64_t pts = cout2*13333+10;
//        cout2=cout2+1;
//        int64_t pts  = pcount*13333 + 10;
        LOGI("encoder --- in pts:%" PRId64 ,pts);
        uint8_t *buf = AMediaCodec_getInputBuffer(mCodec, bufidx, &bufsize);

        //填充yuv数据
        int frameLenYuv = mWidth1 * mHeight1 * 3 / 2;
//        int frameLenYuv = mWidth1 * mHeight1;


// stride  ... what is it ??
//        int32_t yStride, uvStride;
//        AImage_getPlaneRowStride(image, 0, &yStride);
//        AImage_getPlaneRowStride(image, 1, &uvStride);
        memset (buf, 1 ,frameLenYuv);


//  put YUV data in en-coder
        uint8_t *yPixel, *uPixel, *vPixel;
        int32_t yLen, uLen, vLen;
        AImage_getPlaneData(image, 0, &yPixel, &yLen);
        AImage_getPlaneData(image, 1, &vPixel, &vLen);
        AImage_getPlaneData(image, 2, &uPixel, &uLen);


        memcpy(buf, yPixel, yLen);
        int uIndex = 0, vIndex = 0;
        /**
         *  is it optimal ???
         */
        for( int i=yLen ; i<frameLenYuv-2; i+=2 ){
            buf[i+1] = uPixel[vIndex];
            buf[i+2] = vPixel[uIndex];
            vIndex+=2;
            uIndex+=2;
        }
//        LOGI("encoder --- yLen:%d  vLen:%d   uLen:%d",yLen,vLen,uLen );
        AMediaCodec_queueInputBuffer(mCodec, bufidx, 0, frameLenYuv, pts, 0);
    }

    LOGI("encoder --- in index:%d",bufidx);

    /**

     其中有个字段是flags，它有几种常量情况。
flags = 4；End of Stream。
flags = 2；首帧信息帧。
flags = 1；关键帧。
flags = 0；普通帧。

     */
    AMediaCodecBufferInfo info;
    //取输出buffer
    auto outindex = AMediaCodec_dequeueOutputBuffer(mCodec, &info, 0);
    while (outindex >= 0) {

        //在这里取走编码后的数据
        //释放buffer给编码器
        size_t outsize;
        uint8_t *buf = AMediaCodec_getOutputBuffer(mCodec , outindex, &outsize);


        fwrite(buf,1,info.size,fp);
        fflush(fp);

        LOGI("encoder --- out index:%d   size:%d   frame:%d   flag:%d",outindex,info.size,pcount ,info.flags);


        AMediaCodec_releaseOutputBuffer(mCodec, outindex, false);
        outindex = AMediaCodec_dequeueOutputBuffer(mCodec, &info, 0);

    }
    LOGI("encoder --- out index:%d",outindex);

}





void CameraEngine::DrawFrame(void) {

  if (!cameraReady_ || !yuvReader_) {
    /**
     * camera not ready
     */
//      if(pcount<100000) {
//          LOGI("----DrawFrame not ready 1---- :%d",pcount);
//          pcount ++ ;
//      }
      return;
  }
  AImage* image = yuvReader_->GetNextImage();
  if (!image) {

    /**
     *  camera ready but  ,   no next img ....
     */
//      if(pcount<100000) {
//          LOGI("----DrawFrame not ready 2---- :%d",pcount);
//          pcount ++ ;
//      }
      return;
  }

//    if(pcount<100000) {
//        LOGI("----DrawFrame 3 ---- :%d",pcount);
//        pcount ++ ;
//    }

  /**
   *  camera ready has next img ....
   */

  LOGI("frame count:%d",pcount);
  if(pcount==0){
//      callOnFirstFrame(this , true);
//      encodeFrame2mp4(this, image );
//      encodeFrame2H264(this, image );
//      writeYuv(this, image);
  } else {
//      encodeFrame2mp4(this, image );
//      encodeFrame2H264(this, image );
  }

  pcount++;


  ANativeWindow_acquire(app_->window);
  ANativeWindow_Buffer buf;
  if (ANativeWindow_lock(app_->window, &buf, nullptr) < 0) {
    yuvReader_->DeleteImage(image);
    return;
  }

  yuvReader_->DisplayImage(&buf, image);

  ANativeWindow_unlockAndPost(app_->window);
  ANativeWindow_release(app_->window);

}

