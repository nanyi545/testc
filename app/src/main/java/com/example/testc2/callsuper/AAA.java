package com.example.testc2.callsuper;

import android.util.Log;

public class AAA {

    public void call1(){
        Log.d("callsuper","AAA:call1");
        if(true){
            throw new NoSuchMethodError("ss");
        }
    }

}
