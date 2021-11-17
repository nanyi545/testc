package com.ww.performancechore;

import android.util.Log;
import android.view.Choreographer;


/**
 *
 *
 * postSyncBarrier --->
 *
 * Message processing occurs as usual until the message queue encounters the
 * synchronization barrier that has been posted.  When the barrier is encountered,
 * later synchronous messages in the queue are stalled (prevented from being executed)
 * until the barrier is released by calling {@link #removeSyncBarrier} and specifying
 * the token that identifies the synchronization barrier.
 *
 *
 *----------------------------------- choreographer
 *
 *
 *  自定义View时经常用到requestLayout，invalidate等方法，我们调用它们的目的就是告诉系统我们要刷新下界面，但是实际上是他们去刷新界面吗？
 *
 *  下面我们来看下：
 * 我们之前分析过当我调用requestLayout的时候不管你在哪调最后都会传递到ViewRootImp中，由它来统一调用，其实invalidate也是一样。
 *
 *
 * -----
 *
 * @Override
 *     public void requestLayout() {
 *         if (!mHandlingLayoutInLayoutRequest) {
 *             checkThread();
 *             mLayoutRequested = true;
 *             scheduleTraversals();
 *         }
 *     }
 *
 * -----
 *
 *   void invalidate() {
 *         mDirty.set(0, 0, mWidth, mHeight);
 *         if (!mWillDrawSoon) {
 *             scheduleTraversals();
 *         }
 *     }
 *
 *
 * -----
 *
 *    void scheduleTraversals() {
 *        if (!mTraversalScheduled) {
 *            mTraversalScheduled = true;
 *            //添加同步屏障，下面分析源码
 *            mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();   //  barrier token
 *            mChoreographer.postCallback( Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
 *            。。。。
 *        }
 *    }
 *
 * -----
 *
 * final class TraversalRunnable implements Runnable {
 *         @Override
 *         public void run() {
 *             doTraversal();
 *         }
 *     }
 *
 * -----
 *
 * void doTraversal() {
 *         if (mTraversalScheduled) {
 *             mTraversalScheduled = false;
 *             mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);
 *
 *             if (mProfile) {
 *                 Debug.startMethodTracing("ViewAncestor");
 *             }
 *
 *             performTraversals();
 *
 *             if (mProfile) {
 *                 Debug.stopMethodTracing();
 *                 mProfile = false;
 *             }
 *         }
 *     }
 *
 * -----
 *
 * https://juejin.cn/post/6863756420380196877
 *
 *主要有以下逻辑：
 *
 * 1 首先使用mTraversalScheduled字段保证同时间多次更改只会刷新一次，例如TextView连续两次setText()，也只会走一次绘制流程。
 * 2 然后把当前线程的消息队列Queue添加了同步屏障，这样就屏蔽了正常的同步消息，保证VSync到来后立即执行绘制，而不是要等前面的同步消息。后面会具体分析同步屏障和异步消息的代码逻辑。
 * 3 调用了mChoreographer.postCallback()方法，发送一个会在下一帧执行的回调，即在下一个VSync到来时会执行TraversalRunnable-->doTraversal()--->performTraversals()-->绘制流程。
 *
 * Choreographer和Looper一样 是线程单例
 *
 *  接着看看Choreographer构造方法：
 *     private Choreographer(Looper looper, int vsyncSource) {
 *  创建了一个mHandler
 *  VSync事件接收器mDisplayEventReceiver
 *  任务链表数组mCallbackQueues
 *
 *
 *
 *
 *
 */


import java.util.LinkedList;

public class FpsMonitor {

    static Choreographer c;
    static Choreographer.FrameCallback cb;

    //  FPS < 40
    private static final int UPPER_LIMIT = 25;
    private static final int COUNT = 20;
    private static long times[] = new long[COUNT];
    private static int count = 0;
    private static StringBuilder sb = new StringBuilder();


    private static LinkedList<Long> recorder = new LinkedList<>();
    private static int recorderSize = 100;
    private static long recorderTotal = 0;

    private static void add2recorder(long frameTime){
        recorderTotal+=frameTime;
        recorder.addLast(frameTime);
        if(recorder.size()>recorderSize){
            long f = recorder.removeFirst();
            recorderTotal-=f;
        }
    }

    private static void printRecorder(){
        long averageFrameTime = recorderTotal / recorderSize ;
        Log.d("fpsfps","average fps:"+ (1000/averageFrameTime));
    }

    public static void init(boolean debug){
        if(!debug){
            return;
        }
        c = Choreographer.getInstance();
        cb = new Choreographer.FrameCallback() {
            long t = System.currentTimeMillis();
            private void printCounts(){
                sb.setLength(0);
                for (int i=0;i<COUNT;i++){
                    if( times[i] > UPPER_LIMIT ){
                        sb.append(times[i]);
                        sb.append("|");
                    }
                }
                if(sb.length()>1){
                    Log.d("fpsfps",sb.toString());
                }
            }

            @Override
            public void doFrame(long frameTimeNanos) {
                long now = System.currentTimeMillis();
                long diff = now - t;
                times[ (count%COUNT) ] = diff;
                add2recorder(diff);
                c.postFrameCallback(cb);
                t = now;
                count ++ ;
                if(count == COUNT) {
                    count = 0;
//                    printCounts();
                    printRecorder();
                }

            }
        };
        c.postFrameCallback(cb);
    }


}
