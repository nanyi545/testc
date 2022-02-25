package com.ww.performancechore.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ww.performancechore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Use the {@link MasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private ArrayList<String> keys;


    public MasterFragment() {
        // Required empty public constructor
    }

    public static MasterFragment newInstance(ArrayList<String> keys) {
        MasterFragment fragment = new MasterFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, keys);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keys = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    List<Button> btns;
    LinearLayout lo;
    List<Fragment> frags;


    int currentIndex = -1;
    private void showFrag(int ind){

        if(currentIndex==ind){
            return;
        }

        String tag= "sub-f"+ind;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if(currentIndex>=0){
            ft.hide(frags.get(currentIndex));
        }
        ft.show(frags.get(ind));


        // Commit the transaction
        ft.commit();
        currentIndex = ind;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master, container, false);
        lo = v.findViewById(R.id.tabs);
        btns = new ArrayList<>();
        frags = new ArrayList<>();

        for (int i = 0;i<keys.size();i++){
            TestFragment1 fragment1 = TestFragment1.newInstance(keys.get(i));
            frags.add(fragment1);

            Button b = new Button(v.getContext());
            b.setText("sub-frag"+i);
            lo.addView(b);
            int finalI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFrag(finalI);
                }
            });
        }


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add( R.id.sub_frag_holder, frags.get(0) , "sub-frag0");
        ft.add( R.id.sub_frag_holder,frags.get(1) , "sub-frag1");
        ft.add( R.id.sub_frag_holder,frags.get(2) , "sub-frag2");
        ft.show( frags.get(0));
        ft.hide( frags.get(1));
        ft.hide( frags.get(2));
        ft.commit();



        showFrag(0);
        return v;
    }

}
