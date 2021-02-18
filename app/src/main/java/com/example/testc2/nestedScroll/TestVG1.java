package com.example.testc2.nestedScroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

public class TestVG1 extends RelativeLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper nestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    public TestVG1(Context context) {
        super(context);
    }

    public TestVG1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestVG1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean b = super.onStartNestedScroll(child, target, nestedScrollAxes);
        Log.d("TestVG1","onStartNestedScroll child:"+child.getClass().getName()+"   target:"+target.getClass().getName()+"   super:"+b);
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean b =  super.onNestedFling(target, velocityX, velocityY, consumed);
        Log.d("TestVG1","onNestedFling  target:"+target.getClass().getName()+"   super:"+b+"  consumed:"+consumed);
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        Log.d("TestVG1","onNestedScrollAccepted child:"+child.getClass().getName()+"   target:"+target.getClass().getName()+"   axes:"+axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        Log.d("TestVG1","onStopNestedScroll child:"+child.getClass().getName() );
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        Log.d("TestVG1","onNestedPreScroll   target:"+target.getClass().getName()+"   dx:"+dx+"   dy:"+dy);

        //  parent consumes the vertical scroll ??
//        consumed[1] = dy/2;
//        consumed[1] = dy;
//        consumed[1] = dy*2;

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d("TestVG1","onNestedScroll   target:"+target.getClass().getName()+"   dyConsumed:"+dyConsumed+"   dyUnconsumed:"+dyUnconsumed);

    }

}
