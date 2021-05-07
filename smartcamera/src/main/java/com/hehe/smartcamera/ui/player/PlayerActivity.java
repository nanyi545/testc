package com.hehe.smartcamera.ui.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hehe.smartcamera.R;
import com.hehe.smartcamera.player.NativeCodecPlayer;
import com.hehe.smartcamera.player.VideoPlayer;

public class PlayerActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    VideoPlayer player;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        surfaceView = findViewById(R.id.player_surface);
        player = new NativeCodecPlayer();

        videoPath = Environment.getExternalStorageDirectory()+"/aaa/t.mp4";

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                player.startPlay(videoPath,holder.getSurface());
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
