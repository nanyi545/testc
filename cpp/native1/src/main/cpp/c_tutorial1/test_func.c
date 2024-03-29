//
// Created by wei wang on 2022-02-08.
//
/**
 *
 * gcc test_func.c -o func
 *
 *
     * 基础篇（五）
     * https://cloud.tencent.com/developer/article/1450927?from=article.detail.1450932
     *
     * 进阶篇（六）
     * https://cloud.tencent.com/developer/article/1450928?from=article.detail.1450927

 * 在C语言中，出现了两个概念，声明和定义。
 *
 *
 * 函数的作用域
这里简单说说作用域的问题，在函数中声明的变量被称为局部变量，只在当前函数体的范围内可被访问，离开该函数体的范围就不可被访问了。
 而在函数外的声明的变量，称为全局变量，全局变量在整个程序中都可被访问，也就是所有的源文件，包括.c和.h文件中都可被访问。
 具体细节，在后续篇幅中会详细说明

 * 简单函数的小结
函数不能返回数组，因此函数的返回值不能是数组类型。
函数没有返回值时，也应当写上void明确返回值类型
C语言没有函数重载概念。这意味着C中相同作用域内的函数绝不能同名，哪怕返回值和形参都不同。C语言还没有命名空间的概念，这两者综合一起就是C语言最大缺陷之一。
C语言函数的声明与定义是分离的，但是在任何时候都应当先声明再实现。这里声明是指显式声明。意即，当自定义的函数被定义在main函数之前时，它同时包含了声明与定义。

--------

 C语言中的所谓指针，简单理解其实就是数据在内存中的地址，有了这个地址，我们就能找到共享的资源。
 我们需要C语言，需要指针，就是为了这极致的性能和效率，这是除了C/C++外的其他高级语言所不具备的。
 即使是号称继承自C语言的Go语言，它的指针也只是个半吊子货，远没有C指针强大。

严格的说，C语言中的指针类型，是指保存的值是内存地址的变量。

 --------


 简单指针的小结

声明指针变量时，星号应紧靠指针变量，并在同时初始化为NULL
指针变量的值应当是一个地址
声明指针类型时的星号和解引用时的星号意义完全不同，注意区分，不要混淆。声明指针类型时的星号，代表指针类型，解引用时的星号是间接寻址运算符

 *
 *
 */
#include <stdio.h>
#include <string.h>

// 在main函数之前先声明
void printError();


/*
 *
    当数组作为形参时，不能对其使用sizeof运算符
   flags: 值为0时，全部转小写，非0时，转大写

   这里需特别注意，在C语言中，凡是数组做函数参数时，都是引用传递，或者说是指针传递。
   其他基本数据类型做函数参数，包括结构体，都是值传递。
   一定要明确，在C语言中，数组不存在值传递，这也是为什么不能对做函数参数的数组使用sizeof运算的原因所在，因为它会自动退化为指针。

*/
void convstr(char ch[], int flags) {
    for (int i = 0; i < strlen(ch); i++) {
        if (ch[i] >= 97 && ch[i] <= 122) {
            if (flags) ch[i] = ch[i] - 32;
        } else if (ch[i] >= 65 && ch[i] <= 90) {
            if (!flags) ch[i] = ch[i] + 32;
        }
    }
}


/**
 * 实现简单正则表达式匹配器
下面的实例来自经典图书《代码之美》，这段程序使用简单的30来行代码，实现了一个简单正则表达式匹配器，其代码之简洁优雅，可为楷模，也充分展示出了C程序的简洁高效特点。

 https://tool.oschina.net/uploads/apidocs/jquery/regexp.html
https://www.liaoxuefeng.com/wiki/1016959663602400/1017639890281664

 */
int match(char *regexp, char *text);
int matchhere(char *regexp, char *text);
int matchstar(int c, char *regexp, char *text);


void testCall1() {
    char str[] = "Hello,ALICE";
    convstr(str, 0);
    printf("%s\n", str);
    convstr(str, 1);
    printf("%s\n", str);


    char *str1 = "+8613277880066";
    // 检测字符串str1是否以"+86"开头
    printf("%d\n", match("^+86", str1));  // 1
    // 检测字符串str1尾部是否包含"66"子串
    printf("%d\n", match("66$", str1));  // 1
    // 字符串str1中是否包含子串"132"
    printf("%d\n", match("132", str1));   // 1
    // 是否包含3xxx2样式的字符串，xxx可以是任意多个或者0个字符
    printf("%d\n", match("3*2", str1));   // 1
    // 是否包含3x2样式的子串，x是单个任意字符，这里不包含
    printf("%d\n", match("3.2", str1));   // 0

}



