package com.example.size;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        FpsMonitor.init(true);
    }
}


/**
 * bitmap  pre 8.0 on heap
 *         post8.0 on
 *
 * https://stackoverflow.com/questions/48091403/how-does-bitmap-allocation-work-on-oreo-and-how-to-investigate-their-memory
 *
 *
 *
 *
 *
 **/