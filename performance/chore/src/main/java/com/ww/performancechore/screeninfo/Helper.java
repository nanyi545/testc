package com.ww.performancechore.screeninfo;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

public class Helper {

    public void testScreen(Context c){
        Configuration config = c.getResources().getConfiguration();
        int smallestScreenWidthDp = config.smallestScreenWidthDp;
        int wDp = config.screenWidthDp;
        int hDp = config.screenHeightDp;
        int dpi = config.densityDpi;
        DisplayMetrics activityDisplayMetrics = c.getResources().getDisplayMetrics();
        String dm="wDp:"+wDp+"hDp:"+hDp+" dpi:"+dpi+" density:"+activityDisplayMetrics.density+"  densityDpi:"+activityDisplayMetrics.densityDpi
                +"   xdpi:"+activityDisplayMetrics.xdpi+"   scaledDensity:"+activityDisplayMetrics.scaledDensity+"  width:"+activityDisplayMetrics.widthPixels+"  height:"+activityDisplayMetrics.heightPixels;
        Log.d("testScreen","dm:"+dm);
    }
}
