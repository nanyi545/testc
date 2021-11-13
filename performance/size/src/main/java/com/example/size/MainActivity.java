package com.example.size;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * AppCompatActivity /
 *     implementation 'androidx.appcompat:appcompat:1.2.0'
 *     implementation 'com.google.android.material:material:1.2.1'
 *     implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
 *     ------> 80mb
 *
 *
 *
 *
 * activity --->
 * proguard   61-66mb
 * no-proguard   61-66mb
 *
 *
 *   adb at ---->   /media/ww/LINX2/androidsdk/platform-tools
 *
 * adb shell dumpsys meminfo com.example.size
 *
 *
 * developer.android.com/training/articles/memory.html
 * android-developers.blogspot.com.es/2014/01/process-stats-understanding-how-your.html
 *https://stackoverflow.com/questions/2298208/how-do-i-discover-memory-usage-of-my-application-in-android/2299813#2299813
 *
 *
 */

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,MainActivity.class));
//            }
//        });
//    }
//}


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
    }
}