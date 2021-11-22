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
    private static int recorderSize = 30;
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
//                    if( times[i] > UPPER_LIMIT ){
//                        sb.append(times[i]);
//                        sb.append("|");
//                    }
                    sb.append(times[i]);
                    sb.append("|");
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
                    printCounts();
                    printRecorder();
                }

            }
        };
        c.postFrameCallback(cb);
    }


    /**

     2021-11-22 23:13:35.429 25138-25138/com.ww.performancechore D/fpsfps: 17|16|17|16|18|16|17|16|16|17|17|17|16|17|16|17|16|17|17|16|
     2021-11-22 23:13:35.429 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:35.762 25138-25138/com.ww.performancechore D/fpsfps: 17|17|17|16|17|16|17|16|17|17|17|16|17|17|17|16|16|17|17|17|
     2021-11-22 23:13:35.762 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:36.095 25138-25138/com.ww.performancechore D/fpsfps: 16|17|17|16|17|16|17|17|17|16|17|16|17|17|16|17|16|17|17|16|
     2021-11-22 23:13:36.095 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:36.428 25138-25138/com.ww.performancechore D/fpsfps: 17|16|19|15|16|17|18|15|17|17|16|17|17|16|17|16|17|17|17|16|
     2021-11-22 23:13:36.428 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:36.480 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{564c744 VFE...... ......I. 0,-305-900,31 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.480 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{564c744 VFE...... ......ID 0,2312-900,2648 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.515 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{2e63f12 VFE...... ......I. 0,-320-900,16 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.515 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{2e63f12 VFE...... ......ID 0,2226-900,2562 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.583 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{c682a10 VFE...... ......I. 0,-307-900,29 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.583 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{c682a10 VFE...... ......ID 0,2157-900,2493 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.615 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{da13fbe VFE...... ......I. 0,-334-900,2 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.616 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{da13fbe VFE...... ......ID 0,2192-900,2528 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.666 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{33da39c VFE...... ......I. 0,-40-900,296 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.667 25138-25138/com.ww.performancechore D/VG2: on onLayout:1  this:com.ww.performancechore.recy_scroll.VG2{33da39c VFE...... ......ID 0,2155-900,2491 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.716 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{e956691 VFE...... ......I. 0,-318-900,18 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.716 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{e956691 VFE...... ......ID 0,2194-900,2530 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.761 25138-25138/com.ww.performancechore D/fpsfps: 17|19|18|15|20|14|16|15|17|16|16|17|17|17|16|17|17|16|17|17|
     2021-11-22 23:13:36.761 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:36.799 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{26fb6e7 VFE...... ......I. 0,-195-900,141 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.799 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{26fb6e7 VFE...... ......ID 0,2143-900,2479 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.915 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{31fc3ad VFE...... ......I. 0,-288-900,48 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:36.916 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{31fc3ad VFE...... ......ID 0,2126-900,2462 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:37.094 25138-25138/com.ww.performancechore D/fpsfps: 16|17|17|17|16|16|17|17|16|17|17|17|16|17|17|16|17|17|16|17|
     2021-11-22 23:13:37.094 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:37.115 25138-25138/com.ww.performancechore D/VG2: on measure time:1  this:com.ww.performancechore.recy_scroll.VG2{42bd663 VFE...... ......I. 0,-300-900,36 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:37.115 25138-25138/com.ww.performancechore D/VG2: on onLayout:0  this:com.ww.performancechore.recy_scroll.VG2{42bd663 VFE...... ......ID 0,2140-900,2476 #7f080107 app:id/rv_item_holder}
     2021-11-22 23:13:37.427 25138-25138/com.ww.performancechore D/fpsfps: 16|17|16|17|17|16|17|17|17|16|17|16|17|17|17|16|17|16|17|17|
     2021-11-22 23:13:37.427 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:37.760 25138-25138/com.ww.performancechore D/fpsfps: 17|16|17|16|17|16|18|16|17|16|17|17|17|16|16|17|17|17|16|17|
     2021-11-22 23:13:37.760 25138-25138/com.ww.performancechore D/fpsfps: average fps:62
     2021-11-22 23:13:38.215 25138-25138/com.ww.performancechore D/NewGridLayoutManager: onLayoutChildren 367
     2021-11-22 23:13:38.443 25138-25138/com.ww.performancechore D/fpsfps: 16|17|17|16|17|376|6|17|17|17|17|16|17|16|18|16|17|16|17|16|
     2021-11-22 23:13:38.443 25138-25138/com.ww.performancechore D/fpsfps: average fps:35

     */
}
