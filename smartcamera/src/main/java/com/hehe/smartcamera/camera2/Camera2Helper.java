package com.hehe.smartcamera.camera2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Helper {
    private Context context;
    private Size mPreviewSize;
    private Point previewViewSize;
    private ImageReader mImageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice mCameraDevice;
    private TextureView mTextureView;
    CaptureRequest.Builder mPreviewRequestBuilder;
    private CameraCaptureSession mCaptureSession;
    private Camera2Listener camera2Listener;


    public Camera2Helper(Context context, Camera2Listener listener) {
        this.context = context;
        camera2Listener = listener;
        cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        CamerUtil.printCams(cameraManager);
    }



    //        摄像头的管理类
    CameraManager cameraManager;


    private void resizeTextureView(){
        if(mPreviewSize==null || mTextureView==null){
            throw new RuntimeException("can not resize!!");
        }
        ViewGroup.LayoutParams p = mTextureView.getLayoutParams();
        /**
         * switch   width/height
         */
        p.width = mPreviewSize.getHeight();
        p.height = mPreviewSize.getWidth();
        mTextureView.setLayoutParams(p);
    }


    //    开启摄像头
    public synchronized void start(TextureView textureView) {
        mTextureView = textureView;

        String cameraUsed = "";

        try {


            String[] ids = cameraManager.getCameraIdList();
            Log.d("cammm","cam ids:"+ ImageUtil.toString(ids));  // cam ids:[0, 1]
            cameraUsed = ids[0];


//            这个摄像头的配置信息
            CameraCharacteristics characteristics =cameraManager.getCameraCharacteristics(cameraUsed);


//            以及获取图片输出的尺寸和预览画面输出的尺寸
// 支持哪些格式               获取到的  摄像预览尺寸    textView
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);



            ArrayList<Size> sizes = new ArrayList<Size>(Arrays.asList(map.getOutputSizes(SurfaceTexture.class)));
            Log.d("cammm","cam id:"+cameraUsed +" out put sizes:"+ sizes);


//寻找一个 最合适的尺寸
            mPreviewSize = getBestSupportedSize(sizes);
            Log.d("cammm","cam id:"+cameraUsed +"   best size:"+ mPreviewSize);

            resizeTextureView();

//nv21      420   保存到文件

            mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(),
                    mPreviewSize.getHeight(),
                    ImageFormat.YUV_420_888, 2
            );
            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
            mImageReader.setOnImageAvailableListener(new OnImageAvailableListenerImpl(), mBackgroundHandler);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d("cammm","Manifest.permission.CAMERA   not :");
                return;
            }
            cameraManager.openCamera(cameraUsed, mDeviceStateCallback, mBackgroundHandler);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback mDeviceStateCallback = new CameraDevice.StateCallback() {


        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
//            成功     前提       绝对
            mCameraDevice = cameraDevice;
//            建立绘画
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }
    };

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture =  mTextureView.getSurfaceTexture();
//            设置预览宽高
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//            创建有一个Surface   画面  ---》1

            Surface surface = new Surface(texture);
