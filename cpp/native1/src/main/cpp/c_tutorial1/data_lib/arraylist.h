//
// Created by wei wang on 2022-02-11.
//

#ifndef TESTC2_ARRAYLIST_H
#define TESTC2_ARRAYLIST_H

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

// 默认容量
#define DEFAULT_CAPACITY 8

#define OK 0
#define ERROR -1


typedef int Element;


// 声明动态列表的结构体
typedef struct
{
    Element *container;
    int length;   // 列表长度
    int capacity;  // 底层数组容量
} ArrayList;

// 初始化动态列表
int AL_init(ArrayList *);
// 添加元素
int AL_add(ArrayList*,Element);
// 删除元素
int AL_remove(ArrayList*,int);
// 获取元素
int AL_get(ArrayList*,int,Element*);

// 打印
void AL_print(ArrayList *);

#endif //TESTC2_ARRAYLIST_H
