package com.example.testc2.basics.threads;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class DemoReentrantLock {


    private final AtomicBoolean locked = new AtomicBoolean();
    private Queue<Thread> waiters = new ConcurrentLinkedQueue<>();


    public void lock() {
        Thread current = Thread.currentThread();
        waiters.add(current);
        final String thread = current.getName();
        Log.d("ffff","lock -- start:"+thread);
        while (
                // 当前线程没有排在最上面
                waiters.peek() != current ||
                        //
                        //   AtomicBoolean  ---> true已经lock      ---> park
                        //   AtomicBoolean  ---> false 未lock      ---> to remove ....
                        //
                        !locked.compareAndSet(false, true)
        ) {
            Log.d("ffff","lock -- park:"+thread);
            //  休眠
            LockSupport.park(this);
        }
        waiters.remove();
        Log.d("ffff","lock -- passed:"+thread);
    }


    public void unlock() {
        Thread current = Thread.currentThread();
        final String thread = current.getName();
        Log.d("ffff","lock -- unlock:"+thread);
        locked.set(false);

        //  -------> 唤醒 park的线程
        LockSupport.unpark(waiters.peek());

    }


}