//            mPreviewRequestBuilder=  mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder=  mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            Range<Integer>[] fps = new Range[1];
            fps[0] = Range.create(20,30);


            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,fps[0]);



            mPreviewRequestBuilder.addTarget(surface);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),mCaptureStateCallback,mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    private CameraCaptureSession.StateCallback mCaptureStateCallback = new CameraCaptureSession.StateCallback() {


        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
//            系统的相机
            // The camera is already closed
            if (null == mCameraDevice) {
                return;
            }
            mCaptureSession = session;
            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                        new CameraCaptureSession.CaptureCallback() {
                        }, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        }
    };

    private class OnImageAvailableListenerImpl implements ImageReader.OnImageAvailableListener {
        private byte[] y;
        private byte[] u;
        private byte[] v;
        long initDelta  = 11;
        long frames = 0;
        long startTime;
        long elapseTime;// us !!!, not ms

        private void setupTime(){
            if(frames==0){
                startTime = System.currentTimeMillis() * 1000 ;
                elapseTime = 0 + initDelta;
            } else {
                elapseTime = System.currentTimeMillis() * 1000 - startTime + initDelta;
            }
        }

//        摄像 回调应用层  onPreviewFrame(byte[] )  这里 拿哪里
        @Override
        public void onImageAvailable(ImageReader reader) {

            setupTime();

            Log.d("cammm","onImageAvailable  frames:"+frames );
//            不是设置回调了
           Image image= reader.acquireNextImage();


           //  android.graphics.ImageFormat.YUV_420_888
            /**
             *    35 --> {@link ImageFormat.YUV_420_888}
             */
            Log.d("cammm","image:"+(image==null)+" format:"+image.getFormat() );
//            搞事情           image 内容转换成
//           yuv  H264
            Image.Plane[] planes =  image.getPlanes();
            Log.d("cammm","planes:"+(planes==null)+"  size:"+planes.length);


            int ylength = planes[0].getBuffer().limit() - planes[0].getBuffer().position();
            int ulength = planes[1].getBuffer().limit() - planes[1].getBuffer().position();
            int vlength = planes[2].getBuffer().limit() - planes[2].getBuffer().position();
            Log.d("cammm","-----  ylength:"+ylength+"   ulength:"+ulength+"   vlength:"+vlength+"   size:"+mPreviewSize+"  *:"+(mPreviewSize.getHeight()*mPreviewSize.getWidth()) );


            // 重复使用同一批byte数组，减少gc频率
            if (y == null) {
//                new  了一次
//                limit  是 缓冲区 所有的大小     position 起始大小
                y = new byte[ylength];
                u = new byte[ulength];
                v = new byte[vlength];
            }
            if (image.getPlanes()[0].getBuffer().remaining() == y.length) {
//                分别填到 yuv

                planes[0].getBuffer().get(y);
                planes[1].getBuffer().get(u);
                planes[2].getBuffer().get(v);
//                yuv 420
            }
            if(camera2Listener!=null){
                int rowStride = planes[0].getRowStride();
                Log.d("cammm","-----  call on prev  rowStride:"+rowStride);
                camera2Listener.onPreview(y, u, v, mPreviewSize, rowStride, elapseTime );
                frames++;
            }
//良性循环
           image.close();
        }
    }
    public interface Camera2Listener {
        /**
         * 预览数据回调
         * @param y 预览数据，Y分量
         * @param u 预览数据，U分量
         * @param v 预览数据，V分量
         * @param previewSize  预览尺寸
         * @param stride    步长
         * @param presentationTimeUs    *****************  frame time
         * */
        void onPreview(byte[] y, byte[] u, byte[] v, Size previewSize, int stride, long presentationTimeUs);
    }


    private Size getBestSupportedSize2() {
            return new Size(640,480);
    }


    private Size getBestSupportedSize(List<Size> sizes) {
        for(Size t:sizes){
            Log.d("fff","size:"+t);
        }
            if(true){
                return getBestSupportedSize2();
            }
        Point maxPreviewSize = new Point(1920, 1080);
        Point minPreviewSize = new Point(1280, 720);
        Size defaultSize = sizes.get(0);
        Size[] tempSizes = sizes.toArray(new Size[0]);
        Arrays.sort(tempSizes, new Comparator<Size>() {
            @Override
            public int compare(Size o1, Size o2) {
                if (o1.getWidth() > o2.getWidth()) {
                    return -1;
                } else if (o1.getWidth() == o2.getWidth()) {
                    return o1.getHeight() > o2.getHeight() ? -1 : 1;
                } else {
                    return 1;
                }
            }
        });
        sizes = new ArrayList<>(Arrays.asList(tempSizes));
        for (int i = sizes.size() - 1; i >= 0; i--) {
            if (maxPreviewSize != null) {
                if (sizes.get(i).getWidth() > maxPreviewSize.x || sizes.get(i).getHeight() > maxPreviewSize.y) {
                    sizes.remove(i);
                    continue;
                }
            }
            if (minPreviewSize != null) {
                if (sizes.get(i).getWidth() < minPreviewSize.x || sizes.get(i).getHeight() < minPreviewSize.y) {
                    sizes.remove(i);
                }
            }
        }
        if (sizes.size() == 0) {
            return defaultSize;
        }
        Size bestSize = sizes.get(0);
        float previewViewRatio;
        if (previewViewSize != null) {
            previewViewRatio = (float) previewViewSize.x / (float) previewViewSize.y;
        } else {
            previewViewRatio = (float) bestSize.getWidth() / (float) bestSize.getHeight();
        }

        if (previewViewRatio > 1) {
            previewViewRatio = 1 / previewViewRatio;
        }

        for (Size s : sizes) {
            if (Math.abs((s.getHeight() / (float) s.getWidth()) - previewViewRatio) < Math.abs(bestSize.getHeight() / (float) bestSize.getWidth() - previewViewRatio)) {
                bestSize = s;
            }
        }
        return bestSize;
    }



}
