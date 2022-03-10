package com.example.testc2.fragmentsdemo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testc2.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 *
 * onDestroy
 *
 *
 * onDetach
 * Called when the fragment is no longer attached to its activity.  This
 * is called after {@link #onDestroy()}.
 *
 * onAttach
 * Called when a fragment is first attached to its context.
 * {@link #onCreate(Bundle)} will be called after this.
 *
 *
 * onCreate
 * Called to do initial creation of a fragment.  This is called after
 * {@link #onAttach(Activity)} and before
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
 *
 onCreateView
 * <p>It is recommended to <strong>only</strong> inflate the layout in this method and move
 * logic that operates on the returned View to {@link #onViewCreated(View, Bundle)}.

 */
public class BlankFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlankFragment1() {
        // Required empty public constructor
    }


    /**
     *  !!! if you use this constructor ...  you can not restore from args ...
     * @param mParam1
     * @param mParam2
     */
    public BlankFragment1(String mParam1,String mParam2) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment1.
     */
    public static BlankFragment1 newInstance(String param1, String param2) {
        BlankFragment1 fragment = new BlankFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String TAG = "BlankFragment1";

    @Override
    public void onAttach(Context context) {
        Log.d(TAG,"onAttach:"+this);
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // getArguments will be passed to restored fragment
    // other data need to pass through onSaveInstanceState/onViewStateRestored
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate:"+this+"   savedInstanceState null:"+(savedInstanceState==null)+"  arg null:"+(getArguments()==null));
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView:"+this);
        return inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onViewCreated:"+this);
        super.onViewCreated(view, savedInstanceState);
        if(tv==null){
            ViewGroup vg = (ViewGroup) getView();
            tv = (TextView) vg.getChildAt(0);
            tv.setText("key:"+mParam1+"  p:"+mParam2);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = tv.getText().toString()+"A";
                    tv.setText(str);
                }
            });
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG,"onViewStateRestored:"+(savedInstanceState==null));
        if(tv!=null && savedInstanceState!=null){
            tv.setText(savedInstanceState.getString(KEY1));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart:"+this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume:"+this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause:"+this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(tv!=null){
            outState.putString(KEY1, tv.getText().toString());
        }
    }

    private String KEY1="key1";
    TextView tv;


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        Log.d(TAG,"onDetach:"+this);
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
