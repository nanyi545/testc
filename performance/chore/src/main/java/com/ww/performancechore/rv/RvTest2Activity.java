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
        rv1.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new Adapter2();
        rv1.setAdapter(adapter2);
        rv1.setItemAnimator(null);
        findViewById(R.id.shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter2.add();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter2.notifyDataSetChanged();
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
                rv1.smoothScrollBy(0,1);  //   wwnow   1 will not trigger  VIEW_RECYCLE_TAG log   2 will ??  why TODO
//                rv1.invalidate();
            }
        });

    }

}