package com.example.testc2.basics.classes;


/**
 * 第02讲：GC 回收机制与分代回收策略
 *
 *
 * 在 Java 中，有以下几种对象可以作为 GC Root：
 *
 * Java 虚拟机栈（局部变量表）中的引用的对象。
 * 方法区中静态引用指向的对象。
 * 仍处于存活状态中的线程对象。
 * Native 方法中 JNI 引用的对象。
 *
 *
 *
 * Soft references
 *
 * A soft reference is exactly like a weak reference, except that it is less eager to throw away the object to which it refers.
 *
 *
 *
 *
 *
 *
 */
public class Class2 {

}
