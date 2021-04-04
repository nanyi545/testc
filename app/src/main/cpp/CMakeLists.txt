# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_subdirectory(librtmp)


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
        )