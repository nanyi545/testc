package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.util.AttributeSet;

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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
