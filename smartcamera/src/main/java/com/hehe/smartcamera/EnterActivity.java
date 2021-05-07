package com.hehe.smartcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hehe.smartcamera.ui.player.PlayerActivity;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        jump();
//        Log.d("test1","i:"+10_000);  //  10_000 ----> 10000

    }

    private void jump() {
        // camera2 ---> record
//        Intent i = new Intent(this,Camera2RecordActivity.class);
//        startActivity(i);


        // cameraX ---> opengl FBO record
//        Intent i = new Intent(this,GlRecordActivity.class);
//        startActivity(i);


        // player
//        Intent i = new Intent(this, PlayerActivity.class);
//        startActivity(i);

    }

    public void click(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn1:
                // cameraX ---> opengl FBO record
                Intent i = new Intent(this,GlRecordActivity.class);
                startActivity(i);
                break;
            case R.id.btn2:
                // player
                Intent ii = new Intent(this,PlayerActivity.class);
                startActivity(ii);
                break;
        }
    }
}
