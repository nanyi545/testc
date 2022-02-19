//
// Created by ww on 2022/2/19.
//

#include <jni.h>
#include "HelloJNICpp.h"
#include "HelloJNICppImpl.h"

JNIEXPORT void JNICALL Java_HelloJNICpp_sayHello (JNIEnv *env, jobject thisObj) {
    sayHello();  // invoke C++ function
    return;
}