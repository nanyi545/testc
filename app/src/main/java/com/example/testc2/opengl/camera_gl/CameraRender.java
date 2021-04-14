package com.example.testc2.opengl.camera_gl;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "david";
    private CameraHelper cameraHelper;
    private CameraView cameraView;
    private SurfaceTexture mCameraTexure;

    public String mtx2Str(){
        StringBuilder sb=new StringBuilder();
        for (float f:mtx){
            sb.append("--"+f);
        }
        return sb.toString();
    }

//    int
private ScreenFilter screenFilter;
    private  int[] textures;
    float[] mtx = new float[16];
    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
//        打开摄像头
        cameraHelper = new CameraHelper(lifecycleOwner, this);

    }

//textures
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//surface
        textures = new int[1];
//        1
//        让 SurfaceTexture   与 Gpu  共享一个数据源  0-31
        mCameraTexure.attachToGLContext(textures[0]);
//监听摄像头数据回调，
        mCameraTexure.setOnFrameAvailableListener(this);
        screenFilter = new ScreenFilter(cameraView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//

        screenFilter.setSize(width,height);
    }
//  有数据的时候给
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "线程: " + Thread.currentThread().getName());
//        摄像头的数据  ---》
//        更新摄像头的数据  给了  gpu
        mCameraTexure.updateTexImage();
//        不是数据

        Log.i(TAG, "mtx pre: " + mtx2Str());
        mCameraTexure.getTransformMatrix(mtx);
        Log.i(TAG, "mtx after: " + mtx2Str());

        screenFilter.setTransformMatrix(mtx);
//int   数据   byte[]
        screenFilter.onDraw(textures[0]);
    }
//
    @Override
    public void onUpdated(Preview.PreviewOutput output) {
//        摄像头预览到的数据 在这里
        mCameraTexure=output.getSurfaceTexture();
    }
//当有数据 过来的时候
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//一帧 一帧回调时
        cameraView.requestRender();
    }
}
