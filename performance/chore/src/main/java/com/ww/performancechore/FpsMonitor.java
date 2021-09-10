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
 *
 */


public class FpsMonitor {

    static Choreographer c;
    static Choreographer.FrameCallback cb;

    //  FPS < 40
    private static final int UPPER_LIMIT = 25;
    private static final int COUNT = 20;
    private static long times[] = new long[COUNT];
    private static int count = 0;
    private static StringBuilder sb = new StringBuilder();


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
                    Log.d("aaa",sb.toString());
                }
            }

            @Override
            public void doFrame(long frameTimeNanos) {
                long now = System.currentTimeMillis();
                long diff = now - t;
                times[ (count%COUNT) ] = diff;
                c.postFrameCallback(cb);
                t = now;
                count ++ ;
                if(count == COUNT) {
                    count = 0;
                    printCounts();
                }

            }
        };
        c.postFrameCallback(cb);
    }


}
