package com.example.testc2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}





/**
 *
 *
 *
 use ndk-stack
 https://developer.android.com/ndk/guides/ndk-stack


 $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt
 $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt >bar.txt


 /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt
 /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt >error1_parse.txt


 *
 *
 *
 **/