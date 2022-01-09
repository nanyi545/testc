package com.hehe.pluginapk1;

import android.util.Log;


/**
 *
 * cd /media/ww/LINX2/androidsdk/platform-tools
 *
 * ./adb push /media/ww/LINX2/projects/testc2/testc/pluginapk1/build/outputs/apk/debug/pluginapk1-debug.apk /sdcard/Download/aaa
 *
 */
public class TestLog {

    // static field first
    static int a = 10;

    // then static block ...
    static {
        Log.v("mmp","plugin:TestLog static block  a1:"+a);
        a = 5;
        Log.v("mmp","plugin:TestLog static block  a2:"+a);
    }

    public void say(String str){
        Log.v("mmp","plugin:"+str);
    }
}
