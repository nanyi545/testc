//
// Created by ww on 2022/2/19.
//

#include <jni.h>
#include <stdio.h>
#include "include/myjni_HelloJNI.h"

JNIEXPORT void JNICALL Java_myjni_HelloJNI_sayHello(JNIEnv *env, jobject thisObj) {
printf("Hello jni in package !\n");
return;
}