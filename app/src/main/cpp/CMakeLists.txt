# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_subdirectory(librtmp)
add_subdirectory(ctest1)


message("-----------ss----------")
message(${CMAKE_BUILD_TYPE})
message(${ANDROID_PLATFORM})
message(${CMAKE_SOURCE_DIR})
message(${ANDROID_ABI})
message(${CMAKE_CXX_FLAGS})
message("-----------ee----------")

#   app/.cxx/cmake/debug/armeabi-v7a/build_output.txt
#   app/.cxx/cmake/debug/arm64-v8a/build_output.txt



# include x264
#头文件成功引入
include_directories(${CMAKE_SOURCE_DIR}/x264/${ANDROID_ABI}/include
#        ${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/include
#        ${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/include/libyuv
        )
#静态库     ---> 把-L参数添加到现有的CMAKE_CXX_FLAGS之上
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/x264/${ANDROID_ABI}/lib")


#头文件成功引入
include_directories(${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/include)
#静态库    ---> 把-L参数添加到现有的CMAKE_CXX_FLAGS之上
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/lib")



#ffmpeg头文件
include_directories(include)
#添加一个系统查找目录
#添加系统环境变量
#ffmpeg动态库
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")



# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        # recommanded not to use glob /
        #          https://stackoverflow.com/questions/3201154/automatically-add-all-files-in-a-folder-to-a-target-using-cmake
        giflib/dgif_lib.c
        giflib/egif_lib.c
        giflib/gif_err.c
        giflib/gif_font.c
        giflib/gif_hash.c
        giflib/gifalloc.c
        giflib/openbsd-reallocarray.c
        giflib/quantize.c
        giflib/gif_lib.h
        )

# Searches for a specified prebuilt library and stores the path as a variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library you want to add. CMake verifies that the library exists before
# completing its build.

# android log lib
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


# android graphics lib
find_library(  jnigraphics-lib
        jnigraphics )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${jnigraphics-lib}
        rtmp
        x264
        ctest1
        libyuv
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        swresample
        swscale
        android  # native_window
        )