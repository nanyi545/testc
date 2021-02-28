package com.example.testc2.codec2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.testc2.R;

import java.io.File;

/**
 *
 * view file as hex on windows:
 *
 * 1  cd to directory
 * 2  powershell
 * 3  format-hex filename.xx | more
 *
 *
 * on mac:  ???
 * https://stackoverflow.com/questions/827326/whats-a-good-hex-editor-viewer-for-the-mac
 *
 */
public class SpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sps);
        checkPermission();
        Log.d("SPS", "oncreate:");
        start();
    }
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        return false;
    }

    public void start() {
        File sd = Environment.getExternalStorageDirectory();
        File folder = new File(sd,"aaa");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File f = new File(folder,"record2.h264");
        final SpsMediaCodec mediaCodec = new SpsMediaCodec(
                f.getAbsolutePath());
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaCodec.startCodec();
            }
        }).start();

    }
}
