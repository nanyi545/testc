package com.example.testc2.gif;

import android.graphics.Bitmap;

public class GifHandler {


    long  gifHander;//地址   指针类型    大小一样的 纽带

    static {
        System.loadLibrary("native-lib");
    }
    public  int getWidth(){
        return getWidth(gifHander);
    }
    public  int getHeight(){
        return getHeight(gifHander);
    }

    public int updateFrame(Bitmap bitmap ){
        return updateFrame(gifHander,bitmap);
    }

    private GifHandler(long gifHander) {
        this.gifHander = gifHander;
    }

    public static GifHandler load(String path) {
        long gifHander=loadGif(path);
        GifHandler gifHandler = new GifHandler(gifHander);
        return gifHandler;
    }

//   开始加载gif文件  Java+包名+类名+方法名  中间分隔用下划线        参数   第一个 JNIEnv  第二个  如static   class   对象  object
//   第三个开始是需要   传递

    public static native long loadGif(String path);
//    宽

    public static native int getWidth(long gifHander);
    //    高
    public static native int getHeight(long gifPoint);

    //    渲染图片
    public static native int updateFrame(long gifPoint, Bitmap bitmap);


}
