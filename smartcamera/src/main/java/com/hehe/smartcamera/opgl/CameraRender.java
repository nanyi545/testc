package com.hehe.smartcamera.opgl;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.hehe.smartcamera.camerax.CameraHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer {

    private static final String TAG = "CameraRender";
    private CameraHelper cameraHelper;
    private GLSurfaceView cameraView;
    private SurfaceTexture mCameraTexure;


    private  int[] textures;
    float[] mtx = new float[16];

    private SimpleFilter simpleFilter;

    public CameraRender(GLSurfaceView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner, previewOutputUpdateListener);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated: " + Thread.currentThread().getName());
        textures = new int[1];
        mCameraTexure.attachToGLContext(textures[0]);
        mCameraTexure.setOnFrameAvailableListener(onFrameAvailableListener);
        simpleFilter = new SimpleFilter(cameraView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged: " + Thread.currentThread().getName());
        simpleFilter.setSize(width,height);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "线程: " + Thread.currentThread().getName());
        mCameraTexure.updateTexImage();
        mCameraTexure.getTransformMatrix(mtx);
        simpleFilter.setTransformMatrix(mtx);
        int id =  simpleFilter.onDraw(textures[0]);
    }

    Preview.OnPreviewOutputUpdateListener previewOutputUpdateListener = new Preview.OnPreviewOutputUpdateListener() {
        @Override
        public void onUpdated(Preview.PreviewOutput output) {
            mCameraTexure=output.getSurfaceTexture();
        }
    };


    SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            Log.i(TAG, "onFrameAvailable: " + Thread.currentThread().getName());
            cameraView.requestRender();
        }
    };

}
