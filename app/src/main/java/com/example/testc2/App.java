package com.example.testc2;

import android.app.Application;
import android.util.Log;

import java.io.File;

import xcrash.XCrash;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        File f = new File("/sdcard/Download/textc_x");

        //   /storage/self/primary/Android/data/com.example.testc2/cache
        File f = new File(getExternalCacheDir().getAbsolutePath());
        if(!f.exists()){
            f.mkdirs();
        }
        Log.d("hehe","location:"+f.getAbsolutePath()+"  exist:"+f.exists()+"   direct:"+f.isDirectory());

        XCrash.init(this, new XCrash.InitParameters()
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
                .setJavaDumpAllThreadsCountMax(10)
//                .setJavaCallback(callback)
                .setNativeRethrow(true)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
                .setNativeDumpAllThreadsCountMax(10)
//                .setNativeCallback(callback)
                .setAnrRethrow(true)
                .setAnrLogCountMax(10)
//                .setAnrCallback(callback)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogDir(f.getAbsolutePath())
                .setLogFileMaintainDelayMs(1000));

    }

}
