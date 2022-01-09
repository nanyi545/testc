package com.hehe.pluginapk1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    static List<Class> list = new ArrayList();
    private static void addClass(Class cls){
        int ind = 0;
        for(Class t:list){
            if(t.equals(cls)){
                Log.d("aaa","ind:"+ind+"  t:"+t+" cls:"+cls+"   equal");
            } else {
                Log.d("aaa","ind:"+ind+"  t:"+t+" cls:"+cls+"   not equal");
            }
            ind++;
        }
        list.add(cls);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class cls = Class.forName("com.hehe.pluginapk1.TestLog");
                    addClass(cls);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
