package com.hehe.smartcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.hehe.smartcamera.opgl.CameraRender;

public class GlRecordActivity extends AppCompatActivity {

    GLSurfaceView surface;
    CameraRender render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_record);
        surface = findViewById(R.id.my_surface);
        surface.setEGLContextClientVersion(2);
        render = new CameraRender(surface);
        surface.setRenderer(render);
    }
}
