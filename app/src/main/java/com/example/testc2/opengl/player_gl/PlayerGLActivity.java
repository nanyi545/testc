package com.example.testc2.opengl.player_gl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
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

public class PlayerGLActivity extends AppCompatActivity {


    MyGl gl;
    SurfaceView s1;
    TextureView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_gl);
        gl = findViewById(R.id.my_gl);
        s1 = findViewById(R.id.s1);
        t1 = findViewById(R.id.t1);
//        initSurface(s1);
//        initSurface(gl);
//        showToTextureView();

//        toSurfaceTexture();
    }


    SurfaceTexture st1;
    private void toSurfaceTexture(){
        st1 = new SurfaceTexture(1);
        st1.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Log.d("MyGl","--aa   onFrameAvailable-");
//                st1.updateTexImage();
                gl.requestRender();
            }
        });

        Log.d("glplay","toSurfaceTexture     surfaceCreated:");
        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = H264Player.getFileName();
        final File f = new File(folder,fileName);


        t1surface = new Surface(st1);
        h264Player = new H264Player(PlayerGLActivity.this,
                f.getAbsolutePath()
                ,t1surface
        );
        h264Player.play();

    }


    Surface t1surface;
    private void showToTextureView(){

        Log.d("fff","surfaceCreated:");
        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = H264Player.getFileName();
        final File f = new File(folder,fileName);


        t1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {

                t1surface = new Surface(t1.getSurfaceTexture());
                h264Player = new H264Player(PlayerGLActivity.this,
                        f.getAbsolutePath()
                        ,t1surface
                        );
                h264Player.play();

            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
                Log.d("glplay","--aa   onSurfaceTextureUpdated-");
            }
        });
    }


    H264Player h264Player;

    private void initSurface(SurfaceView surfaceView) {
        Log.d("fff","surfaceCreated:");
        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = H264Player.getFileName();
        final File f = new File(folder,fileName);

        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.d("glplay","--aa   onSurfaceCreated-");
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
