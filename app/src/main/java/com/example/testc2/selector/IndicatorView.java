package com.example.testc2.selector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class IndicatorView extends View {

    public IndicatorView(Context context) {
        super(context);
        initPaint();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    int total = 5;
    int current = 0;
    int dotSize = 10;
    int currentDotSize =15;

    Paint lightPaint,heavyPaint;

    private void initPaint(){
        lightPaint = new Paint();
        lightPaint.setARGB(44, 99 , 0 ,0);
        lightPaint.setAntiAlias(true);
        heavyPaint = new Paint();
        heavyPaint.setARGB(244, 244 , 0 ,0);
        heavyPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2f;
        float deltaY = getHeight() / 2f / total;

        for (int i=0;i<total;i++){
            if(i==current){
                canvas.drawCircle(centerX,deltaY+(i*2*deltaY),currentDotSize,heavyPaint);
            } else{
                canvas.drawCircle(centerX,deltaY+(i*2*deltaY),dotSize,lightPaint);
            }
        }

    }

    public void reset(int total,int current){
        this.total = total;
        this.current=current;
        invalidate();
    }


}
