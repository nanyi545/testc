package com.ww.performancechore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ww.performancechore.async_inflate.InflatorActivity;
import com.ww.performancechore.mem.TestLargeClassAPI;
import com.ww.performancechore.recy_scroll.RVTestActivity;
import com.ww.performancechore.rv.RvTest2Activity;
import com.ww.performancechore.stacktest.ActivityA;
import com.ww.performancechore.webview.WebTestActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import leakcanary.AppWatcher;

public class MainActivity extends AppCompatActivity {

    Button tv1;


    private void testMediaCodec(){
        try {
            MediaCodec decoderByType = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_HEVC);
            decoderByType.release();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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




                //  to recyclerview 焦点移动 scroll performance test
//                startActivity(new Intent(MainActivity.this, RVTestActivity.class ));

                //  rv fundamental
//                startActivity(new Intent(MainActivity.this, RvTest2Activity.class ));

                testMediaCodec();

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

        findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebTestActivity.class ));
            }
        });

        findViewById(R.id.tv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InflatorActivity.class ));
            }
        });

        findViewById(R.id.tv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityA.class ));
            }
        });

        TestLargeClassAPI.test1();

    }




    static List<LeakTest.MemObject> list = new ArrayList<>() ;



}
