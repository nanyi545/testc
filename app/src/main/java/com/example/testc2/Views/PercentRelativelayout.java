package com.example.testc2.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.testc2.R;

public class PercentRelativelayout extends RelativeLayout {
    public PercentRelativelayout(Context context) {
        super(context);
    }

    public PercentRelativelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentRelativelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//2   widthMeasureSpec 32
//    heightMeasureSpec 32
     @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         int widthSize = MeasureSpec.getSize(widthMeasureSpec);

         int heightSize = MeasureSpec.getSize(heightMeasureSpec);
         for (int i = 0; i < getChildCount(); i++) {
             View child = getChildAt(i);

             if (child.getLayoutParams() instanceof LayoutParams) {
                 ViewGroup.LayoutParams layoutParams1=child.getLayoutParams();
                 LayoutParams layoutParams= (LayoutParams) child.getLayoutParams();
                 float widthPercent = layoutParams.widthPercent;
                 float heightPercent = layoutParams.heightPercent;
                 float marginLeftPercent = layoutParams.marginLeftPercent;
                 float marginRightPercent= layoutParams.marginRightPercent;
                 float marginTopPercent= layoutParams.marginTopPercent;
                 float marginBottomPercent = layoutParams.marginBottomPercent;

                 if (widthPercent > 0){
                     layoutParams.width = (int) (widthSize * widthPercent);
                 }

                 if (heightPercent > 0){
                     layoutParams.height = (int) (heightSize * heightPercent);
                 }

                 if (marginLeftPercent > 0){
                     layoutParams.leftMargin = (int) (widthSize * marginLeftPercent);
                 }

                 if (marginRightPercent > 0){
                     layoutParams.rightMargin = (int) (widthSize * marginRightPercent);
                 }

                 if (marginTopPercent > 0){
                     layoutParams.topMargin = (int) (heightSize * marginTopPercent);
                 }

                 if (marginBottomPercent > 0){
                     layoutParams.bottomMargin = (int) (heightSize * marginBottomPercent);
                 }
             }
         }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
//  1   调用目的 是为了给子控件生成LayoutParams   赋值给子控件    view.getLayoutParams
// View  --> generateLayoutParams
    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
    public static class LayoutParams extends RelativeLayout.LayoutParams {
        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

        /**
         * app:widthPercent="0.5"
         *  app:heightPercent="0.5"
         *   app:marginLeftPercent="0.25"
         *  app:marginTopPercent="0.25"
         * @param c
         * @param attrs
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a= c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            widthPercent = a.getFloat(
                    R.styleable.PercentLayout_widthPercent, 0);
            heightPercent = a.getFloat(R.styleable.PercentLayout_heightPercent, 0);
            marginLeftPercent = a.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0);
            marginRightPercent = a.getFloat(R.styleable.PercentLayout_marginRightPercent, 0);
            marginTopPercent = a.getFloat(R.styleable.PercentLayout_marginTopPercent, 0);
            marginBottomPercent = a.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }




}