package com.ww.performancechore.rv.rv;

import android.util.Log;
import android.view.View;

public class Logger {




    public static String currentLocation() {
        StackTraceElement classMethod = new Throwable().getStackTrace()[1];
        String   currMethod = classMethod.getMethodName();
        String   fullClass  = classMethod.getClassName();
        return fullClass + "." + currMethod;
    }


    public static void log(String TAG, String str){
        StackTraceElement classMethod = new Throwable().getStackTrace()[1];
        String   currMethod = classMethod.getMethodName();
        String   fullClass  = classMethod.getClassName();
        Log.d(TAG,"called at:"+fullClass+"."+currMethod+"  content:"+str);
    }


    public static String getModeStr(int mode){
        if(mode== View.MeasureSpec.EXACTLY){
            return "EXACTLY";
        }
        if(mode== View.MeasureSpec.AT_MOST){
            return "AT_MOST";
        }
        if(mode== View.MeasureSpec.UNSPECIFIED){
            return "UNSPECIFIED";
        }
        return "non??";
    }


}
