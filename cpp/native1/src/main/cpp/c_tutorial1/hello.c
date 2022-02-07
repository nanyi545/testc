//
// Created by wei wang on 2022-02-07.
//

/**
 *
 *
 *c基础篇（2）
 *  https://cloud.tencent.com/developer/article/1450937
 *
 *
 *
 *  on mac :
 *
 *  gcc hello.c   --> a.out
 *  ./a.out       --> prints "hello world"
 *
 *
 *  以上就是最简单的gcc编译命令，它每次都会默认生成一个a.exe程序，如果我们想指定生成的程序名字，只需要加上一个参数-o指定即可，o表示out，用于指定生成的程序名，如下
 *  gcc hello.c -o hehe
 *  ./hehe
 *
 *
 * 有时候我们写的程序有语法错误，我们希望编译的时候编译器能给出详细的提示信息，这时候就可以加上另一个参数-Wall，让编译器在编译器时输出更多更详细的的信息，建议每次编译都加上这个参数，这样有什么错误也好查
 *
 * gcc hello.c -Wall -o hello
 *
 *

%d 有符号十进制整数
%f 浮点数
%s 字符串
%c 单个字符
%x 十六进制整数


 注意，声明变量后没有初始化就使用，会造成一些不可预知的结果，因为未初始化的变量可能会具有一些随机值，而且这不是良好的C语言编程习惯，应当被批判。
 声明的变量没有预初始化为零值，这是C语言的一个缺陷！推荐的良好的编程范式，是在声明的同时对变量进行零值初始化。

  // 定义一个宏 PI
 #define PI 3.14

它并不是我们所说的那种真正意义上的常量，但它的效果等同于常量，而且在某种时候这种方式比使用const关键字定义常量性能更好，
这也是为什么许多C语言高手都喜欢使用宏的一个原因。





     * c基础篇（三）:
     * https://cloud.tencent.com/developer/article/1450940?from=article.detail.1450937

 上一节已经讲过，由于C语言中，整型的实际长度和范围不固定的问题，会导致C语言存跨平台移植的兼容问题，因此，C99标准中引入了stdint.h头文件，有效的解决了该问题。

 ??? android中 可以直接用 ???

 这里一定会有朋友质疑，为什么32位的系统下，还能表示并使用int64这种64位的整型？这当然就是stdint.h库给我们带来的便利了，简单说一下原理，如果当前平台的是32位的，那么经过组合，我们可以使用两个32位拼起来，不就能表示64位了吗？
 同理，即使是8位的CPU，经过这种拼合思路，照样能表示64位！





 打开cmd命令行，使用gcc命令生成汇编源码，这里学习一个新的gcc参数-S
gcc -S hello.c





 if后面的条件表达式中存在陷阱，在C语言中没有布尔类型，使用0和非0来表示false和true。
 因此很多人会想当然的以为0是false，大于0就是true，实际上，-1也是true，要注意，是一切非0值，包括小数也是true。



 *
 *
 *
 *
 *
 */

#include <stdio.h>
#define PI 3.14
//#include <stdint.h>

int main(void) {
    printf("hello world!\n");
    printf("%f\n",PI);

    // 使用stdint.h中定义的类型表示整数
    int8_t a = 0;
    int16_t b = 0;
    int32_t c = 0;
    int64_t d = 0;
    printf("a:%d\n",a);
    printf("a++:%d\n",(a++));
    printf("++a:%d\n",(++a));



}
