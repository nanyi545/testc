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


  ------------------------
 创建包含多条语句的宏

使用do-while编写多条语句宏是一种C语言的技巧。
 ------------------------
1 #define ECHO(s) \
2    do{
3        gets(s);
4        puts(s);
5    }while(0)
6
7 ECHO(str);
8 //宏展开后
9 do{gets(str);puts(str);}while(0);

 ----


 关于宏的一些总结


使用宏函数，可以减少函数栈的调用，稍微提升一点性能，相当于C++中的内联的概念，在C99中也实现了内联函数的新特性。缺点是宏展开后，增加了编译后的体积大小。
宏参数没有类型检查，缺少安全机制。
宏的替换列表可以包含对其他宏的调用
宏定义的作用范围，直到出现这个宏的文件末尾
宏不能被定义两次，除非新定义与旧定义完全一样
可以使用#undef 标识符取消宏定义，若宏不存在，则该指令没有作用

 -------------------

 条件编译
#if和#endif
 defined运算符
#ifdef和#ifndef
#elif和#else
这两个指令结合#if


        条件编译主要可以用于   !!!! todo
1、需要测试调试代码时，打印更多信息，正式发布时则去除这些代码
2、跨平台，跨编译器。对于不同平台，可以包含不同的代码，使用不同的编译器特性
3、屏蔽代码。使用注释符号注释代码时，有一个缺点，注释无法嵌套，即不能注释中间包含注释的代码，使用条件编译则很方便

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


#define GENERIC_MAX(type)    \
    type type##_max(type x,type y){ \
        return x > y ? x : y;\
        }


//  https://stackoverflow.com/questions/1067226/c-multi-line-macro-do-while0-vs-scope-block
//  why this ??
#define ECHO(s) \
    do{ \
        printf("1:%s\n",s); \
        printf("2:%s\n",s); \
    } while(0)

/**
 *
 * https://stackoverflow.com/questions/19765674/why-do-multi-line-macros-have-backslashes-at-the-end-of-each-line
 *
 * backslashes in macro : It's a line continuation character.
 *
 */
GENERIC_MAX(float)


int main() {
    printf("max(2,3):%d\n", MAX(2, 3));
    PRINT_INT(9/2);


    // valid
    int MK_ID(p)=10;
    printf("ip:%d \n", MK_ID(p));

    int MK_ID(1)=9;
    printf("i1:%d \n", MK_ID(1));

    float f = float_max(2.0f,1.0f);
    printf("f:%f \n", f);

    ECHO("echo this");

    return 0;
}
