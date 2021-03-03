package com.example.testc2.codec1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.example.testc2.R;

import java.io.File;

/**
 *
 * MediaCodec 高效解码得到标准 YUV420P 格式帧
 * https://blog.csdn.net/u010029439/article/details/91525262
 *
 *
 *
 */
public class Player1Activity extends AppCompatActivity {
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    H264Player h264Player;
//    H264Player1 h264Player;

    ImageView hehe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player1);
        hehe = findViewById(R.id.hehe);
        H264Player.hehe = hehe;

        checkPermission();
        initSurface();
    }


    private void initSurface() {

        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = H264Player.getFileName();
        final File f = new File(folder,fileName);

        SurfaceView surface = (SurfaceView) findViewById(R.id.preview);
        final SurfaceHolder surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                h264Player = new H264Player(Player1Activity.this,
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
