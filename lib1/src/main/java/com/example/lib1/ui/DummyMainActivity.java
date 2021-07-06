package com.example.lib1.ui;


import android.app.Activity;
import android.os.Bundle;

import com.example.lib1.R;
import com.example.lib1.Test1InLib1;

public class DummyMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_main);
        Test1InLib1.call1Test();
    }
}
