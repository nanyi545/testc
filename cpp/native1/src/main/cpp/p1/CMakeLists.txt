# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)



# primitive version .... obviously this is not good....
# add_library (p1 STATIC add.c sub.c mul.c div.c)

#  better version ...
#   递归遍历当前目录下的所有.c / .cpp后缀名的文件，并将结果列表保存到SRC_LIST变量中
#   如不需递归，可将GLOB_RECURSE改为GLOB
#FILE(GLOB_RECURSE SRC_LIST *.c *.cpp)
FILE(GLOB_RECURSE SRC_LIST *.c)
message("-----------p1 files----------")
message (${PROJECT_NAME})
message (${SRC_LIST})

##编译静态库
add_library (p1 STATIC ${SRC_LIST})



#
#   source1:
#           https://cloud.tencent.com/developer/article/1518372
#
# keys to use sub-lib with cmake ...
#
#    1 make a folder, create sub-lib with a CMakeLists.txt like this file
#    1-1  add c/cpp files with FILE command
#    1-2  add_library(sub-project-name STATIC ${files})
#
#    2 use this lib in top c lib:
#    2-1  add_subdirectory (sub-project-name)
#    2-2  in target_link_libraries ( ... )  add sub-project-name
#    2-3  in source file: #include "xxx/yyy.h"
#



#   https://cloud.tencent.com/developer/article/1518372
#
#   定义变量
#    源文件较多时，可以定义一个变量来保存，后续只需要引用该变量即可，如下，定义src_list来保存源文件列表，引用变量则使用${}包裹.
#
# message命令是用来打印日志的，它的第一个参数是mode，可省略，常用值如下
#
#STATUS
#附带消息

#WARNING
#CMake警告，继续处理

#AUTHOR_WARNING
#CMake警告（dev），继续处理

#SEND_ERROR
#CMake错误，继续处理，但会跳过生成

#FATAL_ERROR
#CMake错误，停止处理和生成

#
#
#  内置变量
#在cmake中已经内置了一些变量，我们可以直接使用，也可使用set命令去修改
#
#CMAKE_SOURCE_DIR或PROJECT_SOURCE_DIR 表示工程的根目录
#CMAKE_BINARY_DIR或PROJECT_BINARY_DIR 表示编译目录。如果是内部构建，则编译目录与工程根目录相同，如果是外部构建，则表示外部构建创建的编译目录，如上例中的build目录
#CMAKE_CURRENT_SOURCE_DIR 表示当前处理的CMakeLists.txt所在文件夹的路径
#CMAKE_CURRENT_LIST_FILE 当前CMakeLists.txt文件的完整路径
#CMAKE_C_COMPILER和CMAKE_CXX_COMPILER 分别表示C和C++编译器的路径
#PROJECT_NAME 该变量可获取project命令配置的项目名
#
#
