package com.ww.cppnative1;

public class Memo {
}

/**
 * related gradle  tasks
 *
 * 1
 *   ----  view task of ---- task ':cpp:native1:generateJsonModelDebug'  group:null   hasInput:true   hasSource:false
 *   ----  props:[externalNativeJsonGenerator.debuggable:true,
 *   externalNativeJsonGenerator.cppFlags:[],
 *   externalNativeJsonGenerator.abis:[ARMEABI_V7A, ARM64_V8A, X86, X86_64],
 *   externalNativeJsonGenerator.sdkFolder:/Users/weiwang/Library/Android/sdk,
 *   externalNativeJsonGenerator.buildArguments:[],
 *   externalNativeJsonGenerator:class com.android.build.gradle.tasks.CmakeServerExternalNativeJsonGenerator,
 *   externalNativeJsonGenerator.ndkFolder:/Users/weiwang/Library/Android/sdk/ndk/22.0.7026061,
 *   externalNativeJsonGenerator.soFolder:/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/lib,
 *   externalNativeJsonGenerator.objFolder:/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/obj,
 *   externalNativeJsonGenerator.cFlags:[]]
 *   ----  task class:com.android.build.gradle.tasks.ExternalNativeBuildJsonTask_Decorated
 * input  :[/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/src/main/cpp/CMakeLists.txt]
 * out  :[
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/.cxx/cmake/debug/armeabi-v7a/android_gradle_build.json,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/.cxx/cmake/debug/arm64-v8a/android_gradle_build.json,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/.cxx/cmake/debug/x86/android_gradle_build.json,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/.cxx/cmake/debug/x86_64/android_gradle_build.json
 * ]
 *     task deps ----  task:preDebugBuild
 *   ---------------
 *
 *
 *
 * 2
 *   ----  view task of ---- task ':cpp:native1:mergeDebugNativeLibs'  group:null   hasInput:true   hasSource:false
 *   ----  props:[packagingOptions.doNotStrip:[], packagingOptions:class com.android.build.gradle.internal.packaging.SerializablePackagingOptions,....*   ----  task class:com.android.build.gradle.internal.tasks.MergeNativeLibsTask_Decorated
 input  :[
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/obj/armeabi-v7a/libnative1-lib.so,
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/obj/x86/libnative1-lib.so,
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/obj/arm64-v8a/libnative1-lib.so,
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/debug/obj/x86_64/libnative1-lib.so
 ]
 * out  :[
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/incremental/debug-mergeNativeLibs/zip-cache,
 /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/merged_native_libs/debug/out
 ]
 *     task deps ----  task:mergeDebugJniLibFolders
 *     task deps ----  task:externalNativeBuildDebug
 *     task deps ----  task:preDebugBuild
 *
 *
 *
 * 3
 *   ----  view task of ---- task ':cpp:native1:mergeReleaseNativeLibs'  group:null   hasInput:true   hasSource:false
 *   ----  props:[packagingOptions.doNotStrip:[], packagingOptions:class com.android.build.gradle.internal.packaging.SerializablePackagingOptions, packagingOptions.merges:[/META-INF/services/**], packagingOptions.pickFirsts:[], packagingOptions.excludes:
 *
 * input  :[/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/release/obj/armeabi-v7a/libnative1-lib.so, /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/release/obj/x86/libnative1-lib.so, /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/release/obj/arm64-v8a/libnative1-lib.so, /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/cmake/release/obj/x86_64/libnative1-lib.so]
 * out  :[/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/incremental/release-mergeNativeLibs/zip-cache, /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/merged_native_libs/release/out]
 *     task deps ----  task:mergeReleaseJniLibFolders
 *     task deps ----  task:externalNativeBuildRelease
 *     task deps ----  task:preReleaseBuild
 *   ---------------
 *
 *
 *
 * 4
 *
 *   ----  view task of ---- task ':cpp:native1:transformNativeLibsWithSyncJniLibsForRelease'  group:null   hasInput:true   hasSource:false
 *   ----  props:[inputTypes:[NATIVE_LIBS], otherInputs:[:], referencedScopes:[PROJECT, LOCAL_DEPS], scopes:[]]
 *   ----  task class:com.android.build.gradle.internal.pipeline.TransformTask_Decorated
 * input  :[
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/stripped_native_libs/release/out/lib/armeabi-v7a/libnative1-lib.so,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/stripped_native_libs/release/out/lib/x86/libnative1-lib.so,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/stripped_native_libs/release/out/lib/arm64-v8a/libnative1-lib.so,
 * /Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/stripped_native_libs/release/out/lib/x86_64/libnative1-lib.so
 * ]
 * out  :[/Users/weiwang/AndroidStudioProjects/TestC2/cpp/native1/build/intermediates/library_and_local_jars_jni/release]
 *     task deps ----  task:stripReleaseDebugSymbols
 *   ---------------
 *
 *
 */
