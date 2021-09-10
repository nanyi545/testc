package com.ww.performancechore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application {

    static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Activity a;

    public static void setA(Activity a) {
        App.a = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FpsMonitor.init(BuildConfig.DEBUG);

        DisasterTolerance.getInstance().catchLooperException(Looper.getMainLooper(), new DisasterTolerance.ExceptionFilter() {
            @Override
            public boolean filterException(Throwable throwable) {
                if (throwable != null) {
                    Log.d("gggg","error caught:"+Log.getStackTraceString(throwable));
                    return true;
                }
                return false;
            }
        });

        LeakTest.register(this);
        LeakTest.testSoft();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
