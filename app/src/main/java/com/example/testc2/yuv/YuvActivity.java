package com.example.testc2.yuv;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.testc2.R;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class YuvActivity extends AppCompatActivity {

    YuvImage yuvImage;

    ImageView test;



    public byte[] getBytes(String path) throws IOException {
        InputStream is =   new DataInputStream(new FileInputStream(new File(path)));
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1){
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();
        return buf;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuv);

        test = findViewById(R.id.test);

        byte[] ba = null;
        try {
            ba = getBytes("/sdcard/Download/aaa/t1.yuv");
        } catch (IOException e) {
            e.printStackTrace();
        }


        YuvImage yuvImage = new YuvImage(ba, ImageFormat.NV21, 640, 480, null);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, 640, 480), 100, baos);
        byte[] jdata = baos.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);



        test.setImageBitmap(bmp);


    }

}
