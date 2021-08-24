#
# Copyright (C)  2017 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

cmake_minimum_required(VERSION 3.4.1)

add_subdirectory(camera2)

set(CMAKE_VERBOSE_MAKEFILE on)

# 用于添加公共的cpp文件 ...
#
# ../ means parent folder
#
#  how to use ?
#  add_library(ndk_camera SHARED
#        .....
#        ${COMMON_SOURCE_DIR}/utils/camera_utils.cpp)
#
set(COMMON_SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../common)



#（1）引入native_app_glue框架头文件所在目录  +  libyuv
include_directories(${ANDROID_NDK}/sources/android/native_app_glue
${COMMON_SOURCE_DIR}
        )

#（2）将android_native_app_glue.c封装成app_glue静态库，否则在创建activity时，会crash
add_library(app_glue STATIC
${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)

#（3）暴露ANativeActivity_onCreate方法，否则在创建activity时，会crash
set(CMAKE_SHARED_LINKER_FLAGS
"${CMAKE_SHARED_LINKER_FLAGS} -u ANativeActivity_onCreate")



message("-----------camera2 project----------")
message(${COMMON_SOURCE_DIR})
message(${ANDROID_NDK})
message(${ANDROID_ABI})



# include libyuv
#头文件成功引入
include_directories(${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/include)
#静态库 FFmpeg   ---> 把-L参数添加到现有的CMAKE_CXX_FLAGS之上
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/lib")

# include x264
#头文件成功引入
include_directories(${CMAKE_SOURCE_DIR}/x264/${ANDROID_ABI}/include)
#静态库 FFmpeg   ---> 把-L参数添加到现有的CMAKE_CXX_FLAGS之上
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/x264/${ANDROID_ABI}/lib")



add_library( # Sets the name of the library.
        native-lib1

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        android_main.cpp
        )




target_link_libraries( # Specifies the target library.
        native-lib1

        # Links the target library to the log library
        # included in the NDK.
        camera2
        android
        log
        m
        app_glue
        camera2ndk
        mediandk
        OpenMAXAL
        libyuv
        x264
        )