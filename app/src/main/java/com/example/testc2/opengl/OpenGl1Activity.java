package com.example.testc2.opengl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.testc2.R;
import com.example.testc2.opengl.projectAndView.MyGLRenderer;


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
 */
public class OpenGl1Activity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    ConstraintLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl1);
        root =findViewById(R.id.root_);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] pos = new int[2];
                v.getLocationOnScreen(pos);
                Log.d("ddd","-----   sx:"+pos[0]+"  sy:"+pos[1]);
                Log.d("ddd","-----   w:"+v.getWidth()+"  h:"+v.getHeight());
                int vCenterX = v.getWidth()/2 + 0;
                int vCenterY = v.getHeight()/2 + 0;
                float deltaX = event.getX() - vCenterX;
                float deltaY = event.getY() - vCenterY;
                Log.d("ddd","-----   x:"+event.getX()+"  y:"+event.getY());
                Log.d("ddd","-----   dx:"+deltaX+"  dy:"+deltaY);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        updateEyeView(deltaX,deltaY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        updateEyeView(deltaX,deltaY);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        updateEyeView(deltaX,deltaY);
                        return true;
                }
                return false;
            }
        });
        initView();
    }

    MyGLRenderer myGLRenderer;

    void updateEyeView(float dx,float dy){
        if(myGLRenderer!=null){
            myGLRenderer.setEye(dx/555f*3,dy/555f*3, 3f);
            glSurfaceView.requestRender();
        }
    }


    private void initView() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        //GLContext设置OpenGLES2.0
        glSurfaceView.setEGLContextClientVersion(2);


        // ---enable opaque back ground -------
        glSurfaceView.setZOrderOnTop(true);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);


//        glSurfaceView.setRenderer(new TriangleRender());
//        glSurfaceView.setRenderer(new MyGLRenderer1());

        myGLRenderer = new MyGLRenderer();
        glSurfaceView.setRenderer(myGLRenderer);


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
