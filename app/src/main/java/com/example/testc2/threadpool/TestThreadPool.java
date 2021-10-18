package com.example.testc2.threadpool;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TestThreadPool {

    ExecutorService executor;

    public TestThreadPool() {
        this.executor = Executors.newFixedThreadPool(4);
    }



    public void testNormal(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("aaa","--normal call---");
            }
        });
    }

    /**
     *      Caused by: java.util.concurrent.RejectedExecutionException:
     *
     *      Task com.example.testc2.threadpool.TestThreadPool$2@2b96e0b1 rejected from java.util.concurrent.ThreadPoolExecutor@199c996[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
     *
     */
    public void testAbNormal(){
        try {
            executor.shutdown();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("aaa","--abnormal call---");
                    } catch (Throwable e) {
                        Log.d("aaa","--abnormal call e1---"+Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Throwable e) {
            // is caught here ....
            Log.d("aaa","--abnormal call e2---"+Log.getStackTraceString(e));
        }
    }

}
