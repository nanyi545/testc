package com.example.testc2.basics.threads;


/**
 *   Synchronized缺点
 *
 *
 *  1 无法判断锁状态，不知当前线程是否锁住 还是没有锁住
 *  2 不可中断，如果线程迟迟拿不到一把锁，这把锁被其他线程占用，会出现改线程一直等待
 *  3 Synchronized非公平锁，任何新线程过来 与原先排队等待的线程 都有同样的机会获得锁
 *  4 关键字实现获取锁与释放锁，中间不可控
 *  5 当锁产生竞争时，两个和三个线程争取一把锁，没有获得到，此时改为系统主动分配，这也
 *  6 是重量级锁由来。此时会造成 用户空间切换到内核空间
 *
 *
 * --------------------------------------
 *
 * https://objcoding.com/2018/11/29/cas/
 *
 *
 * volatile : 读写操作对其他线程可见 / i++操作不行
 *
 *
 * 修改volatile变量时会强制将修改后的值刷新的主内存中。
 * 修改volatile变量后会导致其他线程工作内存中对应的变量值失效。因此，再读取该变量值的时候就需要重新从读取主内存中的值。
 *
 * --------------------------------------CAS-------------------------------------
 *
 * CAS是英文单词Compare And Swap的缩写，翻译过来就是比较并替换。
 *
 * CAS机制当中使用了3个基本操作数：
 * 内存地址V
 * 旧的预期值A
 * 要修改的新值B
 *
 * CAS 的思想很简单：三个参数，一个当前内存值 V、旧的预期值 A、即将更新的值 B，当且仅当预期值 A 和内存值 V 相同时，将内存值修改为 B 并返回 true，否则什么都不做，并返回 false。
 *
 *
 * -----------volatile 关键字可以保证变量的可见性，但保证不了原子性
 * -----------synchronized 关键字利用 JVM 字节码层面来实现同步机制
 *
 *
 * -------------------------
 *
 *
 * https://blog.csdn.net/ghsau/article/details/38471987
 *
 *
 * public final int incrementAndGet() {
 *     for (;;) {
 *         int current = get();
 *         int next = current + 1;
 *         if (compareAndSet(current, next))
 *             return next;
 *     }
 * }
 *
 *
 * ---->   UnSafe 实现 compareAndSet
 *
 *
 *
 *--------------------------------------------
 *
 */
public class LockTest {
}
