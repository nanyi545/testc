package com.ww.performancechore.stacktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ww.performancechore.R;

public class ActivityB extends AppCompatActivity {

    int[][] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        findViewById(R.id.tv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityB.this, ActivityA.class );
                startActivity(i);
            }
        });

        arr = new int[1024][1024];

    }
}
