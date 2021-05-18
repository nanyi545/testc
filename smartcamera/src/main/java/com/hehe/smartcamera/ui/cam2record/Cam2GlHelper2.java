package com.hehe.smartcamera.ui.cam2record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hehe.smartcamera.opgl.FileUtils;

import java.util.Arrays;
import java.util.List;

public class Cam2GlHelper2 implements Camera2GlInterface {


    private boolean multipleOutputs = true;


    private ImageReader mImageReader;


    private void initReader(){

        //  ----> does switch width/height has issue ? ...
        mImageReader = ImageReader.newInstance(
                mPreviewSize.getWidth(),
                mPreviewSize.getHeight(),
                ImageFormat.YUV_420_888, 2
        );

        //  -----> this will give wrong Image size when reading ...

//        mImageReader = ImageReader.newInstance(
//                mPreviewSize.getHeight(),
//                mPreviewSize.getWidth(),
//                ImageFormat.YUV_420_888, 2
//        );
        HandlerThread mBackgroundThread = new HandlerThread("imgs-ana");
        mBackgroundThread.start();
        Handler imgHandler = new Handler(mBackgroundThread.getLooper());
        mImageReader.setOnImageAvailableListener(new OnImageAvailableListenerImpl() , imgHandler);
    }


    private class OnImageAvailableListenerImpl implements ImageReader.OnImageAvailableListener {
        private byte[] preY;
        private byte[] y;
        private byte[] u;
        private byte[] v;
        long initDelta  = 11;
        long frames = 0;

        boolean convertYUVtoPNG = false;

        int imgCount = 0;

        //        摄像 回调应用层  onPreviewFrame(byte[] )  这里 拿哪里
        @Override
        public void onImageAvailable(ImageReader reader) {


            Log.d("perform"," --onImageAvailable-- "+Thread.currentThread().getName()+"  textureTime:"+st.getTimestamp());

            long start= System.currentTimeMillis();

//            不是设置回调了
            Image image= reader.acquireNextImage();


            /***
             * operation ....
             *
             */
            if(convertYUVtoPNG){

                // width / height  ---> same as camera preview size ....

                Log.d("perform","--size issue   img width:"+image.getWidth()+"   img height:"+image.getHeight());

                //converting to JPEG
                byte[] jpegData = ImageUtil.imageToByteArray(image);
                //write to file (for example ..some_path/frame.jpg)
                if(imgCount<6){
                    FileUtils.writeBytes(jpegData, "ff"+imgCount+".png");
                    imgCount++;
                }

            } else {

                //  android.graphics.ImageFormat.YUV_420_888
                /**
                 *    35 --> {@link android.graphics.ImageFormat.YUV_420_888}
                 */
                Image.Plane[] planes =  image.getPlanes();
                int ylength = planes[0].getBuffer().limit() - planes[0].getBuffer().position();
                int ulength = planes[1].getBuffer().limit() - planes[1].getBuffer().position();
                int vlength = planes[2].getBuffer().limit() - planes[2].getBuffer().position();
                Log.d("perform","----- size issue   ylength:"+ylength+"   ulength:"+ulength+"   vlength:"+vlength
                        +"   img width:"+image.getWidth()+"   img height:"+image.getHeight());


                //  img --->
                //  ylength:1175032   ulength:587511   vlength:587511


                // 重复使用同一批byte数组，减少gc频率
                if (y == null) {
//                new  了一次
//                limit  是 缓冲区 所有的大小     position 起始大小
                    y = new byte[ylength];
                    u = new byte[ulength];
                    v = new byte[vlength];
                    preY = new byte[ylength];
                }
                if (image.getPlanes()[0].getBuffer().remaining() == y.length) {
//                分别填到 yuv
                    planes[0].getBuffer().get(y);
                    planes[1].getBuffer().get(u);
                    planes[2].getBuffer().get(v);

                }

                if(frames==0){

                } else {
                    float sad = ComputeUtil.getSad(y,preY);
                    Log.d("sad","sad:"+sad);
                }


                System.arraycopy(y,0,preY,0,y.length);

            }


            frames++;
            image.close();
            long end= System.currentTimeMillis();
            Log.d("perform"," --onImageAvailable   sad   done:"+ ((end-start)/1000f) );






        }
    }


    /**
     *  operation on UI ...
     */
    Handler uiHandler;
    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    private Context context;
    private CameraManager connectManager;
    private CameraDevice openCamera;
    private WorkThreader cameraThreader;

    public Cam2GlHelper2(Activity c, int facing) {
        context = c;

        cameraThreader = new WorkThreader("camera");
        cameraThreader.start();

        initCamera(facing);
    }


    private void initCamera(int facing) {
        try {
            connectManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (connectManager != null) {
                String[] allIds = connectManager.getCameraIdList();
                CameraCharacteristics params = null;


                for (String id : allIds) {
                    params = connectManager.getCameraCharacteristics(id);

                    if (params != null && params.get(CameraCharacteristics.LENS_FACING)
                            == facing) {
                        openCamera(id);
                        break;
                    }

                }
            }

            mPreviewSize = getBestSupportedSize2();

            initReader();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("MissingPermission")
    private void openCamera(String id) throws CameraAccessException {
        connectManager.openCamera(id, new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                openCamera = camera;
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                openCamera.close();
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                camera.close();
            }
        }, null);
    }

    SurfaceTexture st;

@Override
    public void start(SurfaceTexture texture) {

    st = texture;
        try {
            while(openCamera==null){
                Thread.sleep(10);
            }
//            final CaptureRequest.Builder previewRequestBuilder = openCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            final CaptureRequest.Builder previewRequestBuilder = openCamera.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            Surface previewSurface = new Surface(texture);
            previewRequestBuilder.addTarget(previewSurface);


            if(multipleOutputs){
                previewRequestBuilder.addTarget(mImageReader.getSurface());
            } else {

            }


            List<Surface> list = Arrays.asList(previewSurface);

            if(multipleOutputs){
                list = Arrays.asList(previewSurface,mImageReader.getSurface());
            }

            openCamera.createCaptureSession( list ,
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (null == openCamera) {
                                return;
                            }
                            //  当摄像头已经准备好时，开始显示预览
                            try {
                                //  自动对焦
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                //  显示预览
                                CaptureRequest previewRequest = previewRequestBuilder.build();
                                session.setRepeatingRequest(previewRequest,
                                        null, cameraThreader.getBindHandler());
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(context, "camera ConfigureFailed", Toast.LENGTH_SHORT).show();
                        }
                    }, cameraThreader.getBindHandler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
}

    public void stop() {
        if (openCamera != null) {
            openCamera.close();
        }
        cameraThreader.release();
    }


    @Override
    public void init(Camera2GlRender render) {
        render.resetSurfaceSize(mPreviewSize);
    }


    private Size mPreviewSize;
    private Size getBestSupportedSize2() {
        return new Size(1920,1080);
//        return new Size(640,640);
    }


}
