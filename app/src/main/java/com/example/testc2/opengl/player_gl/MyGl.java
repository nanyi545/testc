package com.example.testc2.opengl.player_gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;

import com.example.testc2.codec1.H264Player;

import java.io.File;

public class MyGl extends GLSurfaceView {

    PlayerRender render;

    public MyGl(Context context) {
        super(context);
        init();
    }

    public MyGl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        render = new PlayerRender(this);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
