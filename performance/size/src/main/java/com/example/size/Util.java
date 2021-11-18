package com.example.size;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Debug;
import android.util.Log;

import static android.content.Context.ACTIVITY_SERVICE;

public class Util {


    public static ActivityManager.MemoryInfo getAvailableMemory(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        Log.d("meminfo","-----availMem:"+humanFileSize(memoryInfo.availMem)+"    totalMem:"+humanFileSize(memoryInfo.totalMem)+"   threshold:"+humanFileSize(memoryInfo.threshold));
        return memoryInfo;
    }




    public static void getNativeMem(){
    }


    /**
     * https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory
     *
     * You may be wondering why there is a totalMemory() AND a maxMemory(). The answer is that the JVM allocates memory lazily. Lets say you start your Java process as such:
     *
     * final long usedMem = totalMemory() - freeMemory();
     *
     */
    public static void getHeapMem(){
        Runtime runtime = Runtime.getRuntime();
        long maxMemInBytes = runtime.maxMemory();
        long availableMemInBytes = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
        long usedMemInBytes = maxMemInBytes - availableMemInBytes;
        float usedMemInPercentage = usedMemInBytes * 100 / (maxMemInBytes+0.1f) ;
        Log.d("meminfo","maxMem:"+humanFileSize(maxMemInBytes)+"    availableMem:"+humanFileSize(availableMemInBytes)+"   usedMem:"+humanFileSize(usedMemInBytes)+"  used percent:"+formatDouble(usedMemInPercentage)+"%");

        long nativeHeapSize = Debug.getNativeHeapSize();
        long nativeHeapFreeSize = Debug.getNativeHeapFreeSize();
        long usedMemInBytes2 = nativeHeapSize - nativeHeapFreeSize;
        float usedMemInPercentage2 = usedMemInBytes * 100 / (nativeHeapSize+0.1f);
        Log.d("meminfo","nativeHeapSize:"+humanFileSize(nativeHeapSize)+"    nativeHeapFreeSize:"+humanFileSize(nativeHeapFreeSize)+"   usedMem2:"+humanFileSize(usedMemInBytes2)+"  used percent2:"+formatDouble(usedMemInPercentage2)+"%");


    }

    private static String[] UNITs = {"B", "kB", "MB", "GB", "TB"};

    public static String humanFileSize(long size) {
        double ii = Math.floor( Math.log(size) / Math.log(1024) );
        int i = (int) ii;
        return formatDouble( size / Math.pow(1024, i) )  + UNITs[i];
    }

    public static String formatDouble(double d) {
        String result = String.format("%.2f", d);
        return result;
    }


}
