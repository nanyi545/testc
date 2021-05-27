package com.example.testc2.basics.threads;


import java.util.concurrent.PriorityBlockingQueue;

/**
 * 线程池原理
 */
public class ThreadPool {

    public void execute(Runnable task){
        queue.add(task);
    }

    PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue();

    private boolean running = true;

    private void start1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                    Runnable task = queue.poll();
                    task.run();
                }
            }
        }).start();
    }

}
