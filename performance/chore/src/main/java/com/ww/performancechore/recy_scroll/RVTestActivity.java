package com.ww.performancechore.recy_scroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ww.performancechore.R;

public class RVTestActivity extends AppCompatActivity {

    /**
     * test rv scroll fps
     */

    Rv rc;
    private void init1(){
        rc = findViewById(R.id.rv1);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(new Adapter1(rc));
    }


    Adapter1 adapter2;
    TvRecyclerView rc2;
    private void init2(){
        rc2 = findViewById(R.id.rv1);
        rc2.setLayoutManager(new NewGridLayoutManager(this,1));
        adapter2 = new Adapter1(rc2);
        rc2.setAdapter(adapter2);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter2.notifyDataSetChanged();
            }
        });
    }


    /**
     * test measure time
     *
     *
     */

    /**
     * https://www.jianshu.com/p/b6437e4b81ac
     *
     * invalidate()和requestLayout()
     *我们已经分析过了invalidate()和requestLayout()的具体实现，现在就来总结一下invalidate()和requestLayout()的异同：
     * 相同点
     * 1.invalidate()和requestLayout()方法最终都会调用ViewRootImpl的performTraversals()方法。
     * 不同点
     * 1.invalidate()方法不会执行measureHierarchy()和performLayout()方法，也就不会执行measure和layout流程，只执行draw流程，如果开启了硬件加速则只进行调用者View的重绘。
     * 2.requestLayout()方法会依次measureHierarchy()、performLayout()和performDraw()方法，调用者View和它的父级View会重新进行measure、layout，一般情况下不会执行draw流程，子View不一定会重新measure和layout。
     * 综上，当只需要进行重新绘制时就调用invalidate()，如果需要重新测量和布局就调用requestLayout()，但是requestLayout()不保证进行重新绘制，如果要进行重新绘制可以再手动调用invalidate()。
     *
     */

    VG2 vg1;
    V1 v1;
    private void test1(){
        vg1 = findViewById(R.id.vg1);
        vg1.setDEBUG_ON("_g");
        v1 = findViewById(R.id.iv1);
        v1.setDEBUG_ON("_v");
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.requestLayout();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvtest);
        init2();
//        test1();
    }

}
