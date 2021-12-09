package com.ww.performancechore.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ww.performancechore.R;
import com.ww.performancechore.rv.rv.LinearLayoutManager;
import com.ww.performancechore.rv.rv.RecyclerView;
import com.ww.performancechore.rv.util.Logger;
import com.ww.performancechore.rv.util.VG3;


/**
 *
 * keys:
 * wwnow  previous location
 * ww:  key positions ..
 *
 *
 *
 *
 *
 *
 * 1
 *  https://blog.csdn.net/qq_33275597/article/details/93849695
 *  https://android.jlelse.eu/anatomy-of-recyclerview-part-1-a-search-for-a-viewholder-404ba3453714
 *
 *
 * 2 RecyclerView缓存原理，有图有真相
 * https://juejin.cn/post/6844903661726859271
 *
 *
 *
 *
 *
 *
 * ----------------
 * systrace
 *
 * https://www.androidperformance.com/2019/05/28/Android-Systrace-About/
 *
 * --------
 *
 *
 * RenderThread
 *
 * https://juejin.cn/post/6844903432642363399
 *
 *
 *
 */
public class RvTest2Activity extends Activity {

    RecyclerView rv1;
    Adapter2 adapter2;
    AdapterWithFocus adapter;
    LinearLayoutManager llm;

    /**
     *  why measure/layout t times ???
     *
     *  box M17 ---> measure 2 times  /  layout 2 times
     *  realme  ---> measure 2 times  /  layout 1 time
     *
     *
     * ?????
     * https://www.jianshu.com/p/733c7e9fb284
     *
     *
     *
     */
//    private void testMeasureTimes(){
//        VG3 vg3 = findViewById(R.id.hehe1);
//        vg3.setKey(VG3.generteKey());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_test2);

//        testMeasureTimes();

        rv1 = findViewById(R.id.rv1);
        llm = new LinearLayoutManager(this);
        rv1.setLayoutManager(llm);
        adapter2 = new Adapter2();
        adapter = new AdapterWithFocus(rv1,llm);
//        rv1.setAdapter(adapter2);
        rv1.setAdapter(adapter);
        rv1.setItemAnimator(null);

        /**
         * ww:
         *
         * ------------------
         * full change :
         * RecyclerView.Adapter.notifyDataSetChanged
         *
         * RecyclerView.RecyclerViewDataObserver
         * ---> RecyclerView#processDataSetCompletelyChanged(boolean)
         * -----> RecyclerView#markKnownViewsInvalid()
         * ---> RecyclerView#requestLayout()
         *
         *
         * ------------------
         * partial change:
         *    RecyclerView.Adapter#notifyItemRangeChanged(int, int)
         *    ---> RecyclerViewDataObserver#triggerUpdateProcessor()
         *    ----> RecyclerView.Recycler#tryBindViewHolderByDeadline(RecyclerView.ViewHolder, int, int, long)    # Attempts to bind view
         *    ---> RecyclerView#requestLayout()
         *
         * ------------------
         * 通过这个设置使得 Holder.needsUpdate() = true
         *    holder.addFlags(ViewHolder.FLAG_UPDATE);
         *
         *    如果Holder.needsUpdate() = true，调用 RecyclerView.Recycler#tryBindViewHolderByDeadline(RecyclerView.ViewHolder, int, int, long) 来bind view （触发Adapter的onBindViewHolder）
         *
         * ------------------
         * RecyclerView#requestLayout()   : no update :     Holder.needsUpdate() = false /
         *
         *
         *
         *
         */
        findViewById(R.id.shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int s = llm.findFirstVisibleItemPosition();
                int e = llm.findLastVisibleItemPosition();
                int count = e - s + 1;
                Logger.log(Logger.A_TAG,"1------------------------");
                adapter.notifyItemRangeChanged(s,count);
                Logger.log(Logger.A_TAG,"2------------------------");

            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Logger.log(Logger.A_TAG,"1------------------------");
//                adapter.notifyDataSetChanged();
//                Logger.log(Logger.A_TAG,"2------------------------");

                rv1.requestLayout();
            }
        });
        findViewById(R.id.btn3_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv1.smoothScrollBy(0,-1);
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rv1.smoothScrollBy(0,2);  //   ww:   1 will not trigger  VIEW_RECYCLE_TAG log   2 will ??  why TODO
//                rv1.invalidate();
//                rv1.requestLayout();

                // test scrap
//                rv1.testCall1();

                adapter.test1();
            }
        });

    }

}


/**

 original RV focus search logic ...



 ------RV scroll down not near bottom---------
  todo ??


 -----------RV scroll down near bottom----------

 ViewPostImeInputState.processKeyEvent : handle touch event ...


 RV.focusSearch
 -FocusFinder.findNextFocus
 --android.view.View#addFocusables(java.util.ArrayList<android.view.View>, int, int)
 LinearLayoutManager#onFocusSearchFailed
 -LinearLayoutManager#updateLayoutState(canUseExistingSpace = false)
 -LinearLayoutManager#fill
 --LinearLayoutManager#layoutChunk
 RecyclerView#isPreferredNextFocus.  --->  offsetDescendantRectToMyCoords(focused, mTempRect);  ??
 offsetDescendantRectToMyCoords(next, mTempRect2);

 -------->  ViewPostImeInputState.processKeyEvent -> requestFocus


 RecyclerView#requestChildFocus
 -RecyclerView#requestChildOnScreen
 --LayoutManager#requestChildRectangleOnScreen(com.ww.performancechore.rv.rv.RecyclerView, android.view.View, android.graphics.Rect, boolean, boolean)
 ---LayoutManager#getChildRectangleOnScreenScrollAmount
 ----RecyclerView#smoothScrollBy(int, int)





 **/


/**
 *
 *  LinearLayoutManager#onLayoutChildren(RecyclerView.Recycler, RecyclerView.State)
 *  - RecyclerView.LayoutManager#detachAndScrapAttachedViews(RecyclerView.Recycler)
 *
 *
 **/
