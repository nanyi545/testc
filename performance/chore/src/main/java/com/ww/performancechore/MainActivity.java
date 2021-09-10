package com.ww.performancechore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import leakcanary.AppWatcher;

public class MainActivity extends AppCompatActivity {





    Button tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        App.getInstance().setA(this);

        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                // this will get caught by disasterTolerance
//                throw new RuntimeException("hehe");



                // this will not get caught by disasterTolerance
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        throw new RuntimeException("hehe");
//                    }
//                }).start();

            }
        });



        Button tv2 = findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class ));

                LeakTest.MemObject o1 = new LeakTest.MemObject();
                list.add(o1);

            }
        });


        Button tv3 = findViewById(R.id.tv3);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWatcher.INSTANCE.getObjectWatcher().expectWeaklyReachable(list,"hehehe");
            }
        });

    }

    static List<LeakTest.MemObject> list = new ArrayList<>() ;



}
