package com.example.testc2.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.example.testc2.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TestUtil {

    /**
     *  how to  reproduce : noClassDef found error ?
     */
//    private static Handler h1 = new Handler((Looper)null);

    public static void LogToFile(String name,String content){
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(Environment.getExternalStorageDirectory() + "/"+name, true);
            writer.write(content);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Size getScreen(Context c){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Context app = null;
        if(c == null){
            app = App.getInstance();
        } else {
            app = c.getApplicationContext();
        }
        WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Size(width,height);
    }



    public static void copyAssets(Context c) {
        AssetManager assetManager = c.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            Log.i("tag", "------------copying:"+filename);
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File sd = Environment.getExternalStorageDirectory();
                File folder = new File(sd,"aaa");
                File outFile = new File(folder, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }


    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }



    public static String convertChinese(int i){
        if(i<=10){
            switch (i){
                case 0:
                    return "零";
                case 1:
                    return "一";
                case 2:
                    return "二";
                case 3:
                    return "三";
                case 4:
                    return "四";
                case 5:
                    return "五";
                case 6:
                    return "六";
                case 7:
                    return "七";
                case 8:
                    return "八";
                case 9:
                    return "九";
                case 10:
                    return "十";
            }
        }
        if(i<100) {
            int tens = i/10;
            int ones = i%10;
            String tensStr = convertChinese(tens);
            if(tens==1){
                tensStr="";
            }
            String onesStr = convertChinese(ones);
            if(ones==0){
                return tensStr+"十";
            } else {
                return tensStr+"十"+onesStr;
            }
        }
        if(i<999) {
            int hundreds = i/100;
            int tens = (i-hundreds*100)/10;
            int ones = i%10;
            String hundredStr=convertChinese(hundreds);
            String tensStr =  convertChinese(tens);
            if(tens==1){
                tensStr="";
            }
            String onesStr =  convertChinese(ones);
            if(tens==0){
                if(ones==0){
                    return hundredStr+"百";
                }
                if(ones!=0){
                    return hundredStr+"百零"+onesStr;
                }
            } else {
                if(ones==0){
                    return hundredStr+"百"+tensStr+"十";
                }
                if(ones!=0){
                    return hundredStr+"百"+tensStr+"十"+onesStr;
                }
            }
        }
        return "";
    }


}
