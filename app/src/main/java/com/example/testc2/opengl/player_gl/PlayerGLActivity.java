package com.example.testc2.opengl.player_gl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.testc2.R;
import com.example.testc2.codec1.H264Player;
import com.example.testc2.codec1.Player1Activity;

import java.io.File;

public class PlayerGLActivity extends Activity {


    GLSurfaceView playerView;
    PlayerRender render2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_gl);
        playerView = findViewById(R.id.player);

        checkPermission();
//        playInSurface();


        // ---enable opaque back ground -------
        playerView.setEGLContextClientVersion(2);
        playerView.setZOrderOnTop(true);
        playerView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        playerView.getHolder().setFormat(PixelFormat.RGBA_8888);

        render2 = new PlayerRender(playerView);
        render2.setOnSurfacePreparedCallBack(new PlayerRender.OnSurfacePreparedCallBack() {
            @Override
            public void onSurfaceReady(Surface s, SurfaceTexture st) {

                File sd = Environment.getExternalStorageDirectory();
                File folder = new File(sd,"aaa");
                if(!folder.exists()){
                    folder.mkdirs();
                }
                String fileName = H264Player.getFileName();
                final File f = new File(folder,fileName);

//                player = new H264Player(MainActivity.this,
//                        f.getAbsolutePath(),
//                        s);
//                player.play();

                MediaPlayer player = new MediaPlayer();
                final File vivo = new File(folder,"vivo720p.mp4");
                player.play(vivo.getAbsolutePath(), s);

            }
        });
        playerView.setRenderer(render2);



        /**
         * 刷新方式：
         *     RENDERMODE_WHEN_DIRTY 手动刷新，調用requestRender();
         *     RENDERMODE_CONTINUOUSLY 自動刷新，大概16ms自動回調一次onDrawFrame方法
         */
        //注意必须在setRenderer 后面。
        playerView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }




    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, 1);

        }
        return false;
    }



}
