package com.hehe.smartcamera.ui.cam2record;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;

import com.hehe.smartcamera.R;

public class Cam2GlRecordActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    Camera2GlInterface helper;
    Camera2GlRender render;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam2_record);
        glSurfaceView = findViewById(R.id.glsurface);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new Camera2GlRender(glSurfaceView);


//        helper = new Camera2GlHelper(this);
        helper = new Cam2GlHelper2(this, CameraCharacteristics.LENS_FACING_FRONT);


        helper.init(render);


        render.setCb(new Camera2GlRender.OnSurfaceCreated() {
            @Override
            public void onSurfaceCreated(SurfaceTexture mCameraTexure) {
                helper.start(mCameraTexure);
            }
        });
        glSurfaceView.setRenderer(render);

    }
}
