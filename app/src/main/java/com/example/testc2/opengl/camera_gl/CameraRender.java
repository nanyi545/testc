package com.example.testc2.opengl.camera_gl;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 *
  1  OnPreviewOutputUpdateListener.onUpdated ....    --->  不是每一帧的回调，是预览ready 可以预览的回调 ....
  2  onSurfaceCreated ...  -> set listener...
  3  onSurfaceChanged   width:xx   height:xx
  4            ----  onFrameAvailable
               ----  onFrameAvailable
               ----  onFrameAvailable  .....

 *
 */
public class CameraRender implements GLSurfaceView.Renderer {

    private static final String TAG = "david";
    private CameraHelper cameraHelper;
    private CameraView cameraView;

    /**
     *   link between camera / opengl
     */
    private SurfaceTexture mCameraTexure;

    public String mtx2Str(){
        StringBuilder sb=new StringBuilder();
        for (float f:mtx){
            sb.append("--"+f);
        }
        return sb.toString();
    }

    private ScreenFilter screenFilter;

    private  int[] textures;
    float[] mtx = new float[16];

    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner)cameraView.getContext();
//        打开摄像头
        cameraHelper = new CameraHelper(lifecycleOwner, previewOutputUpdateListener);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        textures = new int[1];

        //        让 SurfaceTexture   与 Gpu  共享一个数据源  0-31
        Log.d("ffff","pre attach:"+textures[0]);
        mCameraTexure.attachToGLContext(textures[0]);
        Log.d("ffff","aft attach:"+textures[0]);
        //  pre attach:0
        //  aft attach:0

        //监听摄像头数据回调，
        Log.d("ffff"," ----- onSurfaceCreated  ---> setOnFrameAvailableListener ---- ");
        mCameraTexure.setOnFrameAvailableListener(onFrameAvailableListener);
        screenFilter = new ScreenFilter(cameraView.getContext());

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("ffff","onSurfaceChanged   width:"+width+"   height:"+height);
        screenFilter.setSize(width,height);
    }


    //  有数据的时候给
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "线程: " + Thread.currentThread().getName());
        //        摄像头的数据  ---》
        //        更新摄像头的数据  给了  gpu
        mCameraTexure.updateTexImage();


        Log.i(TAG, "mtx pre: " + mtx2Str());
        mCameraTexure.getTransformMatrix(mtx);
        Log.i(TAG, "mtx after: " + mtx2Str());


        screenFilter.setTransformMatrix(mtx);
        //int   数据   byte[]
        screenFilter.onDraw(textures[0]);
    }


    Preview.OnPreviewOutputUpdateListener previewOutputUpdateListener = new Preview.OnPreviewOutputUpdateListener() {
        @Override
        public void onUpdated(Preview.PreviewOutput output) {
            Log.d("ffff"," ----- -androidx.camera.core.Preview.OnPreviewOutputUpdateListener.onUpdated");
//        摄像头预览到的数据 在这里
            mCameraTexure=output.getSurfaceTexture();
        }
    };


    SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            //一帧 一帧回调时
            Log.d("ffff"," ----- android.graphics.SurfaceTexture.OnFrameAvailableListener.onFrameAvailable");
            cameraView.requestRender();
        }
    };

}
