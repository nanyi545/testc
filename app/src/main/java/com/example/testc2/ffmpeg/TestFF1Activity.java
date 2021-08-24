package com.example.testc2.ffmpeg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.testc2.R;

import java.io.File;

public class TestFF1Activity extends AppCompatActivity {
    Surface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ff1);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        final SurfaceHolder surfaceViewHolder = surfaceView.getHolder();

        surfaceViewHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //获取文件路径，这里将文件放置在手机根目录下
                surface = surfaceViewHolder.getSurface();

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {    }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {    }
        });

    }


    public void play(View view) {
//        String folderurl = new File(Environment.getExternalStorageDirectory(), "input.mp4").getAbsolutePath();


//        File sd = Environment.getExternalStorageDirectory();
//        File folder = new File(sd,"aaa");
//        File outFile = new File(folder, "vivo720p.mp4");

        File outFile = new File("/sdcard/Download/aaa/1628745307.mp4");
        String folderurl = outFile.getAbsolutePath();
        play(folderurl, surface);
    }

    public native int play(String url, Surface surface);

}
