
message("-----------camera2 cmake----------")
message(${CMAKE_SOURCE_DIR})


#所有源文件放入 camera2_source 变量 / file函数
file(GLOB camera2_source *.cpp)

message(${camera2_source})


# include libyuv
#头文件成功引入
include_directories(${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/include)
#静态库 FFmpeg   ---> 把-L参数添加到现有的CMAKE_CXX_FLAGS之上
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libyuv/${ANDROID_ABI}/lib")


#编译静态库
add_library(
        camera2
        STATIC
        ${camera2_source}
)


target_link_libraries( # Specifies the target library.
        camera2

        # Links the target library to the log library
        libyuv
        )