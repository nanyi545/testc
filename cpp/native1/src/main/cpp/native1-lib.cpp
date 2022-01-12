#include <jni.h>
#include <android/log.h>

extern "C" {
#include "ijksdl/android/ijksdl_android_jni.h"
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","jni onload---native lib1");

    SDL_JNI_OnLoad(vm,reserved);


    return JNI_VERSION_1_4;
}


