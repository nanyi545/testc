package com.example.testc2.basics.classes;


/**
 *
 * 第03讲：字节码层面分析 class 类文件结构
 *
 *
 * 如果从纵观的角度来看 class 文件，class 文件里只有两种数据结构：无符号数和表。
 *
 * 无符号数：属于基本的数据类型，以 u1、u2、u4、u8 来分别代表 1 个字节、2 个字节、4 个字节和 8 个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者字符串（UTF-8 编码）。
 * 表：表是由多个无符号数或者其他表作为数据项构成的复合数据类型，class文件中所有的表都以“_info”结尾。其实，整个 Class 文件本质上就是一张表。
 *
 *
 *class 文件结构 /
 *
 * magic number / version / constant pool /.....
 *
 * string ---> 长度由啥决定
 *
 * 接下来再看 CONSTANT_Utf8_info 表具体结构如下：
 * table CONSTANT_utf8_info {
 *     u1  tag;
 *     u2  length;
 *     u1[] bytes;
 * }
 *
 *
 *
 *
 *
 */
public class Class3 {
}
