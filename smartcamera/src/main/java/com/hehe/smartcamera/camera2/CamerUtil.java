package com.hehe.smartcamera.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class CamerUtil {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void printCams(CameraManager cameraManager){
        try {
            String[] ids = cameraManager.getCameraIdList();
            for(String i:ids){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(i);
                int LENS_FACING = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                /**
                 *  {@link CameraCharacteristics.LENS_FACING_FRONT}
                 */
                Log.d("CamerUtil","id:"+i+"  facing:"+LENS_FACING);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
