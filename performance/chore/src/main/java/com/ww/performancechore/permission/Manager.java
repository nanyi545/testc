package com.ww.performancechore.permission;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class Manager {

    public static void loadApk(Activity c) {
        //注意：使用运行时权限
        ActivityCompat.requestPermissions(c,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }



}
