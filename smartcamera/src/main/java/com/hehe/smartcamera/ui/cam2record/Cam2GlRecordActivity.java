package com.hehe.smartcamera.ui.cam2record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;

import com.hehe.smartcamera.R;

public class Cam2GlRecordActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    Camera2GlInterface helper;
    Camera2GlRender render;

    ImageView iv;
    Handler h1 = new Handler (Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1789) {
                if(msg.obj instanceof Bitmap){
                    Bitmap bitmap = (Bitmap) msg.obj;
                    iv.setImageBitmap(bitmap);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam2_record);
        glSurfaceView = findViewById(R.id.glsurface);
        iv = findViewById(R.id.img);


        glSurfaceView.setEGLContextClientVersion(2);
        render = new Camera2GlRender(glSurfaceView);

//        helper = new Camera2GlHelper(this);

        helper = new Cam2GlHelper2(this, CameraCharacteristics.LENS_FACING_FRONT);
//        helper = new Cam2GlHelper2(this, CameraCharacteristics.LENS_FACING_BACK);

        if(helper instanceof Cam2GlHelper2){
            Cam2GlHelper2 cast = (Cam2GlHelper2) helper;
            cast.setUiHandler(h1);
        }


        helper.init(render);
        render.setCb(new Camera2GlRender.OnSurfaceCreated() {
            @Override
            public void onSurfaceCreated(SurfaceTexture mCameraTexure) {
                helper.start(mCameraTexure);
            }
        });
        glSurfaceView.setRenderer(render);


//        glSurfaceView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                render.startRecord(1.0f);
//            }
//        },5000);
//        glSurfaceView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                render.stopRecord();
//            }
//        },35000);


    }


}
