
message("-----------ctest1 cmake----------")

#所有源文件放入 rtmp_source 变量 / file函数
file(GLOB ctest1_source *.cpp)

message(${ctest1_source})


#编译静态库
add_library(
        ctest1
        STATIC
        ${ctest1_source}
)
