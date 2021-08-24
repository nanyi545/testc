//
// Created by wei wang on 2021-07-28.
//
#include <jni.h>
#include <android/log.h>
#include <android_native_app_glue.h>
#include <thread>
#include "camera2/native_debug.h"
#include "camera2/camera_engine.h"
//#include "camera2/looper.h"
#include "camera2/decoder.cpp"
#include "camera2/util.cpp"

#include <x264.h>


/**
 *  set native window buffer geometry  !!!!
 *  ----->
 * ANativeWindow_setBuffersGeometry
 *
 *
 *
 *
 * 获取可用视频分辨率
 *  --------> ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS
 *     ACameraMetadata_getConstEntry(metadata, ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS, &entry);
 *
 *
 */

/**
 *  ndk 环境变量配置
 *
 *
 *  https://cloud.tencent.com/developer/article/1573318

open -e .bash_profile


mac:


For ijk ....

export PATH=${PATH}:/Users/weiwang/Downloads/android-ndk-r14b
export ANDROID_NDK=/Users/weiwang/Downloads/android-ndk-r14b


export PATH=${PATH}:/Users/weiwang/Library/Android/sdk/ndk/22.0.7026061
export ANDROID_NDK=/Users/weiwang/Library/Android/sdk/ndk/22.0.7026061





 */



/**
 *
 * libyuv 编译  ( Android.mk方式 )
 *
 * https://www.jianshu.com/p/b529fdaaf694
 *
 *
 *  1    git clone https://chromium.googlesource.com/libyuv/libyuv
 *  2    源码文件夹 libyuv 重命名--->jni
 *  3    修改 Android.mk    ---->   注释 jpeg 相关的代码
 *      --->  ifneq ($(LIBYUV_DISABLE_JPEG), "yes")
              LOCAL_SHARED_LIBRARIES := libjpeg
              endif
        --->  LOCAL_SHARED_LIBRARIES := libjpeg
        --->  ifneq ($(LIBYUV_DISABLE_JPEG), "yes")
              LOCAL_SRC_FILES += \
                  source/convert_jpeg.cc      \
                  source/mjpeg_decoder.cc     \
                  source/mjpeg_validate.cc
              common_CFLAGS += -DHAVE_JPEG
              LOCAL_SHARED_LIBRARIES := libjpeg
              endif
    4   Application.mk

        APP_PLATFORM := android-21
        APP_ABI := armeabi-v7a

    5  cd 到 jni folder 的parent  ndk-build

 *
 *
 *
 */
class looper2: public looper {
    int aaa = 0;
    virtual void handle(int what, void* obj);
};

void looper2::handle(int what, void* obj) {
    LOGI("Looper-------  2 handle:%d   aaa:%d",what,aaa);
    aaa++;
    if(aaa>100){
        quit();
    }
    switch (what) {
        case 1:
            break;
    }
}

looper2* mlooper2;





extern "C"
JNIEXPORT void JNICALL
Java_com_tvtb_ndkcameratest1_MainActivity_test1(JNIEnv *env, jobject thiz) {
//    mlooper->post(3, NULL, false );
}

bool showCameraPreview = true;

extern "C"
JNIEXPORT void JNICALL
Java_com_tvtb_ndkcameratest1_MainActivity_setCamera(JNIEnv *env, jclass jclass1,jboolean useCamera) {
    showCameraPreview = useCamera;
}





/*
 * SampleEngine global object
 */
static CameraEngine* pEngineObj = nullptr;
CameraEngine* GetAppEngine(void) {
    ASSERT(pEngineObj, "AppEngine has not initialized");
    return pEngineObj;
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    // ??? not called ...
    LOGI("JNI_OnLoad111");
    __android_log_print(ANDROID_LOG_VERBOSE, "JNI_OnLoad", "JNI_OnLoad---------");
    return JNI_VERSION_1_6;
}


