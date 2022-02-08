//
// Created by wei wang on 2022-02-08.
//
/**
 *
 * gcc test_arr.c -o arr1
 *
     * 基础篇（四）:
     * https://cloud.tencent.com/developer/article/1450932?from=article.detail.1450940
     *
     * 数组使用小结

声明数组时，数组长度必须使用常量指定
数组应当先初始化再使用
数组的下标（序号）是从0开始的
访问数组时必须做边界检查。例如数组a的长度为5，则使用a[5]访问是错误的。a[5]表示的是数组的第6个元素，访问超出数组长度的元素会导致程序异常退出。
 如果数组长度是n，则当a[i]访问时，应当保证i<5



 在C语言中，虽说字符串是用字符数组来表示的，但是字符串和普通字符数组仍然是不同的，这两者的区别可以简单总结为如下三点

C语言字符串规定，结尾必须包含一个特殊字符'\0'，我们查询一下ASCII表可知，该字符属于控制字符，即无法打印显示出来的字符，它在ASCII表中的编号是0，即表中的第一个字符NUL。
字符串的实际长度（即字符的个数）比字符数组的长度小1。
声明的同时，数组只能使用花括号初始化，而字符串可以使用双引号括起来的字面量初始化。


字符串的常用函数
C语言虽然是手动挡的，但也为我们提供了一些不太完美的标准库函数，虽然这些函数多多少少都存在一些坑，但也聊胜于无，总比我们事事躬亲要强许多。
 要想使用字符串库函数，需要包含string.h头文件。



 */
#include <stdio.h>
#include <string.h>


void test_arr_p1() {

//    C语言数组在使用前应当初始化，否则数组中的数据是不确定的，由此会造成一些不可预知的问题。

    // 声明的同时，使用字面量初始化。即大括号初始化
    int arr[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    // 可以只指定部分元素的值，剩下的元素将自动使用0值初始化
    int arr1[10] = {0, 1, 2, 3, 4};   //数组元素：0，1，2，3，4，0，0，0，0，0

    // 使用大括号初始化时，中括号中的长度可以省略，编译器将按照实际的个数来确定数组长度
    int arr2[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    // 不需要指定每个元素具体值，仅做零值初始化时，可以使用如下写法
    int arr3[10] = {0};     // 数组的每个元素都会被初始化为0

    // 计算数组长度。数组总内存大小/每个元素内存大小
    int len = sizeof(arr) / sizeof(int);
    printf("arr length:%d \n", len);


    /**
     *    // 不规范的使用方式
   str[0]='a';
   str[1]='b';
   str[2]='c';
     */
}


void test_char() {
    char s = 'a';
    char s1 = 97;

    // 可以看到，s和s1打印的结果完全相同
    printf("%c \n", s);
    printf("%c \n", s1);
    // 这也是a
    int i = 97;
    printf("%c \n", i);
    printf("%c \n", i - 32);  //小写转大写

    // 以整数形式打印字符`a`
    printf("%d \n", s);  // -->97


//    声明并初始化字符串
    //1. 与普通数组相同，用花括号初始化
    char str1[30] = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
    char str2[20] = {"helloworld"};    //字符数组的特殊方式
    //2. 字符数组特有的方式。使用英文双引号括起来的字符串字面量初始化
    char str3[20] = "hello world";
    //3. 省略数组长度
    char str4[] = {"hello world"};
    //4. 省略数组长度，并使用字符串字面量初始化
    char str5[] = "hello world";
    // 在C语言中声明字符串，推荐以上第4种方式，它具有简洁且能避免出错的优点。
    printf("%s \n", str5);

    // 动态计算str数组的长度
    printf("char array size is %d \n", (int) (sizeof(str5) /
                                              sizeof(char)));  // why cast?? warning: format specifies type 'int' but the argument has type 'unsigned long'
    // 获取字符串的长度
    int len = strlen(str5);
    printf("string size is %d \n", len);

    // strcmp的返回值等于0时，表示两个字符串内容相同，否则不同
    if (strcmp(str1, str2) == 0) {
        printf("str1 == str2\n");
    } else {
        printf("str1 != str2\n");
    }


    // strncpy  还可使用该函数为字符数组进行初始化
    char str6[100];
    // 将字符串复制到指定的字符数组中，并自动复制结束符。第一个参数就是目的地
    // 第三个参数需指定复制的长度，这里指定目标数组的大小，表示如果超过这个长度则以这个长度为止
    //
    // 暗坑
    //strncpy函数存在一个问题，如果被复制的字符串长度太长，超过了目的数组的长度，则将目的数组填充满为止，但是这种情况下就不会添加\0结束符，
    // 导致存在不可预知的问题。
    strncpy(str6, "Greetings from C", sizeof(str1));
    strncpy(str6 + strlen(str6), "haha", sizeof(str1));   // concat ...
    printf("str6=%s\n", str6);


    char str7[100] = "hello";
    // 将第二个参数的内容追加到第一个参数的后面，相当于将两者拼接
    // 第三个参数为拷贝的长度，类似strncpy，
    // 这里计算数组的总长度减去字符串的长度，求得str7剩余空间的长度
    strncat(str7, " world!!!", sizeof(str7) / sizeof(char) - strlen(str7));
    printf("str7=%s\n", str7);



}


int main(void) {
    test_arr_p1();
    test_char();
}