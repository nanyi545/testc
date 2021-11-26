package com.ww.performancechore.rv.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class VG3 extends RelativeLayout {
    public VG3(Context context) {
        super(context);
    }

    public VG3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VG3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static int KEY_INT=1;
    public static String generteKey(){
        String str = "VG3_instance_"+KEY_INT;
        KEY_INT++;
        return str;
    }


    String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Timer timer= new Timer();
        super.onLayout(changed, l, t, r, b);
        timer.end(key,"onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Timer timer= new Timer();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        timer.end(key,"onMeasure");
    }
}
