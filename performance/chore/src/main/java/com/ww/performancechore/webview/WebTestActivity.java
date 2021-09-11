package com.ww.performancechore.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.webkit.WebView;

import com.ww.performancechore.App;
import com.ww.performancechore.R;

public class WebTestActivity extends AppCompatActivity {


    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);
        ConstraintLayout root = findViewById(R.id.myroot);
        web = WebViewHolder.getWeb();
        root.addView(web,ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
        web.loadUrl("https://main.m.taobao.com/");
    }

    @Override
    protected void onDestroy() {
        ConstraintLayout root = findViewById(R.id.myroot);
        root.removeAllViews();
        super.onDestroy();
    }
}
