package com.tvtb.ndkcameratest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NativeActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import static android.hardware.camera2.CameraMetadata.LENS_FACING_BACK;


/**
 * use NativeActivity  ....
 *
 * 1      externalNativeBuild {
 *           cmake {
 *             path "src/main/cpp/CMakeLists.txt"
 *           }
 *        }
 *
 *
 *
 *
 * 2      cpp ---> android_main
 *
 *  extern "C" void android_main(struct android_app* state) {
 *
 *     // loop waiting for stuff to do.
 *     while (1) {
 *         // Read all pending events.
 *         int events;
 *         struct android_poll_source* source;
 *
 *         while (ALooper_pollAll(0, NULL, &events, (void**)&source) >= 0) {
 *
 *             // Process this event.
 *             if (source != NULL) {
 *                 source->process(state, source);
 *             }
 *
 *             // Check if we are exiting.
 *             if (state->destroyRequested != 0) {
 *                 return;
 *             }
 *         }
 *         __android_log_print(ANDROID_LOG_VERBOSE, "loop", "---------------");
 *     }
 *
 * }
 *
 *
 *
 *  3    把lib名字配置给native-activity
 *                  <meta-data android:name="android.app.lib_name"
 *                 android:value="native-lib1" />
 *
 *--------  still needed !!!
 *     static {
 *         System.loadLibrary("native-lib1");
 *     }
 *
 *
 *
 *
 */
public class MainActivity extends NativeActivity {


    static boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(play) {
            setContentView(R.layout.activity_main);
            SurfaceView s = findViewById(R.id.surfaceview1);
            s.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    setSurface(holder.getSurface());
                    String fileName = "clips/testfile.mp4";
                    createStreamingMediaPlayer(getResources().getAssets(),fileName);
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                }
            });
        }



//        Log.d("fff",""+Thread.currentThread().getName()+"    "+Thread.currentThread().getId());



        handler.sendEmptyMessageDelayed(1,3000);

    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    int getRotationDegree() {
        int val = 90 * ((WindowManager)(getSystemService(WINDOW_SERVICE)))
                .getDefaultDisplay()
                .getRotation();
        return val;
    }



    PopupWindow popupWindow;

    public static final long OPER_INIT = 0;
    public void EnableUI(final long[] params)
    {

        Log.d("ffff","EnableUI:"+params[0]);
        if(params[0]==OPER_INIT){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    LayoutInflater layoutInflater
                            = (LayoutInflater) getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.widgets, null);
                    popupWindow = new PopupWindow(
                            popupView,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);

                    // Show our UI over NativeActivity window
                    popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.START, 0, 0);
                    popupWindow.update();

                }
            });
        }
    }


    private static final int PERMISSION_REQUEST_CODE_CAMERA = 1;

    String TAG = "MainUI";

    public void RequestCamera() {
        if(!isCamera2Device()) {
            Log.d(TAG,"RequestCamera  not camera2");
            return;
        }
        Log.d(TAG,"RequestCamera  camera2");

        String[] accessPermissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean needRequire  = false;
        for(String access : accessPermissions) {
            int curPermission = ActivityCompat.checkSelfPermission(this, access);
            if(curPermission != PackageManager.PERMISSION_GRANTED) {
                needRequire = true;
                break;
            }
        }
        if (needRequire) {
            ActivityCompat.requestPermissions(
                    this,
                    accessPermissions,
                    PERMISSION_REQUEST_CODE_CAMERA);
            return;
        }
        notifyCameraPermission(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /*
         * if any permission failed, the sample could not play
         */
        if (PERMISSION_REQUEST_CODE_CAMERA != requestCode) {
            super.onRequestPermissionsResult(requestCode,
                    permissions,
                    grantResults);
            return;
        }

        if(grantResults.length == 2) {
            Log.d("onRequestPermissionsResult","- "+grantResults[0]+"- "+grantResults[1] );
            notifyCameraPermission(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED);
        }
    }




    private boolean isCamera2Device() {
        CameraManager camMgr = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        boolean camera2Dev = true;
        try {
            String[] cameraIds = camMgr.getCameraIdList();
            if (cameraIds.length != 0 ) {
                for (String id : cameraIds) {
                    CameraCharacteristics characteristics = camMgr.getCameraCharacteristics(id);
                    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                    Log.d(TAG,"RequestCamera  id:"+id+"  deviceLevel:"+deviceLevel+"   facing:"+facing);
                    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY &&
                            facing == LENS_FACING_BACK) {
                        camera2Dev =  false;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            camera2Dev = false;
        }
        return camera2Dev;
    }


    native static void notifyCameraPermission(boolean granted);
    native static void setCamera(boolean useCamera);


    native void test1();

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(1,3000);
//            test1();
        }
    };




    static {
        System.loadLibrary("native-lib1");
        boolean useCamera = true;
        setCamera(useCamera);
        play = !useCamera;
    }



    private void setupPlayer(){

    }


    public static native boolean createStreamingMediaPlayer(AssetManager assetMgr, String filename);
    public static native void setSurface(Surface surface);


}
