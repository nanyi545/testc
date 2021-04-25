package com.example.testc2.x264;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.TextureView;
import android.view.ViewGroup;

import com.example.testc2.R;
import com.example.testc2.cam2test.ImageUtil;

import java.util.concurrent.locks.ReentrantLock;


/**
 *
 *
----------------------------------------------------
 x264     https://www.videolan.org/developers/x264.html
 ----------------------------------------------------

 #include <stdio.h>

 int main()
 {
 printf("hello world\n");
 return 0;
 }
 *
 *
 * --------------------------MAC: --------------------------
 * ***** Darwin: Because macOS is, along with iOS and tvOS, powered by a piece of open source, BSD-based software called Darwin.
 *
 * sdk:     /Users/weiwang/Library/Android/sdk
 * ndk:     /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/
 * sysroot: /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/toolchains/llvm/prebuilt/darwin-x86_64/sysroot/
 *          /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/toolchains/llvm/prebuilt/darwin-x86_64/bin
 *
 *
 * compile main.cpp
 *
 * /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi23-clang++ main.cpp -o hello
 *
 *
 * ----> push
 * adb push hello /data/local/tmp
 *
 * ....
 * WARNING: linker: Unsupported flags DT_FLAGS_1=0x8000001
 * CANNOT LINK EXECUTABLE: empty/missing DT_HASH in "./hello" (built with --hash-style=gnu?)
 *
 *
 *
 *
 *
 * ---x264:script
 * source ./mac.sh    ----> this has issue ???
 *
 *
 *
 * --------------------------ubuntu--------------------------
 *
 *
 *
 *
 *
 *
 * #### 2.3 添加系统环境变量
 * >
 * > export PATH=$PATH:/root/ndk/android-ndk-r21d
 * > export SYSROOT="$NDK/toolchains/llvm/prebuilt/linux-x86_64/sysroot/"
 * > export ANDROID_GCC="$NDK/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android24-clang"
 *--------------------
 * $NDK/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android26-clang++  main.cpp -o hello
 *
 *
 *
 *
 *
 *
 */

public class X264Activity extends AppCompatActivity {



    int width = 480;
    int height = 640;
    private HandlerThread handlerThread;
    //    直播中  480 640
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.FRONT;
    private TextureView textureView;

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, 1);

        }
        return false;
    }

    X264Encoder endcoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x264);
        checkPermission();
        textureView = findViewById(R.id.t1);
        handlerThread = new HandlerThread("Analyze-thread");
        handlerThread.start();

        endcoder = new X264Encoder();
        endcoder.init(width,height,20,width*height);

        CameraX.bindToLifecycle(this, getPreView(), getAnalysis());

    }


    private Preview getPreView() {
//        480 *640  是 1 不是  2 Cmaera
        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetResolution(new Size(width, height)).setLensFacing(currentFacing).build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(newListener);
        return preview;
    }


    Preview.OnPreviewOutputUpdateListener newListener = new Preview.OnPreviewOutputUpdateListener(){
        @Override
        public void onUpdated(Preview.PreviewOutput output) {
            SurfaceTexture surfaceTexture = output.getSurfaceTexture();
            if (textureView.getSurfaceTexture() != surfaceTexture) {
                if (textureView.isAvailable()) {
                    // 当切换摄像头时，会报错
                    ViewGroup parent = (ViewGroup) textureView.getParent();
                    parent.removeView(textureView);
                    parent.addView(textureView, 0);
                    parent.requestLayout();
                }
                textureView.setSurfaceTexture(surfaceTexture);
            }
        }
    };



    private ImageAnalysis getAnalysis() {
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setCallbackHandler(new Handler(handlerThread.getLooper()))
                .setLensFacing(currentFacing)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .setTargetResolution(new Size(width, height))
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        imageAnalysis.setAnalyzer(analyzer);
        return imageAnalysis;
    }


    private ReentrantLock lock = new ReentrantLock();
    private byte[] y;
    private byte[] u;
    private byte[] v;
    private byte[] nv21;
    byte[] nv21_rotated;
    byte[] nv12;




    ImageAnalysis.Analyzer analyzer = new ImageAnalysis.Analyzer(){
        @Override
        public void analyze(ImageProxy image, int rotationDegrees) {
            try {
                lock.lock();


                //          能够 播放  H264码流   x264     摸索   x264
//3 ge
                ImageProxy.PlaneProxy[] planes =  image.getPlanes();
                // 重复使用同一批byte数组，减少gc频率
                if (y == null) {
//            初始化y v  u
                    y = new byte[planes[0].getBuffer().limit() - planes[0].getBuffer().position()];
                    u = new byte[planes[1].getBuffer().limit() - planes[1].getBuffer().position()];
                    v = new byte[planes[2].getBuffer().limit() - planes[2].getBuffer().position()];

                }

                if (image.getPlanes()[0].getBuffer().remaining() == y.length) {
                    planes[0].getBuffer().get(y);
                    planes[1].getBuffer().get(u);
                    planes[2].getBuffer().get(v);
                    int stride = planes[0].getRowStride();
                    Size size = new Size(image.getWidth(), image.getHeight());
                    int width = size.getHeight();
                    int heigth = image.getWidth();
//            Log.i(TAG, "analyze: "+width+"  heigth "+heigth);
                    if (nv21 == null) {
                        nv21 = new byte[heigth * width * 3 / 2];
                        nv21_rotated = new byte[heigth * width * 3 / 2];
                    }
                    ImageUtil.yuvToNv21(y, u, v, nv21, heigth, width);
                    ImageUtil.nv21_rotate_to_90(nv21, nv21_rotated, heigth, width);
                    endcoder.encode(nv21_rotated);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    };


}
