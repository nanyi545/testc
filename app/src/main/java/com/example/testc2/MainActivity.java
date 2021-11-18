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

import com.example.lib1.Test1InLib1;
import com.example.lib1.TestRv;
import com.example.lib1.ui.DummyMainActivity;
import com.example.testc2.basics.threads.DemoReentrantLock;
import com.example.testc2.callsuper.BBB;
import com.example.testc2.cam2test.Cam2Activity;
import com.example.testc2.codec1.Player1Activity;
import com.example.testc2.codec2.SpsActivity;
import com.example.testc2.encoder1.EncoderActivity1;
import com.example.testc2.encoder1.MyService;
import com.example.testc2.ffmpeg.TestFF1Activity;
import com.example.testc2.gif.GifHandler;
import com.example.testc2.nestedScroll.NestedScrollActivity;
import com.example.testc2.opengl.OpenGl1Activity;
import com.example.testc2.opengl.camera_gl.CameraOpenGlActivity;
import com.example.testc2.opengl.player_gl.PlayerGLActivity;
import com.example.testc2.rtmp.RtmpBilibiliActivity;
import com.example.testc2.selector.Selector2Activity;
import com.example.testc2.threadpool.TestThreadPool;
import com.example.testc2.util.DeviceUtil;
import com.example.testc2.util.TestUtil;
import com.example.testc2.videochat1.VideoChatPush;
import com.example.testc2.videochat2.VideoChatReceiver;
import com.example.testc2.x264.X264Activity;
import com.example.testc2.yuv.YuvActivity;


/**
 *
 * /Users/weiwang/AndroidStudioProjects/TestSdk2/app/build/outputs/logs/manifest-merger-debug-report.txt
 *
 * to see manifest merge record
 *
 * .....
 *
 */


