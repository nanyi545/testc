package com.ww.performancechore.rv.util;

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
//        if(TAG.equalsIgnoreCase(LLM_TAG)){
//            return;
//        }
        StackTraceElement classMethod = new Throwable().getStackTrace()[1];
        String   currMethod = classMethod.getMethodName();
        String   fullClass  = classMethod.getClassName();
        Log.d(TAG,"called at:"+fullClass+"."+currMethod+"  content:"+str);
    }

    public static final String RECYCLERVIEW_TAG = "RecyclerView";
    public static final String LLM_TAG = "LinearLayoutManager";
    public static final String ADAPTER2_TAG = "Adapter2Tag";

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
