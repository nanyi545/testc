package com.ww.performancechore.stacktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ww.performancechore.MainActivity;
import com.ww.performancechore.R;

public class ActivityA extends AppCompatActivity {

    int[][] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        findViewById(R.id.tv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityA.this, ActivityB.class ));
            }
        });

        arr = new int[1024][1024];
    }
}
