//
// Created by wei wang on 2022-02-10.
//

/**
 *
     * 高级篇（九）
     * https://cloud.tencent.com/developer/article/1466756?from=article.detail.1460518
     *
gcc struct_test.c -o struc

 在使用时，需将struct + 标签名合起来看做一种新的类型，然后使用我们熟知的数据类型 + 变量名的格式



结构体与指针

结构体与数组很像，本质上就是指的一片特定的连续的内存空间，结构体成员就在这边内存空间中按顺序分布。
 那么所谓结构体指针，也就是指向该结构体的指针，结合结构体内存分布知识可知，这个指针实际上就是保存了结构体空间的初始地址。


实际上在C语言中，结构体声明通常是和另一关键字typedef结合起来使用的。

 -------------
 结构体总结

在声明结构体变量的时候，编译器就为其分配内存空间
结构体在内存中的分布，是一片连续的内存空间
结构体指针保存的是结构体在内存空间的起始地址
结构体的总内存大小并不一定等于其全部成员变量内存大小之和，当存在内存对齐时，可能会多占用一些额外的空间
结构体变量使用.访问成员，结构体指针使用->访问成员
声明结构体时，建议结合typedef关键字创建别名
结构体可以嵌套使用，即将一个结构体作为另一个结构体的成员

*
 *
*/

#include <stdio.h>

// 声明一个结构体
struct student
{
    char *name;
    int age;
};


// 结构体类型做函数参数
void printStudent(struct student stu){
    printf("学生信息：%s,%d\n",stu.name,stu.age);
}



// 使用typedef时，省略结构体标签名
typedef struct{
    int age;
    char *name;
} man;

void printMan(man m1){
    printf("man信息：%s,%d\n",m1.name,m1.age);
}



int main() {

    // 声明结构体变量:stu
    // 声明同时对所有成员变量做零值初始化
    struct student stu={0};
    // 为结构体中的成员赋值
    stu.name = "zhangsan";
    stu.age = 19;
    // 访问结构体中各个成员变量的内容
    printStudent(stu);


    //缺省的顺序初始化
    struct student stu2={"lisu",19};
    printStudent(stu2);

    // 声明一个结构体指针变量，并指向一个结构体
    struct student *p_stu = &stu;

    man m1 = {0};
    m1.name = "ww";
    m1.age = 19;
    printMan(m1);

    return 0;
}
