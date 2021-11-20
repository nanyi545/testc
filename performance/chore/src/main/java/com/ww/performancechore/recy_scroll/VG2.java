package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class VG2 extends ConstraintLayout {

    public VG2(@NonNull Context context) {
        super(context);
    }

    public VG2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VG2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 模拟较长测量时间.....
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        long t = 0;
        if(isDEBUG_ON()){
            t = System.currentTimeMillis();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isDEBUG_ON()){
            long t2 = System.currentTimeMillis();
            Log.d(TAG,"on measure time:"+(t2-t));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(isDEBUG_ON()){
            Log.d(TAG,"on onLayout:");
        }
    }

    private boolean DEBUG_ON = false;
    private String TAG = "VG2";

    public boolean isDEBUG_ON() {
        return DEBUG_ON;
    }

    public void setDEBUG_ON(String key) {
        this.DEBUG_ON = true;
        TAG+=key;
    }
}
