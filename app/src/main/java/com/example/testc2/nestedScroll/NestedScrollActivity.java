package com.example.testc2.nestedScroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testc2.R;

public class NestedScrollActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);
        rv=findViewById(R.id.vr1);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RvAdapter1());
    }


}
