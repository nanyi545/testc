package com.example.testc2.opengl.player_gl;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PlayerRender implements GLSurfaceView.Renderer {
    private static final String TAG = "david";

    private GLSurfaceView playerView;
    private SurfaceTexture mCameraTexure;

//    int
private ScreenFilter screenFilter;


    private  int[] textures;
    float[] mtx = new float[16];
    public PlayerRender(GLSurfaceView playerView) {
        this.playerView = playerView;

    }

    int mTextureID;
    SurfaceTexture mSurfaceTexture;
    Boolean updateSurface = false;
    Surface mDecoderSurface;


    Handler mainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:

                    Log.i(TAG, "onSurfaceReady  线程: " + Thread.currentThread().getName());

                    if(onSurfacePreparedCallBack!=null){
                        onSurfacePreparedCallBack.onSurfaceReady(mDecoderSurface, mSurfaceTexture);
                    }

                    break;
            }
        }
    };


    public interface OnSurfacePreparedCallBack {
        void onSurfaceReady(Surface s, SurfaceTexture st);
    }

    OnSurfacePreparedCallBack onSurfacePreparedCallBack;

    public void setOnSurfacePreparedCallBack(OnSurfacePreparedCallBack onSurfacePreparedCallBack) {
        this.onSurfacePreparedCallBack = onSurfacePreparedCallBack;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

//        GLES20.glClearColor( 0, 0, 0, 0 );
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);


        Log.i(TAG, "onSurfaceCreated  线程: " + Thread.currentThread().getName());
        textures = new int[1];
        screenFilter = new ScreenFilter(playerView.getContext());


        // Prepare texture handler
        GLES20.glGenTextures(1, textures, 0);

        mTextureID = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Link the texture handler to surface texture
        mSurfaceTexture = new SurfaceTexture(mTextureID);
//        mSurfaceTexture.setDefaultBufferSize(320, 240);


        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Log.i(TAG, "onFrameAvailable  线程: " + Thread.currentThread().getName());
                synchronized(updateSurface) {
                    updateSurface = true;
                }
                playerView.requestRender();
            }
        });


        mainHandler.sendEmptyMessage(111);

        // Create decoder surface
        mDecoderSurface = new Surface(mSurfaceTexture);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//
        screenFilter.setSize(width,height);
        Log.i(TAG, "onSurfaceChanged  width:"+width+"  height:"+height  );
        Log.i("info", "onSurfaceChanged  width:"+width+"  height:"+height  );
    }


//  有数据的时候给
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "onDrawFrame  线程: " + Thread.currentThread().getName()+"  updateSurface:"+updateSurface);

        if(!updateSurface){
            return;
        }
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );


//        摄像头的数据  ---》
//        更新摄像头的数据  给了  gpu
        mSurfaceTexture.updateTexImage();
//        不是数据

        printMtx("pre get");
        mSurfaceTexture.getTransformMatrix(mtx);
        printMtx("after get");

        screenFilter.setTransformMatrix(mtx);
//int   数据   byte[]
        screenFilter.onDraw(textures[0]);
    }


    private void printMtx(String info){
        StringBuilder sb = new StringBuilder();
        for (float f:mtx){
            sb.append("-"+f);
        }
        Log.i("info", "printMtx_"+info+":"+sb.toString()  );
    }



}