const char* ToString1(int32_t v)
{
    switch (v)
    {
        case APP_CMD_INPUT_CHANGED:   return "APP_CMD_INPUT_CHANGED";
        case APP_CMD_INIT_WINDOW:   return "APP_CMD_INIT_WINDOW";
        case APP_CMD_TERM_WINDOW:   return "APP_CMD_TERM_WINDOW";
        case APP_CMD_WINDOW_RESIZED:   return "APP_CMD_WINDOW_RESIZED";
        case APP_CMD_WINDOW_REDRAW_NEEDED:   return "APP_CMD_WINDOW_REDRAW_NEEDED";
        case APP_CMD_CONTENT_RECT_CHANGED:   return "APP_CMD_CONTENT_RECT_CHANGED";
        case APP_CMD_GAINED_FOCUS:   return "APP_CMD_GAINED_FOCUS";
        case APP_CMD_LOST_FOCUS:   return "APP_CMD_LOST_FOCUS";
        case APP_CMD_CONFIG_CHANGED:   return "APP_CMD_CONFIG_CHANGED";
        case APP_CMD_LOW_MEMORY:   return "APP_CMD_LOW_MEMORY";
        case APP_CMD_START:   return "APP_CMD_START";
        case APP_CMD_RESUME:   return "APP_CMD_RESUME";
        case APP_CMD_PAUSE:   return "APP_CMD_PAUSE";
        case APP_CMD_STOP:   return "APP_CMD_STOP";
        case APP_CMD_DESTROY:   return "APP_CMD_DESTROY";
        default:      return "[Unknown ]";
    }
}

/**
 *
 * start
 *

07-29 12:47:17.436 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:10  APP_CMD_START
07-29 12:47:17.440 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:11  APP_CMD_RESUME
07-29 12:47:17.443 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:0  APP_CMD_INPUT_CHANGED
07-29 12:47:17.467 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:1  APP_CMD_INIT_WINDOW
07-29 12:47:17.468 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:3  APP_CMD_WINDOW_RESIZED
07-29 12:47:17.469 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:6  APP_CMD_GAINED_FOCUS
07-29 12:47:17.480 32729-392/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:4  APP_CMD_WINDOW_REDRAW_NEEDED


 end

07-29 12:47:06.084 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:7  APP_CMD_LOST_FOCUS
07-29 12:47:06.085 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:13  APP_CMD_PAUSE
07-29 12:47:06.501 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:2  APP_CMD_TERM_WINDOW
07-29 12:47:06.508 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:14  APP_CMD_STOP
07-29 12:47:06.510 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:0  APP_CMD_INPUT_CHANGED
07-29 12:47:06.510 32729-32756/com.tvtb.ndkcameratest1 V/ProcessAndroidCmd: ProcessAndroidCmd:15  APP_CMD_DESTROY

 *
 *
 */

static void ProcessAndroidCmd(struct android_app* app, int32_t cmd) {
    CameraEngine* engine = reinterpret_cast<CameraEngine*>(app->userData);
    LOGI("ProcessAndroidCmd:  %d  %s",cmd,ToString1(cmd));
    switch (cmd) {
        case APP_CMD_INIT_WINDOW:
            if (engine->AndroidApp()->window != NULL) {
                engine->SaveNativeWinRes(ANativeWindow_getWidth(app->window),
                                         ANativeWindow_getHeight(app->window),
                                         ANativeWindow_getFormat(app->window));
                engine->OnAppInitWindow();
            }
            break;
        case APP_CMD_TERM_WINDOW:
            engine->OnAppTermWindow();
            ANativeWindow_setBuffersGeometry(
                    app->window, engine->GetSavedNativeWinWidth(),
                    engine->GetSavedNativeWinHeight(), engine->GetSavedNativeWinFormat());
            break;
        case APP_CMD_CONFIG_CHANGED:
            engine->OnAppConfigChange();
            break;
        case APP_CMD_LOST_FOCUS:
            break;
    }
}




