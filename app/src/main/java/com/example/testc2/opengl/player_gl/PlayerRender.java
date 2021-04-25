package com.example.testc2.opengl.player_gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.testc2.R;
import com.example.testc2.codec1.H264Player;
import com.example.testc2.codec1.Player1Activity;
import com.example.testc2.opengl.camera_gl.OpenGLUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

public class PlayerRender implements GLSurfaceView.Renderer {


    Context c;
    GLSurfaceView glSurfaceView;

    public PlayerRender(GLSurfaceView glSurfaceView) {
        this.c = glSurfaceView.getContext();
        this.glSurfaceView = glSurfaceView;
    }


    //    顶点着色器
//    片元着色器
    public int program;
    //句柄  gpu中  vPosition
    private int vPosition;
    FloatBuffer textureBuffer; // 纹理坐标
    private int vCoord;
    private int vTexture;
    private int vMatrix;
    private int mWidth;
    private int mHeight;
    private float[] mtx;


    //gpu顶点缓冲区
    FloatBuffer vertexBuffer; //顶点坐标缓存区
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };





    String TAG = "PlayerRender";

    private  int[] textures;

    SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            Log.d("MyGl","--aa   onFrameAvailable-");
            glSurfaceView.requestRender();
        }
    };

    SurfaceTexture st;

    H264Player h264Player;

    Handler mainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    File sd = Environment.getExternalStorageDirectory();
                    File folder = new File(sd,"aaa");
                    if(!folder.exists()){
                        folder.mkdirs();
                    }
                    String fileName = H264Player.getFileName();
                    final File f = new File(folder,fileName);

                    h264Player = new H264Player(c,
                            f.getAbsolutePath(),
                            new Surface(st));
                    h264Player.play();

                    break;
            }
        }
    };




    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("MyGl","--PlayerRender   onSurfaceCreated  in:"+Thread.currentThread().getName());

//        textures = new int[1];
//        GLES20.glGenTextures(1, textures, 0);
//
//        int mTextureID = textures[0];
//
//        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID);
//        checkGlError("glBindTexture mTextureID");
//        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
//                GLES20.GL_NEAREST);
//        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
//                GLES20.GL_LINEAR);
//
//        st = new SurfaceTexture(mTextureID);
//        st.setOnFrameAvailableListener(onFrameAvailableListener);
//
//        mainHandler.sendEmptyMessage(111);



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;

        Log.d("MyGl","--PlayerRender   onSurfaceChanged  width:"+width+"   height:"+height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("MyGl","--PlayerRender   onDrawFrame-");


    }

}
