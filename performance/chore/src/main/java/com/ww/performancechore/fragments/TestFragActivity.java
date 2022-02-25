package com.ww.performancechore.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.ww.performancechore.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragActivity extends Activity {

    List<Fragment> list ;
    ArrayList<String> str = new ArrayList<>();

    private void setUp(){
        list = new ArrayList<>();
//        list.add(TestFragment1.newInstance("aaa"));

        str.add("sub1");str.add("sub2");str.add("sub3");
        list.add(MasterFragment.newInstance(str));
        list.add(TestFragment1.newInstance("bbb"));
        list.add(TestFragment1.newInstance("ccc"));
    }

    FrameLayout holder;


    int currentIndex = -1;
    private void showFrag(int ind){

        if(currentIndex==ind){
            return;
        }

        String tag= "f"+ind;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if(currentIndex>=0){
            ft.hide(list.get(currentIndex));
        }
        ft.show(list.get(ind));

        // Commit the transaction
        ft.commit();
        currentIndex = ind;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        setContentView(R.layout.activity_test_frag);

        holder = findViewById(R.id.frag_holder);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add( R.id.frag_holder, list.get(0) , "f0");
        ft.add( R.id.frag_holder,list.get(1) , "f1");
        ft.add( R.id.frag_holder,list.get(2) , "f2");
        ft.show( list.get(0));
        ft.hide( list.get(1));
        ft.hide( list.get(2));
        ft.commit();


        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFrag(0);
            }
        });


        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFrag(1);
            }
        });


        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFrag(2);
            }
        });


        holder.post(new Runnable() {
            @Override
            public void run() {
                showFrag(0);
            }
        });

    }

}
