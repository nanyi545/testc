package com.example.lib1.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.lib1.Test1InLib1;
import com.example.lib1.R;

public class DummyMainActivity extends Activity {

    public DummyMainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dummy_main);
        Test1InLib1.call1Test();
        this.log();
        new Util1();
    }

    private void log() {
        Log.d("DummyMainActivity", "---replaced log---");
    }

}
