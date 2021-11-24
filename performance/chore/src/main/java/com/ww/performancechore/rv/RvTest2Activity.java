package com.ww.performancechore.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ww.performancechore.R;
import com.ww.performancechore.rv.rv.LinearLayoutManager;
import com.ww.performancechore.rv.rv.RecyclerView;


/**
 *  https://blog.csdn.net/qq_33275597/article/details/93849695
 *
 *  https://android.jlelse.eu/anatomy-of-recyclerview-part-1-a-search-for-a-viewholder-404ba3453714
 *
 */
public class RvTest2Activity extends AppCompatActivity {

    RecyclerView rv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_test2);
        rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(new Adapter1());
    }
}