void testPointer() {
    int num = 10;

    //声明指针类型变量 , //指针应在声明同时初始化为NULL
    // 如果直接访问未初始化的指针，会造成无法预知的后果，这种指针也被称为野指针！
    int *ptr = NULL;

    //给指针类型变量赋值
    //注意，ptr才是指针变量，而不是*ptr，切记！
    ptr = &num;
    /**
     * 上例中，int *ptr;，变量ptr就是指针变量，与普通变量的区别就是多了一个星号。
     *
     * 实际上如果换种写法大家可能一眼就理解了：int* ptr;，将int和*紧挨，把它们看成一个整体，
     * 那这个整体就是指针类型，这样就复合我们最初学习的声明变量的格式了：【数据类型】【变量名】。
     *
     *
     * 实际上这样写是可以的，但是千万不要这样写，请将星号和变量紧挨一起，不要和类型挨在一起，虽然这很反直觉，但这确实是C语言的潜规则，  ??? Why ????
     * 当大家都这样写的时候，最好还是遵守规范。这样写并不是心血来潮，确实能避免犯一些错误。
     *
     * 这里还要学习两个运算符
     取地址运算符 &
     顾名思义，就是可以获得一个变量在内存中的地址。内存地址是一个4字节的数值，通常用16进制显示,如上例中的22fe44，它表示变量num的值储存在内存编号为22fe44的空间中。

     间接寻址运算符 *
     以上第10行代码中的星号是间接寻址运算符，它只能对指针变量使用，表示将该指针变量保存的地址对应的内存中的值取出来。
     这样说比较绕，换个说法，
     如果直接将一个内存地址对应的内存中保存的值取出来，这就叫直接寻址，
     如果是对保存地址的变量使用，这就是间接寻址。使用间接寻址运算符的过程被称为解引用。 ??

     */

    printf("ptr=%x\n", ptr);
    printf("num=%d\n", *ptr);
    (*ptr)++;  // use pointer change value ...
    printf("num=%d\n", num);


    int n = 7;
    int l = 10;
    // 指针常量仅指向唯一的内存地址，一旦被初始化后，就不能再指向其他地址。简单说就是指针本身是常量。
    //声明并初始化指针常量
    int *const pp1 = &n;
//    pp1 = &l; // 错误，无法编译！指针常量不能再指向其他地址


    // 常量指针的意思是说指针所指向的内容是个常量。
    // 既然内容是个常量，那就不能使用解引用符去修改指向的内容。但指针自己本身却是个变量，因此它仍然可以再次指向其他的内容。
    //声明常量指针
    const int *pp2 = &n;
//    *pp2 = 0; // 错误，无法编译！不能修改所指向的内容
    pp2 = &l; //它可以再指向其他地址

}


int main() {
    printError();
    testCall1();
    testPointer();
    return 0;
}

// 在main函数之后再定义
void printError() {
    printf("this is error!\n");
}


// 在text中查找正则表达式regexp
int match(char *regexp, char *text) {
    if (regexp[0] == '^') {
        return matchhere(regexp + 1, text);
    }
    do {  //即使字符串为空也必须检查
        if (matchhere(regexp, text)) return 1;
    } while (*text++ != '\0'); // *text++的用法，这里自增运算符++的优先级高于解引用运算符*，因此实际上的运算顺序是*(text++)
    return 0;
}

// 在text开头查找regexp
int matchhere(char *regexp, char *text) {
    if (regexp[0] == '\0') return 1;
    if (regexp[0] == '*') {
        return matchstar(regexp[0], regexp + 2, text);
    }

    if (regexp[0] == '$' && regexp[1] == '\0') {
        return *text == '\0';
    }

    if (*text != '\0' && (regexp[0] == '.' || regexp[0] == *text)) {
        return matchhere(regexp + 1, text + 1);
    }
    return 0;
}

int matchstar(int c, char *regexp, char *text) {
    do {   // 通配符* 匹配零个或多个实例
        if (matchhere(regexp, text)) return 1;
    } while (*text != '\0' && (*text++ == c || c == '.'));
    return 0;
}


