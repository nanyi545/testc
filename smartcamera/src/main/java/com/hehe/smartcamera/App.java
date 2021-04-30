package com.hehe.smartcamera;

import android.app.Application;

public class App extends Application {

    static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
