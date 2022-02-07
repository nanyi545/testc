# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

FILE(GLOB_RECURSE SRC_LIST *.c *.cpp)

##编译静态库
add_library (c_tutorial1 STATIC ${SRC_LIST})

