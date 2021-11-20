package com.ww.performancechore.recy_scroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ww.performancechore.R;

public class RVTestActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvtest);
        init2();
    }

}
