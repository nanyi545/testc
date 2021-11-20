package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class V1 extends View {
    public V1(Context context) {
        super(context);
    }

    public V1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public V1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isDEBUG_ON()){
            Log.d(TAG,"ondraw");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isDEBUG_ON()){
            Log.d(TAG,"onmeasure");
        }
    }

    private boolean DEBUG_ON = false;
    private String TAG = "V1";

    public boolean isDEBUG_ON() {
        return DEBUG_ON;
    }

    public void setDEBUG_ON(String key) {
        this.DEBUG_ON = true;
        TAG+=key;
    }



}
