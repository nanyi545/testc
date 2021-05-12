package com.hehe.smartcamera.ui.cam2record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class Cam2GlHelper2 implements Camera2GlInterface {



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


@Override
    public void start(SurfaceTexture texture) {
        try {
            final CaptureRequest.Builder previewRequestBuilder = openCamera
                    .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            Surface previewSurface = new Surface(texture);
            previewRequestBuilder.addTarget(previewSurface);
            openCamera.createCaptureSession(Arrays.asList(previewSurface),
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
    }


}
