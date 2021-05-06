package com.hehe.smartcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        jump();
    }

    private void jump() {
        // camera2 ---> record
//        Intent i = new Intent(this,Camera2RecordActivity.class);
//        startActivity(i);

        // cameraX ---> opengl FBO record
        Intent i = new Intent(this,GlRecordActivity.class);
        startActivity(i);
    }

}
