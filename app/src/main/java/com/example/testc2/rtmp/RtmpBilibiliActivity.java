package com.example.testc2.rtmp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.testc2.R;

public class RtmpBilibiliActivity extends AppCompatActivity {
    private static final String TAG = "david";
    // ww
    String url = "rtmp://qn.live-send.acg.tv/live-qn/?streamname=live_1712295031_62212773&key=b6d9db19332ca2dedd412e3593ae05e0&schedule=rtmp&pflag=1";

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    ScreenLive screenLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtmp_bilibili);
        checkPermission();
    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
            }, 1);

        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, " url:"+url);
            mediaProjection = mediaProjectionManager.getMediaProjection
                    (resultCode, data);
            screenLive = new ScreenLive();
            screenLive.startLive(url,mediaProjection );
        }
    }

    public void startLive(View view) {
        this.mediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, 100);
    }

    public void stopLive(View view) {
    }


}
