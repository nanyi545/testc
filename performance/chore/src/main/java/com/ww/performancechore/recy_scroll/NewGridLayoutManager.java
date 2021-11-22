package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewGridLayoutManager extends GridLayoutManager {
    public NewGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NewGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NewGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    //recyclerview-v7:25.3.0+版本
    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        if (parent instanceof TvRecyclerView) {
            return parent.requestChildRectangleOnScreen(child, rect, immediate);
        }
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
        if (parent instanceof TvRecyclerView) {
            return parent.requestChildRectangleOnScreen(child, rect, immediate);
        }
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate);
    }

    /**
     *
     * see
     * https://stackoverflow.com/questions/35653439/recycler-view-inconsistency-detected-invalid-view-holder-adapter-positionviewh
     * @param recycler
     * @param state
     *
     */
//    @Override
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        try {
//            super.onLayoutChildren(recycler, state);
//        } catch (Exception e) {
//            TvBuyLog.e("Error", "NewGridLayoutManager ----> onLayoutChildren err"+ Log.getStackTraceString(e));
//        }
//    }


    private String TAG= "NewGridLayoutManager";

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        long t1 = System.currentTimeMillis();
        super.onLayoutChildren(recycler, state);
        long t2 = System.currentTimeMillis();
        Log.d(TAG,"onLayoutChildren "+(t2-t1));
    }
}
