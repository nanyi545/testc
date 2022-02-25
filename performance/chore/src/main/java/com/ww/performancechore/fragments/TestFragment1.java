package com.ww.performancechore.fragments;


import android.app.Fragment;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ww.performancechore.R;

/**
 * Use the {@link TestFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment1 extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private int count=0;

    public TestFragment1() {
        // Required empty public constructor
    }

    public static TestFragment1 newInstance(String param1) {
        TestFragment1 fragment = new TestFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }
    TextView tv;
    private void updateDisp(){
        tv.setText(mParam1+"  "+count);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test_fragment1, container, false);
        tv = v.findViewById(R.id.tv1);
        updateDisp();

        Button btn = v.findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                updateDisp();
            }
        });
        return v;
    }

}
