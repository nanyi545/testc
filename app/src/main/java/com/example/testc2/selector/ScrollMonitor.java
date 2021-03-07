package com.example.testc2.selector;

import android.util.Log;

public class ScrollMonitor {

    public static final int MONITOR_IDLE = 0;
    public static final int MONITOR_STARTED = 1;
    public static final int MONITOR_COMPLETED = 2;


    int monitorState = MONITOR_IDLE;

    public void setMonitorState(int monitorState) {
        this.monitorState = monitorState;
    }


    public boolean isStarted() {
        return monitorState==MONITOR_STARTED;
    }




    int max;
    int current;

    public void setTotal(int max){
        this.max = max;
        this.current = 0;
        setMonitorState(MONITOR_STARTED);
    }

    public void onStep(int delta){
        current+=delta;
        float p = getProgress();
        Log.d("gaga","current:"+current+"  max:"+max+"   p:"+p);
        if(p > 0.99f){
            Log.d("gaga","on compete");
            setMonitorState(MONITOR_COMPLETED);
        }
    }


    public float getProgress(){
        return (current+0.0f)/(max+0.0f);
    }


    float MAX_RATIO = 1.28f;
    float MIN_RATIO = 1.0f;

    public float getMIN_RATIO() {
        return MIN_RATIO;
    }

    public float getMAX_RATIO() {
        return MAX_RATIO;
    }

    public float getExpandRatio(){
        float p = getProgress();
        float compliment = 1 - p;
        return p*MAX_RATIO + compliment*MIN_RATIO;
    }

    public float getContractRatio(){
        float p = getProgress();
        float compliment = 1 - p;
        return p*MIN_RATIO + compliment*MAX_RATIO;
    }

}
