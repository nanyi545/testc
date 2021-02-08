package com.example.testc2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testc2.gif.GifHandler;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    GifHandler gifHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        image = findViewById(R.id.iv1);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ndkLoadGif();
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

        gifHandler = GifHandler.load("/sdcard/Download/textc_x/aaa.gif");
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


 *
 *
 *
 **/