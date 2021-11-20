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
//        init2();
        test1();
    }

}
