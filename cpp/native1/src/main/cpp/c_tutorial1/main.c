//
// Created by wei wang on 2022-02-09.
//
/**
     * 进阶篇（七）
     * https://cloud.tencent.com/developer/article/1450925?from=article.detail.1450928

 *
 * gcc t1.c main.c -o main
 *

这里有几点需要注意

头文件和.c源文件放到一个文件夹下
我们自己本地的头文件，在包含时应当写英文双引号，而不是尖括号

有了头文件以后，我们的声明都可以放到头文件中，然后在源码文件的顶部去包含它。这就是将声明和实现分离，声明单独放一个文件，实现放在源码文件中。
 这种开发模式，就是模块化开发，也被人称为面向接口的开发。

试想一下，在多人开发之前，大家只要协商好头文件，后面就只需要对照着头文件去写代码，省了很多事。
 开发完成后，将源代码编译，这时候头文件就相当于一份功能说明书，可以很方便的将二进制和头文件一同发布。

-----
 首先回答第一个问题，头文件实际上并不是什么特殊的东西，它仅是一个普通的文本文件，它也可以是任意后缀名的文本文件。
 例如，我们将calculate.h文件改为calculate.txt，包含时使用#include "calculate.txt"

-----
最后说一下包含头文件时<>和""的区别.实际上两者的区别仅仅是参照物的区别，更简单的说就是路径的区别，和是不是标准库头文件或自定义头文件没有关联。
这一点很重要，特别是在自己编写或修改开源库构建脚本，编译大型C语言工程时。


 I am wondering how compilers on Mac OS X, Windows and Linux know where to find the C header files:
https://stackoverflow.com/questions/1899373/how-do-compilers-know-where-to-find-include-stdio-h



----------------------------------------------------

预处理

所谓预处理，就是在办正事之前做一点准备工作。预处理指令都是以#号开头的，这一点很好辨认。

学习预处理最好的方法，就是将C语言的预处理-编译-汇编-链接四个阶段拆开，分步进行，这时候正好体现出使用gcc命令行学习C语言的优势。

使用gcc进行预处理，这里加-E参数预处理，-o指定生成的文件名
gcc -E main.c -o main.i
gcc -E t1.c -o t1.i
执行命令后，生成了预处理之后的源文件main.i

--->这个文件很简单，只是将calculate.h中的声明都复制到了当前的源文件中来。
 到现在就很容易理解预处理指令#include了吧，就是在正式编译代码之前，帮我们把头文件中的声明拷贝到源文件中，这说明C语言中，那些声明最终还是必须得写到源文件中的。
 这件事被称为声明展开



编译 -S 生成汇编代码
 gcc -S main.i -o main.s
 gcc -S t1.c -o t1.s

汇编 -c 生成机器码，亦称为目标文件。
 gcc -c main.s -o main.o
 gcc -c t1.c -o t1.o

链接  生成可执行程序main.exe
 gcc main.o t1.o -o main

----------------------------------------------------

 条件编译
包含#if、#ifdef、ifndef、#endif，使预处理器可以根据条件确定是否将一段文本包含

------------------------
宏定义
使用#define指令定义一个宏
使用#undef指令删除一个宏

之前说用#define来定义常量，实际上就是利用宏的预处理，进行字符串替换而已。现在我们就使用gcc命令要验证
------------------------

关于预编译指令，需要记住几点

#开头的预处理指令必须顶格写，前面不要有空格
记住三大类预处理指令的特点，#include指令是声明展开，宏定义是文本替换，条件编译是直接删除代码。

----------------------------------------------------

 *
 */
#include <stdio.h>
#include "main1.h"

int main() {
    printf("1+2=%d\n", add(1, 2));
    printf("18-9=%d\n", sub(18, 9));
    return 0;
}
