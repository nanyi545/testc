# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(common_sources

#        src/main/cpp  //  todo  where is "base" defined ??
        native1-lib.cpp
        test1.c
        q1.c
        ijksdl/ijksdl_thread.c
        ijksdl/android/ijksdl_android_jni.c
        )


message("-----------ss----------")
message(${CMAKE_BUILD_TYPE})
message(${ANDROID_PLATFORM})
message(${CMAKE_SOURCE_DIR})
message(${ANDROID_ABI})
message(${CMAKE_CXX_FLAGS})
message(${common_sources})
message("-----------ee----------")




add_library( # Sets the name of the library.
        native1-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
#        native1-lib.cpp
#        util/aa.cpp
        ${common_sources}
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



include_directories(
        src/main/cpp
        src/main/cpp/ijksdl
)




# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native1-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${jnigraphics-lib}
        )