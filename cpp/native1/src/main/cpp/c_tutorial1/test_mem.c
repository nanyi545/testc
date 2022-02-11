//
// Created by wei wang on 2022-02-08.
//
/**
 *
 *
gcc test_mem.c -o mem

     * 高级篇（十）
     * https://cloud.tencent.com/developer/article/1473817?from=article.detail.1460518


--------------------------------------------------------
内存四区

栈(stack)用于保存函数中的形参、返回地址、局部变量以及函数运行状态等数据。栈区的数据由编译器自动分配、自动释放，无需程序员去管理和操心。
 当我们调用一个函数时，被称为函数入栈，指的就是为这个函数在栈区中分配内存。

堆(heap)堆内存由程序员手动分配、手动释放，如果不释放，只有当程序运行结束后，操作系统才会去回收这片内存。
 C语言所谓的动态内存管理，指的就是堆内存管理，这也是C语言内存管理的核心内容。

静态全局区又被人称为数据区、静态区。它又可细分为静态区和常量区。主要用来存放全局变量、静态变量以及常量。该区域内存，只有在程序运行结束后才会被操作系统回收。
 被形象的比喻为与整个程序同生共死，也就是说只要程序没有退出，这部分内存数据就一直存在。

代码区用于存放程序编译链接后生成的二进制机器码指令。由操作系统管理，程序员无需关心。


--------------------------------------------------------
 内存分配

C语言内存分配的三种形式

静态/全局内存 静态声明的变量和全局变量都使用这部分内存。在程序开始运行时分配，终止时消失。区别：所有函数都能访问全局变量，静态变量作用域则只局限于定义它的函数内部
自动内存 在函数内声明，函数调用时创建（分配在栈中），作用域局限于该函数内部，函数执行完则释放。
动态内存 内存分配在堆上，用完需手动释放，使用指针来引用分配的内存，作用域局限于引用内存的指针

 ----

     // 使用malloc函数，分配动态内存空间，注意包含stdlib.h
    int *p = (int*)malloc(sizeof(int));



与动态内存管理相关的主要有四个函数



从堆上分配一块指定大小的内存，并返回分配的空间的起始地址，这里是一个void类型指针，如果系统内存不足以分配，则返回NULL。
 该函数不会清空所分配的内存空间中的内容，因此可能分配的空间会包含一些随机数据

------------------------
calloc 原型

void *calloc(size_t _NumOfElements,size_t _SizeOfElements);
第一个参数用来指定元素的个数，第二个参数指定一个元素所占内存大小。malloc函数的参数正好相当于它的两参数的乘积。

int *array = (int*)calloc(10,sizeof(int));
------------------------

 realloc原型

void *realloc(void *_Memory,size_t _NewSize);
它的第一个参数为指向原内存块的指针，第二个参数为重新请求的内存大小。

当我们使用malloc动态分配了一块内存空间，随着数据的增加，内存不够用时，就可以使用realloc调整原来分配的内存大小。

---------

关于free的使用总结

 当使用free函数释放内存后，指向原堆空间的指针并不会被清理或重置，这意味着指向原空间的指针中仍保存着一个不合法的地址，
 如果不小心再次使用了这个指针，就会造成无法预知的问题，因此在使用free释放内存后，还应当将原指针重置为NULL



 -------------------------------------------

 二级指针

所谓二级指针，就是一个指向指针的指针。

我们知道指针变量是用来保存一个普通变量的地址的，那么如果对一个指针变量取地址，并用另一个变量保存指针变量的地址，这种情况是否存在呢？


 -------------------------------------------

 函数指针

在上面的内存四区中提到了代码区，而函数就是一系列指令的集合，因此它也是存放在代码区。既然存放在内存中，那么就会有地址。我们知道数组变量实际上也是一个指针，指向数组的起始地址，结构体指针也是指向第一个成员变量的起始地址，而函数指针亦是指向函数的起始地址。

所谓函数指针，就是一个保存了函数的起始地址的指针变量。

函数指针的声明
声明格式：

【返回值类型】 (*变量名) (【参数类型】)

 // 分别声明四个函数指针变量 f1、f2、f3、f4
int (*f1)(double);
void (*f2)(char*);
double* (*f3)(int,int);
int (*f4)();


 ----
 函数指针的赋值与使用
当一个函数的原型与所声明的函数指针类型匹配，那么就可以将一个函数名赋值给函数指针变量。

-------------------------

 void*指针

前面几次提到通用类型指针void*，它可以指向任意类型，但对于void*指针到底是什么没有做深入的探讨。事实上，只有理解了void*指针，才能真正理解C语言指针的本质，
 才能使用void*指针实现一些奇技淫巧。


 由此我们基本可以推断一个事实，指针用来保存变量的内存地址与变量的类型无关，任何类型指针都可以保存任何一个地址；指针之所以需要类型，只与该指针的解引用有关。

 当我们不确定指针所指向的具体数据类型时，就可以使用void*类型来声明，当我们后续确定了具体类型之后，就可以使用强制类型转换来将void*类型转换为我们需要的具体类型。

接触过Java等具有泛型的面向对象编程语言的人，可能马上就会联想到泛型，是的，C语言没有泛型，但是利用void*指针的特点，我们可以使用一些技巧来模拟泛型编程。



 *
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *String(int len){
    char *s = (char*)malloc(len);
    return s;
}

void printStrings(char **s,int len){
    char **start = s;
    // 使用二级指针来遍历字符串数组
    char *pre = NULL;
    for (;s < start + len;s++){
        printf("%s\n",*s);
        printf("level2 p address:%x\n",s);
        printf("level1 p address:%x\n",*s);
        if(pre==NULL){
            pre = *s;
        } else {
            int diff = (*s - pre);
            printf("level1 p diff:%d\n",diff);
        }
    }
}


// 2d arr demo
void test_d2arr(){
    // total size 3*50
    char songs[3][50]={
            "My love",
            "Just one last dance",
            "As long as you love me",
    };
    printf("s0 %s  s0 size:%lu string length:%lu\n",songs[0], sizeof(songs[0]) / sizeof(char), strlen(songs[0]));
    printf("s1 %s  s1 size:%lu string length:%lu\n",songs[1], sizeof(songs[1]) / sizeof(char), strlen(songs[1]));
    printf("s2 %s  s2 size:%lu string length:%lu\n",songs[2], sizeof(songs[2]) / sizeof(char), strlen(songs[2]));
    printf("2d array length1:%lu\n", sizeof(songs) / sizeof(char) );


    char* songs2[3]={
            "1",
            "22",
            "333",
    };



//    // 一个指针数组的数组名，实际上就是一个二级指针
    char **p = songs2;
    printf("2d array length2:%lu\n", sizeof(p) / sizeof(char*) );
    printf("2d array length2:%lu\n", sizeof(p+1) / sizeof(char*) );
    printf("2d array length2:%lu\n", sizeof(p+2) / sizeof(char*) );
    printf("2d array length2-1:%lu\n", sizeof(songs2) / sizeof(char*) );
    printf("2d array length3:%lu\n", sizeof(*p) / sizeof(char) );
    printf("2d array length3:%lu\n", sizeof(*(p+1)) / sizeof(char) );
    printf("2d array length3:%lu\n", sizeof(*(p+2)) / sizeof(char) );
    printf("2d array length4:%lu\n", sizeof(*p) / sizeof(char*) );
    printf("2d array length4:%lu\n", sizeof(*(p+1)) / sizeof(char*) );
    printf("2d array length4:%lu\n", sizeof(*(p+2)) / sizeof(char*) );
    printf("char pointer size:%lu   char size:%lu \n", sizeof(char*), sizeof(char) );

    printStrings(p,3);


    char* s1 = "123";
    //  pointer like this sizeof can not return Array size .....
    printf("****  size1:%lu  size2:%lu\n", sizeof(s1) , sizeof(*s1) );


}




int count(double val){
    printf("count run\n");
    return 0;
}

void printStr(char *str){
    printf("printStr run\n");
}

double *add(int a,int b){
    printf("add run\n");
    return NULL;
}

int get(){
    printf("get run\n");
    return 0;
}



// 加法函数
int func1(int a,int b){
    return a +b;
}
// 减法函数
int func2(int a, int b){
    return a-b;
}

// 计算器函数。将函数指针做形式参数
void calculate(int a,int b, int(*proc)(int,int)){
    printf("result=%d\n",proc(a,b));
}

// 以上在函数的形参中直接定义函数指针看起来不够简洁优雅，每次都得写一大串，实际上还有更简洁的方式，这就需要借助typedef

// 定义一个函数指针类型，无需起新的别名
typedef int(*proc)(int,int);

// 使用函数指针类型 proc 声明新的函数指针变量 p
void calc(int a,int b, proc p){
    printf("result=%d\n",p(a,b));
}


// ----------- demo of void ---- like generics in JAVA

// 交换两个变量的值
void swap(void* a, void *b, int size){
    // 申请一块指定大小的内存空间做临时中转
    void *p = (void*)malloc(size);

    // 内存拷贝函数，拷贝指定的字节数
    memcpy(p, a, size);
    memcpy(a, b, size);
    memcpy(b, p, size);

    // 释放申请的内存空间
    free(p);
}



int main() {
    //可以看到malloc的函数原型 void *malloc(size_t _Size); 它返回一个void *类型指针，这是一个无类型或者说是通用类型指针，它可以指向任意类型，
    // 因此我们在使用它的返回值时，首先做了强制类型转换。该函数只有一个无符号整数参数，用来传入我们想要申请的内存大小，单位是字节。
    int *p = (int*)malloc(sizeof(int));
    *p=10;
    printf("%d \n",(*p));
    printf("%x \n",(int)p);


    // 上例中我们传入的是一个int类型的大小，通常是4字节。需要特别注意，当使用malloc分配动态内存时，如果失败，它会返回NULL指针，因此使用时需判断。
    char *str = String(100);
    if (str == NULL){
        // 内存分配失败时，返回NULL指针，使用时需先判断分配是否成功
        printf("Not enough memory space!\n");
    }

    strncpy(str,"Hi,use dynamic memory space",100);
    printf("%s\n",str);

    // 手动释放内存
    free(str);
    str=NULL;



    test_d2arr();



    // 声明函数指针并初始化为NULL
    int (*f1)(double) = NULL;
    void (*f2)(char*) = NULL;
    double* (*f3)(int,int) = NULL;
    int (*f4)() = NULL;

    // 为函数指针赋值
    f1 = &count;
    f2 = &printStr;
    f3 = &add;
    f4 = &get;

    // 使用函数指针调用函数
    f1(0.5);
    f2("f2");
    f3(1, 3);
    f4();


    // 算加法，传入加法函数
    calculate(10,5,func1);
    // 算减法，传入减法函数
    calculate(10,5,func2);
    calc(10,5,func2);



    // 传入short型指针
    short x=10,y=80;
    swap(&x, &y,sizeof(short));
    printf("x=%d  y=%d\n",x,y);


    return 0;
}