//    编码器
x264_t *videoCodec = 0;

extern "C" void android_main(struct android_app* state) {

    /**
     *  init global engine instance ..
     */
    CameraEngine engine(state);
    pEngineObj = &engine;

    state->userData = reinterpret_cast<void*>(&engine);
    state->onAppCmd = ProcessAndroidCmd;


    //    定义参数
    x264_param_t param;
//    参数赋值   x264  麻烦  编码器 速度   直播  越快 1  越慢2
    x264_param_default_preset(&param, "ultrafast", "zerolatency");
    videoCodec = x264_encoder_open(&param);

    if(videoCodec==NULL){
        LOGI("x264 init --- sucess");
    } else {
        LOGI("x264 init --- fail");
    }



    /**
     *
     */
    mlooper2 = new looper2();


    // loop waiting for stuff to do.
    while (1) {
        // Read all pending events.
        int events;
        int ident;
        struct android_poll_source* source;

        while ( (ident = ALooper_pollAll(0, NULL, &events, (void**)&source)) >= 0) {

            ASSERT(source , "Failed to get task");

            // Process this event.
            if (source != NULL) {
                source->process(state, source);
            }

            // Check if we are exiting.
            if (state->destroyRequested != 0) {
                engine.DeleteCamera();
                pEngineObj = nullptr;
                return;
            }
        }
        mlooper2->post(1, NULL);
        pEngineObj->DrawFrame();

    }

}



/**
 * Handle Android System APP_CMD_INIT_WINDOW message
 *   Request camera persmission from Java side
 *   Create camera object if camera has been granted
 *
 *   ------------------------------
 *
 *   preview entry point !!!!
 *
 */
void CameraEngine::OnAppInitWindow(void) {
    if(!showCameraPreview){
        return;
    }
    if (!cameraGranted_) {
        LOGI("not granted");

        // Not permitted to use camera yet, ask(again) and defer other events
        RequestCameraPermission();
        return;
    }
    LOGI(" granted");

    rotation_ = GetDisplayRotation();
    LOGI(" rotation_  %d",rotation_);

    CreateCamera();
    ASSERT(camera_, "CameraCreation Failed");

    EnableUI();

    // NativeActivity end is ready to display, start pulling images
    cameraReady_ = true;
    camera_->StartPreview(true);
}

/**
 * Handle APP_CMD_TEMR_WINDOW
 */
void CameraEngine::OnAppTermWindow(void) {
    cameraReady_ = false;
    DeleteCamera();
}

/**
 * Handle APP_CMD_CONFIG_CHANGED
 */
void CameraEngine::OnAppConfigChange(void) {
    int newRotation = GetDisplayRotation();

    if (newRotation != rotation_) {
        OnAppTermWindow();

        rotation_ = newRotation;
        OnAppInitWindow();
    }
}

/**
 * Retrieve saved native window width.
 * @return width of native window
 */
int32_t CameraEngine::GetSavedNativeWinWidth(void) {
    return savedNativeWinRes_.width;
}

/**
 * Retrieve saved native window height.
 * @return height of native window
 */
int32_t CameraEngine::GetSavedNativeWinHeight(void) {
    return savedNativeWinRes_.height;
}

/**
 * Retrieve saved native window format
 * @return format of native window
 */
int32_t CameraEngine::GetSavedNativeWinFormat(void) {
    return savedNativeWinRes_.format;
}

/**
 * Save original NativeWindow Resolution
 * @param w width of native window in pixel
 * @param h height of native window in pixel
 * @param format
 */
void CameraEngine::SaveNativeWinRes(int32_t w, int32_t h, int32_t format) {
    savedNativeWinRes_.width = w;
    savedNativeWinRes_.height = h;
    savedNativeWinRes_.format = format;
    LOGI("SaveNativeWinRes   w:%d  h:%d",w,h);
}


