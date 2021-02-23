package com.example.testc2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testc2.codec1.Player1Activity;
import com.example.testc2.encoder1.EncoderActivity1;
import com.example.testc2.gif.GifHandler;
import com.example.testc2.nestedScroll.NestedScrollActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private boolean gotoOtherPage = true;
    private void toOtherPage(){
        if(!gotoOtherPage){
            return;
        }
        // media-codec demo   decoder
        Intent i =new Intent(MainActivity.this, Player1Activity.class);
        startActivity(i);


        // media-codec demo   encoder
//        Intent i =new Intent(MainActivity.this, EncoderActivity1.class);
//        startActivity(i);


        // nested scroll
//        Intent i =new Intent(MainActivity.this, NestedScrollActivity.class);
//        startActivity(i);


    }


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    GifHandler gifHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  you need this !!!!!!!!! for ndk read/write ....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        }

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());


        image = findViewById(R.id.iv1);
        toOtherPage();
        testSd();
    }


    private void testSd(){
        File f = Environment.getExternalStorageDirectory();
        File img = new File(f,"screenshot.jpg");
        Log.d("hehe","f:"+f.getAbsolutePath()+"   img:"+img.exists());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        ndkLoadGif();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    Bitmap bitmap;
    ImageView image;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int delay=gifHandler.updateFrame(bitmap);
            myHandler.sendEmptyMessageDelayed(1,delay);
            image.setImageBitmap(bitmap);
        }
    };

    public void ndkLoadGif() {


        //  mac
//        gifHandler = GifHandler.load("/sdcard/Download/textc_x/aaa.gif");

        //  win10 ----> Pixel2
        //  /sdcard/DCIM/demo3.gif
//          /sdcard/DCIM/demo4.gif
        //  /sdcard/360/download/aaa.gif
        //  /sdcard/DCIM/aaa.gif
        //  /sdcard/DCIM/aaa1.gif
        gifHandler = GifHandler.load("/sdcard/DCIM/demo3.gif");


        int width = gifHandler.getWidth();
        int height = gifHandler.getHeight();
        Log.d("gif","gif width:"+width+"  height:"+height);


        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
//C  通知C渲染完成
        int delay= gifHandler.updateFrame(bitmap);
        image.setImageBitmap(bitmap);
        myHandler.sendEmptyMessageDelayed(1, delay);

    }


}







/**
 *  gif lib:
 *  https://android.googlesource.com/platform/external/giflib/
 *  git clone https://android.googlesource.com/platform/external/giflib
 *  git remote add origin https://android.googlesource.com/platform/external/giflib
 *
 */


/**
 *
 *
 *
 use ndk-stack
 https://developer.android.com/ndk/guides/ndk-stack


 $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt
 $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt >bar.txt


 macos :
 /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt
 /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt >error1_parse.txt
 /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error2.txt >error2_parse.txt

win10 : at   D:\sdk\ndk\22.0.7026061
 ndk-stack -sym D:\as_projects\testc_git\app\.cxx\cmake\debug\x86 -dump D:\sdk\ndk\22.0.7026061\temp\err1.txt >bar.txt
 ndk-stack -sym D:\as_projects\testc_git\app\.cxx\cmake\debug\arm64-v8a -dump D:\sdk\ndk\22.0.7026061\temp\err2.txt >bar2.txt


 *
 *
 *
 **/