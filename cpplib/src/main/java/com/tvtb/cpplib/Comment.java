package com.tvtb.cpplib;


/**
 *
 *  run ndk-build in jni folder .....  to generate .so files  ....
 *
 */
public class Comment {
}


/**

 https://developer.android.google.cn/ndk/guides/android_mk?hl=zh-cn

 Android.mk 文件位于项目 jni/ 目录的子目录中，用于向构建系统描述源文件和共享库



 Android.mk 文件必须先定义 LOCAL_PATH 变量：
 LOCAL_PATH := $(call my-dir)
 此变量表示源文件在开发树中的位置。在上述命令中，构建系统提供的宏函数 my-dir 将返回当前目录（Android.mk 文件本身所在的目录）的路径。



 下一行声明 CLEAR_VARS 变量，其值由构建系统提供。
 include $(CLEAR_VARS)
 CLEAR_VARS 变量指向一个特殊的 GNU Makefile，后者会为您清除许多 LOCAL_XXX 变量，例如 LOCAL_MODULE、LOCAL_SRC_FILES 和 LOCAL_STATIC_LIBRARIES。请注意，GNU Makefile 不会清除 LOCAL_PATH。此变量必须保留其值，因为系统在单一 GNU Make 执行上下文（其中的所有变量都是全局变量）中解析所有构建控制文件。在描述每个模块之前，您必须声明（重新声明）此变量。



 接下来，LOCAL_MODULE 变量存储您要构建的模块的名称。请在应用的每个模块中使用一次此变量。
 LOCAL_MODULE := hello-jni
 每个模块名称必须唯一，且不含任何空格。构建系统在生成最终共享库文件时，会对您分配给 LOCAL_MODULE 的名称自动添加正确的前缀和后缀。例如，上述示例会生成名为 libhello-jni.so 的库。


 下一行会列举源文件，以空格分隔多个文件：
 LOCAL_SRC_FILES := hello-jni.c
 LOCAL_SRC_FILES 变量必须包含要构建到模块中的 C 和/或 C++ 源文件列表。


 最后一行帮助系统将一切连接到一起：
 include $(BUILD_SHARED_LIBRARY)
 BUILD_SHARED_LIBRARY 变量指向一个 GNU Makefile 脚本，该脚本会收集您自最近 include 以来在 LOCAL_XXX 变量中定义的所有信息。此脚本确定要构建的内容以及构建方式。


 **/


/**
 * https://www.jianshu.com/p/92fdedddd177
 *
 * ？？？
 *
 **/