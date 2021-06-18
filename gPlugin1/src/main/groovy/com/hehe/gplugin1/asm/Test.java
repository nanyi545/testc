package com.hehe.gplugin1.asm;


/**
 *
 * https://zhuanlan.zhihu.com/p/94498015
 *
 *
 *
 * 下面我们通过一个简单的实例来进行说明。下面是我们编写的一个简单的java文件，只是简单的函数调用.
 *
 *
 *
 * 使用javac -g Test.java编译为class文件，然后通过 javap -verbose Test.class 命令查看class文件格式。
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class Test {
    private int num1 = 1;
    public static int NUM1 = 100;
    public int func(int a,int b){
        return add(a,b);
    }
    public int add(int a,int b) {
        return a+b+num1;
    }
    public int sub(int a, int b) {
        return a-b-NUM1;
    }
}