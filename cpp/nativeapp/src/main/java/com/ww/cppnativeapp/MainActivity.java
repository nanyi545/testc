package com.ww.cppnativeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    static {
        try {
            System.loadLibrary("native1-lib");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d("cppnativeapp","e:"+e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
