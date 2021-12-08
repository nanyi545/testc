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

    private static boolean APPEND_CALLING_METHOD = false;

    public static void log(String TAG, String str){
//        if(TAG.equalsIgnoreCase(LLM_TAG)){
//            return;
//        }
        String   currMethod = "";
        String   fullClass = "";
        if(APPEND_CALLING_METHOD){
            StackTraceElement classMethod = new Throwable().getStackTrace()[1];
            currMethod = classMethod.getMethodName();
            fullClass  = classMethod.getClassName();
        }
        Log.d("mylogger"+TAG,"called at:"+fullClass+"."+currMethod+"  content:"+str);
    }

    public static final String RECYCLERVIEW_TAG = "RecyclerView";
    public static final String LLM_TAG = "LinearLayoutManager";
    public static final String LM_TAG = "LayoutManager";
    public static final String RECYCLERVIEW_RECYCLER_TAG = "RecyclerView#RECYCLER";
    public static final String ADAPTER2_TAG = "Adapter2Tag";
    public static final String ChildHelper_TAG = "ChildHelper";


    public static final String VIEW_RECYCLE_TAG = "VIEW_RECYCLE_TAG";
    public static final String SCROLL_TAG = "scrollBy";
    public static final String UTIL_TAG = "UTIL_TAG";
    public static final String FOCUS_TAG = "FOCUS_TAG";

    public static final String A_TAG = "A_TAG";

    public static final String IMG_LOAD_TAG = "IMG_LOAD_TAG";


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

    public static void traceMarker(){
        try {
            Thread.sleep(33);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
