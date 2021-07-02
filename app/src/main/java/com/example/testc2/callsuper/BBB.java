package com.example.testc2.callsuper;

import android.util.Log;

public class BBB extends AAA {

    @Override
    public void call1() {
        try {
            super.call1();
            Log.d("callsuper","BBB:call1");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d("callsuper","e:"+ Log.getStackTraceString(e));
        }
    }
}
