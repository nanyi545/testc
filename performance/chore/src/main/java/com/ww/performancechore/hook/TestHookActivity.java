package com.ww.performancechore.hook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ww.performancechore.R;

public class TestHookActivity extends Activity {

    HookUtils9 hookUtils9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hook);
        hookUtils9 = new HookUtils9(getApplicationContext(),TestHookActivity.class);
        try {
            hookUtils9.hookAms();
            hookUtils9.hookSystemHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestHookActivity.this,TestHookActivity2.class));
            }
        });
    }
}