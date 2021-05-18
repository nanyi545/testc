package com.hehe.smartcamera.ui.cam2record;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.ViewGroup;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.hehe.smartcamera.camerax.CameraHelper;
import com.hehe.smartcamera.opgl.AbstractFilter;
import com.hehe.smartcamera.opgl.CameraFilter;
import com.hehe.smartcamera.opgl.MediaRecorder1;
import com.hehe.smartcamera.opgl.SimpleFilter;
import com.hehe.smartcamera.util.Util;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Camera2GlRender implements GLSurfaceView.Renderer {

    private boolean FBO_ENABLED = true;

    private static final String TAG = "Camera2GlRender";

    private GLSurfaceView cameraView;
    private SurfaceTexture mCameraTexure;

    private int[] textures;
    float[] mtx = new float[16];

    private SimpleFilter simpleFilter;
    AbstractFilter filter;


//    private MediaRecorder mRecorder;
    private MediaRecorder1 mRecorder;


    public Camera2GlRender(GLSurfaceView cameraView) {
        this.cameraView = cameraView;
    }


    public interface OnSurfaceCreated {
        void onSurfaceCreated(SurfaceTexture mCameraTexure);
    }
    public void setCb(OnSurfaceCreated cb) {
        this.cb = cb;
    }
    OnSurfaceCreated cb;




    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "perform   onSurfaceCreated: " + Thread.currentThread().getName());

        textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int mTextureID = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Link the texture handler to surface texture
        mCameraTexure = new SurfaceTexture(mTextureID);
//        mCameraTexure.setDefaultBufferSize(320, 240);


        mCameraTexure.setOnFrameAvailableListener(onFrameAvailableListener);

        if(cb!=null){
            cb.onSurfaceCreated(mCameraTexure);
        }

        if(FBO_ENABLED){
            filter = new CameraFilter(cameraView.getContext());
        }

        simpleFilter = new SimpleFilter(cameraView.getContext());

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


        /**
         * {@link #previewOutputSize}
         * previewOutputSize  --->   previewOutputSize: 从camera过来的预览尺寸
         * width/height       --->   GlSurfaceView 的 尺寸
         *
         * 由于摄像头转了90度， 这两个尺寸的 宽高对换。。。
         *
         */

        Log.d(TAG,"  -----size issue    onSurfaceChanged:    surface-w:"+width+"  surface-h"+height+"    prev-w:"+previewOutputSize.getWidth()
                + "   prev-h:"+previewOutputSize.getHeight());


        mCameraTexure.setDefaultBufferSize(width, height);
//        mCameraTexure.setDefaultBufferSize(previewOutputSize.getWidth(), previewOutputSize.getHeight());


//        mRecorder = new MediaRecorder(cameraView.getContext(), "",
//                EGL14.eglGetCurrentContext(),
//                width, height);

        mRecorder = new MediaRecorder1(cameraView.getContext(), Environment.getExternalStorageDirectory()+"/aaa/t.mp4",
                EGL14.eglGetCurrentContext(),
                width, height);

        Log.i(TAG, "onSurfaceChanged: " + Thread.currentThread().getName()+"  width:"+width+"  height:"+height);
        if(FBO_ENABLED){
            filter.setSize(width,height);
        }
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

        int id = textures[0];

        if(FBO_ENABLED){
            filter.setTransformMatrix(mtx);
            id = filter.onDraw(textures[0]);
        }

        id = simpleFilter.onDraw(id);


        long textureTime = mCameraTexure.getTimestamp();

        Log.i(TAG, " ------ perform  onDrawFrame  textureTime:" + textureTime);

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



    public void resetSurfaceSize(Size size){
        this.previewOutputSize = size;
        if(previewOutputSize!=null){

            /**
             *  由于摄像头转了90度， 这两个尺寸的 宽高对换
             */

            // switch width-height
            int previewWidth = previewOutputSize.getHeight();
            int previewHeight = previewOutputSize.getWidth();

            // no switch width-height
//            int previewWidth = previewOutputSize.getWidth();
//            int previewHeight = previewOutputSize.getHeight();


            Size screenSize = Util.getScreen(null);
            int screenWidth = screenSize.getWidth();
            int screenHeight = screenSize.getHeight();
            Log.d(TAG,"  -----size issue    previewOutputSize:  w:"+previewOutputSize.getWidth()+"  h:"+previewOutputSize.getHeight());
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
            Log.d(TAG,"  -----size issue    resetSurfaceSize w:"+p.width+"   h:"+p.height);
            cameraView.setLayoutParams(p);
        }
    }

    SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            Log.i(TAG, " ------ perform    onFrameAvailable: " + Thread.currentThread().getName()+ "---  equal:"+(mCameraTexure==surfaceTexture)+"  t:"+mCameraTexure.getTimestamp());
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
