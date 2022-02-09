//
// Created by wei wang on 2022-02-08.
//
/**
 *
 *
gcc test_macr.c -o macr

gcc -E test_macr.c -o test_macr.i

     * 进阶篇（七）
     * https://cloud.tencent.com/developer/article/1450925?from=article.detail.1450928

 预处理的高级使用

在预处理指令中，最复杂的是宏定义。

 ------------------------

 带参的宏
也称函数式宏，宏函数。

1 #define 标识符(a,b,c,...,d) 替换列表
2 #define MAX(x,y) ((x)>(y)?(x):(y))

 关于小括号的注意事项
1、如果宏替换列表中有运算符号，那么必须将整个替换列表放入小括号中
#define TOW_PI (2*3.14)

2、如果宏有参数，那么每个参数在替换列表中出现时，都要放在小括号中
#define MAX(x,y) ((x)>(y)?(x):(y))

 ------------------------

运算符
宏定义包含两个专用运算符#和##

# 运算符可以用来字符串化宏函数里的参数，它出现在带参数宏的替换列表中。
## 运算符可以将两个记号（如标识符）粘合在一起。


 *
 *
 */
#include <stdio.h>

#define MAX(x,y) ((x)>(y)?(x):(y))

// # 运算符可以用来字符串化宏函数里的参数，它出现在带参数宏的替换列表中。
#define PRINT_INT(n) printf(#n "=%d\n",n)
// //宏展开为
// printf("i/j""=%d\n",i/j)
// //等价于（C语言相邻字符串字面量会被合并）
// printf("i/j=%d\n",i/j)


// ## 运算符可以将两个记号（如标识符）粘合在一起。
#define MK_ID(n) i##n
//int MK_ID(1),MK_ID(2);
//宏展开后
//int i1,i2;


//#define GENERIC_MAX(type)    \
//type type##_max(type x,type y){ \
//return x > y ? x : y;
//}


int main() {
    printf("max(2,3):%d\n", MAX(2, 3));
    PRINT_INT(9/2);


    // valid
    int MK_ID(p)=10;
    printf("ip:%d \n", MK_ID(p));

    int MK_ID(1)=9;
    printf("i1:%d \n", MK_ID(1));

    return 0;
}
