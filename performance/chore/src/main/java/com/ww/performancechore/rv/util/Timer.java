package com.ww.performancechore.rv.util;

import android.util.Log;

public class Timer {
    long start;

    public Timer() {
        start = System.currentTimeMillis();
    }

    public void end(String tag,String c){
        Log.d(tag,"elapse:"+(System.currentTimeMillis() - start)+" "+c);
    }
}
