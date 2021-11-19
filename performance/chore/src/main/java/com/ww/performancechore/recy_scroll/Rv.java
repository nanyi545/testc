package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Rv extends RecyclerView {

    private String Tag="myrv";

    public Rv(@NonNull Context context) {
        super(context);
    }

    public Rv(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Rv(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean mIsFocusedViewScrollNeedCenter = true;


    /**
     * 通过该方法设置选中的item居中
     * <p>
     * 该方法能够确定在布局中滚动或者滑动时候，子item和parent之间的位置
     * dy，dx的实际意义就是在滚动中下滑和左右滑动的距离
     * 而这个值的确定会严重影响滑动的流畅程度
     */
    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        Log.d(Tag,"requestChildRectangleOnScreen");
        if (!mIsFocusedViewScrollNeedCenter) {
            return true;
        }
        Rect rect1 = new Rect();
        getDrawingRect(rect1);
        Rect rect2 = new Rect();
        child.getDrawingRect(rect2);
        offsetDescendantRectToMyCoords(child, rect2);
        int dx = 0, dy = rect2.centerY() - rect1.centerY();
        if (dx != 0 || dy != 0) {
            if (immediate) {
                scrollBy(dx, dy);
            } else {
                smoothScrollBy(dx, dy);
            }
            // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
            postInvalidate();
            return true;
        }
        return false;
    }

}
