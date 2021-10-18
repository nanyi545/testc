package com.ww.performancechore.async_inflate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.ww.performancechore.R;

public class InflatorActivity extends AppCompatActivity {

    AsyncLayoutInflater asyncLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        asyncLayoutInflater = new AsyncLayoutInflater(this);
        asyncLayoutInflater.inflate(R.layout.activity_inflator, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                setContentView(view);
            }
        });


//        setContentView(R.layout.activity_inflator);
    }
}
