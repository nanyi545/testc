package com.example.size;

import android.util.Log;
import android.view.Choreographer;

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
