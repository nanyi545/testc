package com.example.testc2.nestedScroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

/**
 *
 * NestedScrolling   ----->   parent/child   分配滑动距离
 *
 *
 *
 */
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
        Log.d("TestVG1","onStartNestedScroll child:"+child.getClass().getSimpleName()+"   target:"+target.getClass().getSimpleName()+"   super:"+b);
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean b =  super.onNestedFling(target, velocityX, velocityY, consumed);
        Log.d("TestVG1","onNestedFling  target:"+target.getClass().getSimpleName()+"   super:"+b+"  consumed:"+consumed);
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        Log.d("TestVG1","onNestedScrollAccepted child:"+child.getClass().getSimpleName()+"   target:"+target.getClass().getSimpleName()+"   axes:"+axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        Log.d("TestVG1","onStopNestedScroll child:"+child.getClass().getSimpleName() );
    }

    // parent consumes all dy
    private boolean parentConsumesAllDy = false;

    // parent consumes 1/2 dy , child consumes 1/2 dy
    private boolean parentConsumesHalfDy = false;


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        Log.d("TestVG1","onNestedPreScroll   target:"+target.getClass().getName()+"   dx:"+dx+"   dy:"+dy);


        //  分配滑动距离

        if(parentConsumesAllDy){
            // 滑动 dy 距离
            scrollBy(0, dy);
            // 将消耗掉的 dy 放入 consumed 数组通知子 view
            consumed[1] = dy;
        } else if (parentConsumesHalfDy){
            // 滑动 dy 距离
            scrollBy(0, dy/2);
            // 将消耗掉的 dy 放入 consumed 数组通知子 view
            consumed[1] = dy/2;
        } else {
            // do nothing ...  child consumes all dy

        }


    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d("TestVG1","onNestedScroll   target:"+target.getClass().getSimpleName()+"   dyConsumed:"+dyConsumed+"   dyUnconsumed:"+dyUnconsumed);

    }

}
