
message("-----------camera2 cmake----------")

#所有源文件放入 camera2_source 变量 / file函数
file(GLOB camera2_source *.cpp)

message(${camera2_source})


#编译静态库
add_library(
        camera2
        STATIC
        ${camera2_source}
)