/**
 *
 * LibreSSL SSL_connect: SSL_ERROR_SYSCALL in connection to
 * ---->
 *
 * git config --global --unset http.proxy
 * git config --global --unset https.proxy
 *
 */
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {


    private boolean gotoOtherPage = true;

    private void toOtherPage() {
        if (!gotoOtherPage) {
            return;
        }


        // nested scroll
//        Intent i =new Intent(MainActivity.this, NestedScrollActivity.class);
//        startActivity(i);


        // selector
//        Intent i =new Intent(MainActivity.this, SelectorActivity.class);
//        startActivity(i);
//        Intent i =new Intent(MainActivity.this, Selector2Activity.class);
//        startActivity(i);


        // open gl
//        Intent i =new Intent(MainActivity.this, OpenGl1Activity.class);
//        startActivity(i);
//        Intent i =new Intent(MainActivity.this, CameraOpenGlActivity.class);
//        startActivity(i);
//        Intent i =new Intent(MainActivity.this, PlayerGLActivity.class);
//        startActivity(i);

        /**
         * demo of replace classes in aar module ...
         *
         * need to switch dependency ...
         *     implementation('com.example.lib1:exampleLib1:1.0.0@aar') {} // original aar
         *     implementation(name: 'exampleLib1_patch', ext: 'aar')   // modified(patched) aar
         *
         * see lib1/lib1modifier for how to do it ....
         */
//        Intent i = new Intent(MainActivity.this, DummyMainActivity.class);
//        startActivity(i);


        // YUV ... test
//        Intent i = new Intent(MainActivity.this, YuvActivity.class);
//        startActivity(i);

    }


    // Used to load the 'native-lib' library on application startup.
    static {
        Log.d("TAG","MainActivity-static-block");
        System.loadLibrary("native-lib");
    }

    GifHandler gifHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(DeviceUtil.DEVICE_UTIL_KEY,"mac1:"+DeviceUtil.getMacAddress());
        Log.d(DeviceUtil.DEVICE_UTIL_KEY,"mac2:"+DeviceUtil.getWifiMac(this));

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                TestUtil.LogToFile("aaa","1231231");
            }
        },4000);

        System.out.println("System.out.println-----");

        Log.d("TAG","-------test----call-----");

        TestRv rv1;
        com.example.lib2.TestRv rv2;


        //  you need this !!!!!!!!! for ndk read/write ....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        }

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(getStr1());


        image = findViewById(R.id.iv1);
        toOtherPage();
        testSd();
        testThreads();
        testCall1();

        new BBB().call1();
        testCallLib();

        try {
            Method mod1 = getClass().getDeclaredMethod("mod1");
            Log.d("TAG","mod1    getModifiers:"+ Modifier.toString(mod1.getModifiers())+"   name:"+mod1.getName());
            mod1();
        } catch (Throwable e) {
            e.printStackTrace();
        }


        // test thread pool
        TestThreadPool t1= new TestThreadPool();
        t1.testAbNormal();


    }

    public void testCallLib(){
        Test1InLib1.call1Test();
    }

    public void testCall1(){
        Log.d("TAG","-------testCall1-----");
    }

    private void testThreads() {
        com.example.testc2.basics.threads.Test1.test1();

        DemoReentrantLock lock = new DemoReentrantLock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        }).start();

    }


    private void testSd() {
        File f = Environment.getExternalStorageDirectory();
        File img = new File(f, "screenshot.jpg");
        Log.d("hehe", "f:" + f.getAbsolutePath() + "   img:" + img.exists());
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
    private native String stringFromJNI();

    public String getStr1(){
        return stringFromJNI();
    }


    Bitmap bitmap;
    ImageView image;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int delay = gifHandler.updateFrame(bitmap);
            myHandler.sendEmptyMessageDelayed(1, delay);
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
        Log.d("gif", "gif width:" + width + "  height:" + height);


        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//C  通知C渲染完成
        int delay = gifHandler.updateFrame(bitmap);
        image.setImageBitmap(bitmap);
        myHandler.sendEmptyMessageDelayed(1, delay);

    }

    private void mod1(){
        Log.d("TAG","-------mod1 original call-----");
    }


   public void handleClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.record_btn:
                // media-codec demo   encoder
                Intent i = new Intent(MainActivity.this, EncoderActivity1.class);
                startActivity(i);
                break;
            case R.id.play_btn:
                // media-codec demo   decoder / get YUV
                Intent ii = new Intent(MainActivity.this, Player1Activity.class);
                startActivity(ii);
                break;
            case R.id.sps_btn:
                //  parse sps
                Intent iii = new Intent(MainActivity.this, SpsActivity.class);
                startActivity(iii);
                break;
            case R.id.chat_btn:
                //  parse sps
                Intent iiii = new Intent(MainActivity.this, VideoChatPush.class);
                startActivity(iiii);
                break;
            case R.id.chat_btn2:
                //  parse sps
                Intent iiii2 = new Intent(MainActivity.this, VideoChatReceiver.class);
                startActivity(iiii2);
                break;
            case R.id.cam2:
                // camera2
                Intent iiii3 = new Intent(MainActivity.this, Cam2Activity.class);
                startActivity(iiii3);
                break;
            case R.id.rtmp_push:
                Intent iiiii3 = new Intent(MainActivity.this, RtmpBilibiliActivity.class);
                startActivity(iiiii3);
                break;
            case R.id.x264_1:
                Intent p = new Intent(MainActivity.this, X264Activity.class);
                startActivity(p);
                break;

            case R.id.ff_1:
                Intent p1 = new Intent(MainActivity.this, TestFF1Activity.class);
                startActivity(p1);
                break;

        }
    }

}


/**
 * gif lib:
 * https://android.googlesource.com/platform/external/giflib/
 * git clone https://android.googlesource.com/platform/external/giflib
 * git remote add origin https://android.googlesource.com/platform/external/giflib
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * use ndk-stack
 * https://developer.android.com/ndk/guides/ndk-stack
 * <p>
 * <p>
 * $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt
 * $NDK/ndk-stack -sym $PROJECT_PATH/obj/local/armeabi-v7a -dump foo.txt >bar.txt
 * <p>
 * <p>
 * macos :
 * /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt
 * /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error1.txt >error1_parse.txt
 * /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/app/build/intermediates/cmake/debug/obj/armeabi-v7a -dump error2.txt >error2_parse.txt
 *
 *
 * /Users/weiwang/Library/Android/sdk/ndk/22.0.7026061/ndk-stack -sym /Users/weiwang/AndroidStudioProjects/TestC2/ndkcameratest1/build/intermediates/cmake/debug/obj/armeabi-v7a -dump xxxx.txt >xxxx_parse.txt
 *
 * ******   Use the .so file under obj/local/armeabi, since this is the non-stripped version
 *
 *
 *
 *
 * <p>
 * win10 : at   D:\sdk\ndk\22.0.7026061
 * ndk-stack -sym D:\as_projects\testc_git\app\.cxx\cmake\debug\x86 -dump D:\sdk\ndk\22.0.7026061\temp\err1.txt >bar.txt
 * ndk-stack -sym D:\as_projects\testc_git\app\.cxx\cmake\debug\arm64-v8a -dump D:\sdk\ndk\22.0.7026061\temp\err2.txt >bar2.txt
 *
 *
 *
 *
 **/


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