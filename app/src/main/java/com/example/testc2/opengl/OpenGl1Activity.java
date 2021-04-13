package com.example.testc2.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.testc2.R;


/**
 * Draw a shape
 *
 * Drawing a defined shape using OpenGL ES 2.0 requires a significant amount of code, because you must provide a lot of details to the graphics rendering
 * pipeline. Specifically, you must define the following:
 *
 * Vertex Shader - OpenGL ES graphics code for rendering the vertices of a shape.
 * Fragment Shader - OpenGL ES code for rendering the face of a shape with colors or textures.
 * Program - An OpenGL ES object that contains the shaders you want to use for drawing one or more shapes.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class OpenGl1Activity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl1);
        initView();
    }

    private void initView() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        //GLContext设置OpenGLES2.0
        glSurfaceView.setEGLContextClientVersion(2);

        glSurfaceView.setRenderer(new TriangleRender());
//        glSurfaceView.setRenderer(new MyGLRenderer1());

        /*渲染方式，RENDERMODE_WHEN_DIRTY表示被动渲染，只有在调用requestRender或者onResume等方法时才会进行渲染。RENDERMODE_CONTINUOUSLY表示持续渲染*/
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

}
