package com.ww.performanceversions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT  = Build.VERSION.SDK_INT;

        int targetSdkVersion = getApplicationContext().getApplicationInfo().targetSdkVersion;

        Log.d("myversions","SDK_INT:"+SDK_INT+"   targetSdkVersion:"+targetSdkVersion);

//        if(hasRunTime()){
//            testDialog();
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1234);
        }

        // 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            testDialog();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // If the permission has been checked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 1234) {
            if (Settings.canDrawOverlays(this)) {
                testDialog();
            }
        }
    }

    private boolean hasRunTime(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, 1234);
                Log.d("myversions","11 no Settings.ACTION_MANAGE_OVERLAY_PERMISSION");
                return false;
            } else {
                Log.d("myversions","22 has Settings.ACTION_MANAGE_OVERLAY_PERMISSION");
                return true;
            }
        } else {
            Log.d("myversions","33");
            return true;
        }
    }




    private void testDialog(){
        View v = new FrameLayout(this);
        v.setMinimumWidth(100);
        v.setMinimumHeight(100);
        v.setLayoutParams(new ViewGroup.LayoutParams(200,200));
        v.setBackgroundColor(Color.RED);
        InnerDialog innerDialog = new InnerDialog(this);
        innerDialog.setContentView(v);
        innerDialog.show();
    }


    /**
     * https://stackoverflow.com/questions/26694108/what-is-the-difference-between-compilesdkversion-and-targetsdkversion
     *
     * That targetSdkVersion impacts the way in which permissions are requested:
     * If the device is running Android 6.0 (API level 23) or higher, and the app's targetSdkVersion is 23 or higher, the app requests permissions from the user at run-time.
     * If the device is running Android 5.1 (API level 22) or lower, or the app's targetSdkVersion is 22 or lower, the system asks the user to grant the permissions when the user installs the app.
     *
     *
     *
     * https://stackoverflow.com/questions/45163008/android-failed-to-finalize-session-26-new-target-sdk-22-doesnt-support-runti
     *
     *
     * In your case, the targetSdkVersion decide how Android requests permissions from user for some dangerous permissions,
     * if targetSdkVersion >= 23, these permissions are requested at run-time,
     * if targetSdkVersion <= 22,these permissions are requested at installing time.
     * Your may have used runtime permissions in your code which will not work with targetSdkVersion 22.
     * More detail see the Android documentation about requesting permissions
     *
     */
    static class InnerDialog extends Dialog {

        public InnerDialog(Context context) {
            super(context);
            init();
        }

        public InnerDialog(Context context, int themeResId) {
            super(context, themeResId);
            init();
        }

        protected InnerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
            init();
        }

        private void init(){
            Window mWindow = getWindow();
            if (mWindow != null) {
                int windowType = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//                int windowType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                mWindow.setType(windowType);
                WindowManager.LayoutParams attributes = mWindow.getAttributes();
                attributes.width = 100;
                attributes.height =100;
                mWindow.setAttributes(attributes);
            }
        }



    }

}
