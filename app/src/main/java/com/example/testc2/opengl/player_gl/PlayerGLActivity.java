package com.example.testc2.opengl.player_gl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.testc2.R;
import com.example.testc2.codec1.H264Player;
import com.example.testc2.codec1.Player1Activity;

import java.io.File;

public class PlayerGLActivity extends AppCompatActivity {
    MyGl gl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_gl);
        gl  = findViewById(R.id.my_gl);
        initSurface();
    }


    H264Player h264Player;

    private void initSurface() {
        Log.d("fff","surfaceCreated:");
        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = H264Player.getFileName();
        final File f = new File(folder,fileName);

        final SurfaceHolder surfaceHolder = gl.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.d("fff","surfaceCreated:");
                h264Player = new H264Player(PlayerGLActivity.this,
                        f.getAbsolutePath()
                        ,
                        surfaceHolder.getSurface());
                h264Player.play();
            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

    }


}
