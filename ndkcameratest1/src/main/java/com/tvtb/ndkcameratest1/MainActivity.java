package com.tvtb.ndkcameratest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NativeActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Log.d("fff",""+Thread.currentThread().getName()+"    "+Thread.currentThread().getId());
    }


    int getRotationDegree() {
        int val = 90 * ((WindowManager)(getSystemService(WINDOW_SERVICE)))
                .getDefaultDisplay()
                .getRotation();
        return val;
    }

    public void EnableUI(final long[] params)
    {

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

    static {
        System.loadLibrary("native-lib1");
    }


}
