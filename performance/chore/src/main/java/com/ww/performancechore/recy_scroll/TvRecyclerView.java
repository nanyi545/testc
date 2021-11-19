package com.ww.performancechore.recy_scroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TvRecyclerView extends RecyclerView {

    private String TAG = "TvRecyclerView";

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    static class TvBuyLog {
        static void i (String TAG,String str){
            Log.d(TAG,str);
        }
        static void d (String TAG,String str){
            Log.d(TAG,str);
        }
    }

    //焦点是否居中
    private boolean mSelectedItemCentered = true;
    private int mSelectedItemOffsetStart = 0;
    private int mSelectedItemOffsetEnd = 0;

    public interface FocusSearchFailedListener {
        void onSearchFailed(View focused, int direction);
    }

    private FocusSearchFailedListener focusSearchFailedListener;


    public void setFocusSearchFailedListener(FocusSearchFailedListener focusSearchFailedListener) {
        this.focusSearchFailedListener = focusSearchFailedListener;
    }

    //是否可以纵向移出
    private boolean mCanFocusOutVertical = false;
    //是否可以横向移出
    private boolean mCanFocusOutHorizontal = true;
    //焦点移出recyclerview的事件监听
    private FocusLostListener mFocusLostListener;
    //焦点移入recyclerview的事件监听
    private FocusGainListener mFocusGainListener;


    //焦点搜索拦截
    private OnFocusSearchIntercept mOnFocusSearchIntercept;
    //最后一次聚焦的位置
    private int mLastFocusPosition = 0;
    private View mLastFocusView = null;
    private boolean isFirstChildAttachedToWindow = true;
    private boolean mIsFocusedViewScrollNeedCenter = true;
    private boolean isKeyUpFocusedUnDismiss = false;//键盘快速上滑时焦点不丢失

    public TvRecyclerView(Context context) {
        super(context);
        init();
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void init() {
        setItemAnimator(null);
        /**
         * 该属性是当一个为view获取焦点时，定义viewGroup和其子控件两者之间的关系。
         *
         * 属性的值有三种：
         * beforeDescendants：viewgroup会优先其子类控件而获取到焦点
         * afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
         * blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
         * */
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setChildrenDrawingOrderEnabled(true);
        this.setFocusable(true);
    }

    /**
     * 是否默认聚焦到第一个子view
     *
     * @param isFocus
     */
    public void setIsFirstChildFocus(boolean isFocus) {
        isFirstChildAttachedToWindow = isFocus;
    }

    /**
     * 实现焦点记忆的关键代码
     * <p>
     * root.addFocusables会遍历root的所有子view和孙view,然后调用addFocusable把isFocusable的view添加到focusables
     */
    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        TvBuyLog.i(TAG, "views = " + views);
        TvBuyLog.i(TAG, "lastFocusView = " + mLastFocusView + " mLastFocusPosition = " + mLastFocusPosition + "  hasF:" + this.hasFocus());
        if (this.hasFocus() || mLastFocusView == null) {
            //在recyclerview内部焦点切换
            super.addFocusables(views, direction, focusableMode);
        } else {
            //将当前的view放到Focusable views列表中，再次移入焦点时会取到该view,实现焦点记忆功能
            View tmp = getLayoutManager().findViewByPosition(mLastFocusPosition);
            if (tmp != null) {
                views.add(tmp);
            } else {
                super.addFocusables(views, direction, focusableMode);
            }
            TvBuyLog.i(TAG, "views.add(lastFocusView)");
        }
    }

    public interface KeyInterceptor {
        boolean intercept(KeyEvent event);
    }

    KeyInterceptor mKeyinterceptor;

    public void setKeyinterceptor(KeyInterceptor mKeyinterceptor) {
        this.mKeyinterceptor = mKeyinterceptor;
    }


    public interface LongPressKeyCallback {
        boolean interceptLongPress(int keyCode);
    }

    LongPressKeyCallback mLongPressCallback;

    public void setLongPressCallback(LongPressKeyCallback mLongPressCallback) {
        this.mLongPressCallback = mLongPressCallback;
    }

    boolean consumKeyUp = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        TvBuyLog.i(TAG, "dispatchKeyEvent:" + event.toString());

        if (mKeyinterceptor != null && mKeyinterceptor.intercept(event)) {
            return true;
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getRepeatCount() > 1) {
                if (mLongPressCallback!=null){
                    return mLongPressCallback.interceptLongPress(event.getKeyCode());
                }
            }
        }

        //解决快速下滑时焦点乱跑
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                View focusedView = getFocusedChild();
                boolean callPostChange = false;
                if (onFocusShiftListener != null) {
                    callPostChange = onFocusShiftListener.onchange(mLastFocusPosition, View.FOCUS_DOWN);
                }
                View nextFocusView;
                try {
                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_DOWN);
                } catch (Exception e) {
                    nextFocusView = null;
                }
                if (onFocusShiftListener != null && callPostChange) {
                    onFocusShiftListener.onPostChange(mLastFocusPosition, View.FOCUS_DOWN);
                }
                if (nextFocusView == null && focusedView != null) {
                    //未找到焦点时消费事件
                    if (focusSearchFailedListener != null) {
                        focusSearchFailedListener.onSearchFailed(focusedView, View.FOCUS_DOWN);
                    }
                    consumKeyUp = true;
                    return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && isKeyUpFocusedUnDismiss) {
                View focusedView = getFocusedChild();
                boolean callPostChange = false;
                if (onFocusShiftListener != null) {
                    callPostChange = onFocusShiftListener.onchange(mLastFocusPosition, View.FOCUS_UP);
                }

                View nextFocusView;
                try {
                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_UP);
                } catch (Exception e) {
                    nextFocusView = null;
                }

                if (onFocusShiftListener != null && callPostChange) {
                    onFocusShiftListener.onPostChange(mLastFocusPosition, View.FOCUS_UP);
                }
                if (nextFocusView == null && focusedView != null) {
                    //未找到焦点时消费事件
                    if (focusSearchFailedListener != null) {
                        focusSearchFailedListener.onSearchFailed(focusedView, View.FOCUS_UP);
                    }
                    consumKeyUp = true;
                    return true;
                }
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (consumKeyUp) {
                    consumKeyUp = false;
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     *  dispatch key event 的监听
     */
    public interface OnFocusShiftListener{
        /**
         * @param currentFocus  : position
         * @param direction  {@link View#FOCUS_DOWN}   {@link View#FOCUS_UP}
         * @retrun call or not call post change ...
         */
        boolean onchange(int currentFocus, int direction);
        void onPostChange(int currentFocus, int direction);
    }

    OnFocusShiftListener onFocusShiftListener;

    public void setOnFocusShiftListener(OnFocusShiftListener onFocusShiftListener) {
        this.onFocusShiftListener = onFocusShiftListener;
    }


    public int getmLastFocusPosition() {
        return mLastFocusPosition;
    }


    /**
     * 上下键时候，焦点不能离开
     */
    private boolean forceFocusStayInThisListForUpAndDownKey = false;

    public void setForceFocusStayInThisListForUpAndDownKey(boolean forceFocusStayInThisListForUpAndDownKey) {
        this.forceFocusStayInThisListForUpAndDownKey = forceFocusStayInThisListForUpAndDownKey;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        try {
            if (mOnFocusSearchIntercept != null) {
                View interceptFocusView = mOnFocusSearchIntercept.onInterceptFocusSearch(focused, direction);
//                TvBuyLog.i(TAG, "focusSearch intercept = " + (interceptFocusView == null ? "null" : interceptFocusView.toString()));
                if (interceptFocusView != null) {
                    return interceptFocusView;
                }
            }

            if (mOnFocusSearchIntercept2 != null) {
                boolean intercept = mOnFocusSearchIntercept2.shouldInterceptFocusSearch(focused, direction);
                if (intercept) {
                    return null;
                }
            }

            View realNextFocus = super.focusSearch(focused, direction);
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
//            TvBuyLog.i(TAG, "focused = " + focused);
//            TvBuyLog.i(TAG, "nextFocus = " + nextFocus);
//            TvBuyLog.i(TAG, "realNextFocus = " + realNextFocus);
//            //canScrollVertically(1)  true表示能滚动，false表示已经滚动到底部
//            //canScrollVertically(-1) true表示能滚动，false表示已经滚动到顶部
//            TvBuyLog.i(TAG, "canScrollVertically(-1) = " + canScrollVertically(-1));
//            TvBuyLog.i(TAG, "canScrollVertically(1) = " + canScrollVertically(1));
//            TvBuyLog.i(TAG, "canScrollHorizontally(-1) = " + canScrollHorizontally(-1));
//            TvBuyLog.i(TAG, "canScrollHorizontally(1) = " + canScrollHorizontally(1));
            switch (direction) {
                case FOCUS_RIGHT:
                    //调用移出的监听
                    if (nextFocus == null) {
                        if (mCanFocusOutHorizontal) {
                            if (mFocusLostListener != null) {
                                mFocusLostListener.onFocusLost(focused, direction);
                            }
                            return realNextFocus;
                        } else {
                            return null;
                        }
                    }
                    break;
                case FOCUS_LEFT:
                    //调用移出的监听
                    if (nextFocus == null) {
                        if (mCanFocusOutHorizontal) {
                            if (mFocusLostListener != null) {
                                mFocusLostListener.onFocusLost(focused, direction);
                            }
                            return realNextFocus;
                        } else {
                            return null;
                        }
                    }
                    break;
                case FOCUS_UP:
                    if (forceFocusStayInThisListForUpAndDownKey) {
                        return nextFocus;
                    }
                    if (nextFocus == null && !canScrollVertically(-1)) {
                        //滑动到顶部
                        if (mCanFocusOutVertical) {
                            return realNextFocus;
                        } else {
                            return null;
                        }
                    }
                    break;
                case FOCUS_DOWN:
                    if (forceFocusStayInThisListForUpAndDownKey) {
                        return nextFocus;
                    }
                    if (nextFocus == null && !canScrollVertically(1)) {
                        //滑动到底部
                        if (mCanFocusOutVertical) {
                            return realNextFocus;
                        } else {
                            return null;
                        }
                    }
                    break;
            }
            return realNextFocus;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过ViewParent#requestChildFocus通知父控件即将获取焦点
     *
     * @param child   下一个要获得焦点的recyclerview item
     * @param focused 当前聚焦的view
     */
    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            TvBuyLog.i(TAG, "nextchild = " + child + ",focused = " + focused+ "     focusPos = " + mLastFocusPosition);
            if (!hasFocus()) {
                //recyclerview 子view 重新获取焦点，调用移入焦点的事件监听
                if (mFocusGainListener != null) {
                    mFocusGainListener.onFocusGain(child, focused);
                }
            }

            //取得获得焦点的item的position
            mLastFocusView = focused;
            mLastFocusPosition = getChildViewHolder(child).getAdapterPosition();
            TvBuyLog.i(TAG, "focusPos = " + mLastFocusPosition);

            //计算控制recyclerview 选中item的居中从参数
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        TvBuyLog.i(TAG, "mSelectedItemOffsetStart = " + mSelectedItemOffsetStart);
        TvBuyLog.i(TAG, "mSelectedItemOffsetEnd = " + mSelectedItemOffsetEnd);
        //执行过super.requestChildFocus之后hasFocus会变成true
        super.requestChildFocus(child, focused);
    }

    /**
     * 通过该方法设置选中的item居中
     * <p>
     * 该方法能够确定在布局中滚动或者滑动时候，子item和parent之间的位置
     * dy，dx的实际意义就是在滚动中下滑和左右滑动的距离
     * 而这个值的确定会严重影响滑动的流畅程度
     */
    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
//        final int parentLeft = getPaddingLeft();
//        final int parentRight = getWidth() - getPaddingRight();
//
//        final int parentTop = getPaddingTop();
//        final int parentBottom = getHeight() - getPaddingBottom();
//
//        final int childLeft = child.getLeft() + rect.left;
//        final int childTop = child.getTop() + rect.top;
//
//        final int childRight = childLeft + rect.width();
//        final int childBottom = childTop + rect.height();
//
//        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
//        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
//
//        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
//        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);
//
//
//        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
//        final boolean canScrollVertical = getLayoutManager().canScrollVertically();
//
//        // Favor the "start" layout direction over the end when bringing one side or the other
//        // of a large rect into view. If we decide to bring in end because start is already
//        // visible, limit the scroll such that start won't go out of bounds.
//        final int dx;
//        if (canScrollHorizontal) {
//            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                dx = offScreenRight != 0 ? offScreenRight
//                        : Math.max(offScreenLeft, childRight - parentRight);
//            } else {
//                dx = offScreenLeft != 0 ? offScreenLeft
//                        : Math.min(childLeft - parentLeft, offScreenRight);
//            }
//        } else {
//            dx = 0;
//        }
//
//        // Favor bringing the top into view over the bottom. If top is already visible and
//        // we should scroll to make bottom visible, make sure top does not go out of bounds.
//        final int dy;
//        if (canScrollVertical) {
//            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
//        } else {
//            dy = 0;
//        }

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
            TvBuyLog.i(TAG, ".requestChildRectangleOnScreen dx = " + dx + " dy = " + dy);
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

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        TvBuyLog.i(TAG, ".scrollTo x=" + x + " y=" + y);
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
        TvBuyLog.i(TAG, ".scrollBy x=" + x + " y=" + y);
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy);
        TvBuyLog.i(TAG, ".smoothScrollBy dx=" + dx + " dy=" + dy);
    }

    //默认聚焦在第一个子view
    @Override
    public void onChildAttachedToWindow(View child) {
        if (isFirstChildAttachedToWindow) {
            if (child.isFocusable()) {
                child.requestFocus();
                isFirstChildAttachedToWindow = false;
            }
        }
        super.onChildAttachedToWindow(child);
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LayoutManager manager = getLayoutManager();
        if (manager != null) {
            LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
            return layout.getOrientation() == LinearLayoutManager.VERTICAL;

        }
        return false;
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    public void resetFocusItem(int y){
        resetFocusItem(y,true);
    }
    /**
     * 设置前一次的聚焦位置  {@link #isFirstChildAttachedToWindow} 为false时候需要调用此方法来决定初始聚焦位置
     *
     * @param index
     * @param requestFocus
     */
    public void resetFocusItem(int index, boolean requestFocus) {
        TvBuyLog.d(TAG, "resetFocusItem:" + index + "   requestFocus:" + requestFocus + "      last focus null:" + (mLastFocusView == null));
        mLastFocusPosition = index;
        if (mLastFocusView == null || mLastFocusView.getParent() != this) {
            View lastFocusItemView = getLayoutManager().findViewByPosition(mLastFocusPosition);
            mLastFocusView = lastFocusItemView;
            TvBuyLog.d(TAG, "resetFocusItem:" + index + "   requestFocus:" + requestFocus + "      after reset ---> last focus null:" + (mLastFocusView == null));
        }
        if (requestFocus) {
            post(new Runnable() {
                @Override
                public void run() {
                    requestLastFocusItem();
                }
            });
        }
    }

    public void scroll2PrePosition(int index, int dy) {
        mLastFocusPosition = index;
        ((LinearLayoutManager) this.getLayoutManager()).scrollToPositionWithOffset(mLastFocusPosition, dy);
    }

    public void forceResetItemToCenterAndResetLastFocus(int index, boolean requestFocus) {
        int rvh = getHeight();
        int itemHeight = 0;
        if (mLastFocusView != null) {
            itemHeight = mLastFocusView.getHeight();
        }
        mLastFocusView = null;
        int topOffset = itemHeight == 0 ? 0 : (rvh - itemHeight) / 2;
        resetFocusItem(index, requestFocus);
        ((LinearLayoutManager) this.getLayoutManager()).scrollToPositionWithOffset(mLastFocusPosition, topOffset);
    }

    public void resetItemToCenterAndResetLastFocus(int index, boolean requestFocus) {
        int rvh = getHeight();
        if (mLastFocusView == null) {
            return;
        }
        int itemHeight = mLastFocusView.getHeight();
        int topOffset = (rvh - itemHeight) / 2;
        resetFocusItem(index, requestFocus);
        ((LinearLayoutManager) this.getLayoutManager()).scrollToPositionWithOffset(mLastFocusPosition, topOffset);
    }

    public void resetItemToCenter(int index) {
        int rvh = getHeight();
        resetFocusItem(index, false);
        int itemHeight = 0;
        if (mLastFocusView == null) {
            itemHeight = mLastFocusView.getHeight();
        }
        int topOffset = (rvh - itemHeight) / 2;
        ((LinearLayoutManager) this.getLayoutManager()).scrollToPositionWithOffset(mLastFocusPosition, topOffset);
    }



    public void requestLastFocusItem() {
        View lastFocusItemView = getLayoutManager().findViewByPosition(mLastFocusPosition);
        if (lastFocusItemView != null) {
            lastFocusItemView.requestFocus();
        }
    }

    public void setIsFocusedViewScrollNeedCenter(boolean isFocusedViewScrollNeedCenter) {
        this.mIsFocusedViewScrollNeedCenter = isFocusedViewScrollNeedCenter;
    }

    public void setKeyUpFocusedUnDismiss(boolean keyUpFocusedUnDismiss) {
        isKeyUpFocusedUnDismiss = keyUpFocusedUnDismiss;
    }

    /**
     * 是否可以垂直滚动
     **/
    public boolean isCanFocusOutVertical() {
        return mCanFocusOutVertical;
    }

    /**
     * 设置可以垂直滚动
     **/
    public void setCanFocusOutVertical(boolean canFocusOutVertical) {
        mCanFocusOutVertical = canFocusOutVertical;
    }

    /**
     * 是否可以水平滚动
     **/
    public boolean isCanFocusOutHorizontal() {
        return mCanFocusOutHorizontal;
    }

    /**
     * 设置是否可以水平滚动
     **/
    public void setCanFocusOutHorizontal(boolean canFocusOutHorizontal) {
        mCanFocusOutHorizontal = canFocusOutHorizontal;
    }

    /**
     * 设置焦点丢失监听
     */
    public void setFocusLostListener(FocusLostListener focusLostListener) {
        this.mFocusLostListener = focusLostListener;
    }

    public interface FocusLostListener {
        void onFocusLost(View lastFocusChild, int direction);
    }

    /**
     * 设置焦点获取监听
     */
    public void setGainFocusListener(FocusGainListener focusListener) {
        this.mFocusGainListener = focusListener;
    }

    public void setOnFocusSearchIntercept(OnFocusSearchIntercept mOnFocusSearchIntercept) {
        this.mOnFocusSearchIntercept = mOnFocusSearchIntercept;
    }

    //FocusSearch拦截
    public interface OnFocusSearchIntercept {
        View onInterceptFocusSearch(View focused, int direction);
    }

    public interface OnFocusSearchIntercept2 {
        boolean shouldInterceptFocusSearch(View focused, int direction);
    }

    OnFocusSearchIntercept2 mOnFocusSearchIntercept2;

    public void setOnFocusSearchIntercept2(OnFocusSearchIntercept2 mOnFocusSearchIntercept2) {
        this.mOnFocusSearchIntercept2 = mOnFocusSearchIntercept2;
    }


    public interface FocusGainListener {
        void onFocusGain(View child, View focued);
    }


    public int getScollYDistance() {
        if (!(this.getLayoutManager() instanceof LinearLayoutManager)) {
            return 0;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView == null) {
            return 0;
        }
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


}
