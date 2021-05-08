package com.hehe.smartcamera.opgl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.ViewGroup;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.hehe.smartcamera.camerax.CameraHelper;
import com.hehe.smartcamera.util.Util;

import java.io.File;
import java.io.IOException;

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
    AbstractFilter filter;


//    private MediaRecorder mRecorder;
    private MediaRecorder1 mRecorder;


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

        filter = new CameraFilter(cameraView.getContext());
        simpleFilter = new SimpleFilter(cameraView.getContext());

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

//        mRecorder = new MediaRecorder(cameraView.getContext(), "",
//                EGL14.eglGetCurrentContext(),
//                width, height);

        mRecorder = new MediaRecorder1(cameraView.getContext(), Environment.getExternalStorageDirectory()+"/aaa/t.mp4",
                EGL14.eglGetCurrentContext(),
                width, height);

        Log.i(TAG, "onSurfaceChanged: " + Thread.currentThread().getName());
        filter.setSize(width,height);
        simpleFilter.setSize(width,height);

    }


    long preTimeStamp = 0;
    int counter = 0;
    int SKIP_FACTOR = 1;    // 2 : 帧率降一半  , 1 : 不变

    @Override
    public void onDrawFrame(GL10 gl) {
        mCameraTexure.updateTexImage();
        mCameraTexure.getTransformMatrix(mtx);
        simpleFilter.setTransformMatrix(mtx);
//        int id =  simpleFilter.onDraw(textures[0]);

        filter.setTransformMatrix(mtx);
        int id = filter.onDraw(textures[0]);
//        id = simpleFilter.onDraw(id);


        long textureTime = mCameraTexure.getTimestamp();

        Log.i(TAG, " ------ onDrawFrame  textureTime:" + textureTime);

        /**
         *
         * 多次onDrawFrame回调， mCameraTexure.getTimestamp()的返回值相同  ---->
         *
         * 摄像头预览的framerate 比 SurfaceView的onDrawFrame的frame rate 低
         *
         */
        if(preTimeStamp!=textureTime){

            // frame rate ---> preview frame rate决定
            //  降一半 ？？
            //  -----> 帧率降一半，好像视频大小影响很小（h265对非i帧压缩很高？）
            int remain = counter % SKIP_FACTOR;

            preTimeStamp = textureTime;
            Log.i(TAG, " ------  remain:"+remain+"  counter:"+counter+"  fireFrame:" + textureTime);

            if(remain==0){
                mRecorder.fireFrame(id, textureTime);
            }

            counter++;

        } else {

        }

    }


    Size previewOutputSize;

    Preview.OnPreviewOutputUpdateListener previewOutputUpdateListener = new Preview.OnPreviewOutputUpdateListener() {
        @Override
        public void onUpdated(Preview.PreviewOutput output) {
            previewOutputSize = output.getTextureSize();
            resetSurfaceSize();
            Log.i(TAG, "on preview Updated:"+previewOutputSize+"  t:" + Thread.currentThread().getName());
            mCameraTexure=output.getSurfaceTexture();
        }
    };

    private void resetSurfaceSize(){
        if(previewOutputSize!=null){

            int previewWidth = previewOutputSize.getHeight();
            int previewHeight = previewOutputSize.getWidth();
            Size screenSize = Util.getScreen(null);
            int screenWidth = screenSize.getWidth();
            int screenHeight = screenSize.getHeight();

            ViewGroup.LayoutParams p = cameraView.getLayoutParams();
            if((previewWidth>screenWidth)||(previewHeight>screenHeight)){
                float widthContraction =  (screenWidth+0f)/previewWidth;
                float heigthContraction = (screenHeight+0f)/previewHeight;
                float contraction = widthContraction;
                if( heigthContraction < widthContraction ){
                    contraction = heigthContraction;
                }
                int actualWidth = (int) (previewWidth * contraction);
                int actualHeight = (int) (previewHeight * contraction);
                p.width = actualWidth;
                p.height = actualHeight;
            } else {
                p.width = previewWidth;
                p.height = previewHeight;
            }
            cameraView.setLayoutParams(p);
        }
    }

    SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            Log.i(TAG, " ------ onFrameAvailable: " + Thread.currentThread().getName()+ "---  equal:"+(mCameraTexure==surfaceTexture)+"  t:"+mCameraTexure.getTimestamp());
//            cameraView.requestRender();
        }
    };



    public void startRecord(float speed) {
        try {
            mRecorder.start(speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopRecord() {
        mRecorder.stop();
    }


}
