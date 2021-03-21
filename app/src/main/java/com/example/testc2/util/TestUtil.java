package com.example.testc2.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.FileWriter;
import java.io.IOException;

public class TestUtil {

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
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Size(width,height);
    }


}
