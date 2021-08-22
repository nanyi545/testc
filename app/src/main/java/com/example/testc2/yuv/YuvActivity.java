package com.example.testc2.yuv;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.util.Log;
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

    int[] yuvSpec = new int[5];


    int getInt(byte[] by){
        int value = 0;
        for (int i = 0; i < by.length; i++)
        {
            value += ((long) by[i] & 0xffL) << (8 * i);
        }
        return value;
    }

    public byte[] getBytes(String path) throws IOException {
        InputStream is = new DataInputStream(new FileInputStream(new File(path)));
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] l1 = new byte[4];
        byte[] l2 = new byte[4];
        byte[] l3 = new byte[4];
        byte[] l4 = new byte[4];
        byte[] l5 = new byte[4];

        is.read(l1,0,4);
        is.read(l2,0,4);
        is.read(l3,0,4);
        is.read(l4,0,4);
        is.read(l5,0,4);

        yuvSpec[0] = getInt(l1);
        yuvSpec[1] = getInt(l2);
        yuvSpec[2] = getInt(l3);
        yuvSpec[3] = getInt(l4);
        yuvSpec[4] = getInt(l5);


        Log.d("fff","l1:"+ yuvSpec[0]+"  l2:"+ yuvSpec[1]+"  l3:"+ yuvSpec[2] +"  l4:"+ yuvSpec[3] +"  l5:"+ yuvSpec[4]  );


        buf = new byte[yuvSpec[0]*2];
        while ((len = is.read(buf, 0, size)) != -1){
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();


        for (int i=yuvSpec[0]+1;i<buf.length-1;i++){
            buf[i] = (byte) 128;
        }
        return buf;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuv);

        test = findViewById(R.id.test);

        byte[] ba = null;
        try {
            ba = getBytes("/sdcard/Download/aaa/t2.yuv");
        } catch (IOException e) {
            e.printStackTrace();
        }


        YuvImage yuvImage = new YuvImage(ba, ImageFormat.NV21, yuvSpec[3], yuvSpec[4], null);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvSpec[3], yuvSpec[4]), 100, baos);
        byte[] jdata = baos.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);



        test.setImageBitmap(bmp);


    }

}
