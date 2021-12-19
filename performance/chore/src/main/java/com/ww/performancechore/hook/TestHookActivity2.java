package com.ww.performancechore.hook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ww.performancechore.R;

public class TestHookActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv =new TextView(this);
        tv.setText("6666666");
        setContentView(tv);
    }
}