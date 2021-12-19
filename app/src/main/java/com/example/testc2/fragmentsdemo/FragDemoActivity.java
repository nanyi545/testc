package com.example.testc2.fragmentsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.testc2.R;

public class FragDemoActivity extends AppCompatActivity
        implements BlankFragment1.OnFragmentInteractionListener
{

    /**
     *
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     *
     * It is strongly recommended that subclasses do not have other constructors with parameters, since these constructors
     * will not be called when the fragment is re-instantiated; instead, arguments can be supplied by the caller with
     * setArguments(Bundle) and later retrieved by the Fragment with getArguments().
     * ----------
     *     当我们的应用进入后台，系统可能因为内存紧张而杀掉activity，在activity被杀掉之前调用onSaveInstanceState()保存每个实例的状态,
     *     保证等到我们切换到前台时实例状态可以在onCreate(Bundle)或者onRestoreInstanceState(Bundle)
     *     (传入的Bundle参数是由onSaveInstanceState封装好的)中恢复。在这个过程其实系统有时不会确保把全部的实例都保存下来了，所以就造成了我们遇到的异常。
     *---------------------
     *
     *
     * String name = makeFragmentName(container.getId(), itemId)  ;
     * Fragment fragment = fragmentManager.findFragmentByTag(name);
     *
     * //  从我们的activity进入到别人的activity的时候，我们的activity进入后台，被杀。切回来的时候，会被restore
     * // 如果fragment没有使用newInstance，不能restore ，findFragmentByTag 还是找到了这个没有正确restore的fragment，导致显示不正常
     *
     */

    Fragment f1;

    private String TAG = "FragDemoActivityxx";


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG,"onSaveInstanceState:" +this );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG,"onRestoreInstanceState:" +this );
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy:" +this );
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume:"+this );
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate:"+(savedInstanceState==null)+"  hash:"+this +"   f1 null:"+(f1==null));
        setContentView(R.layout.activity_frag_demo);

        if(savedInstanceState==null){

            // 如果使用setArguments，fragment 自动restore的时候，会重新获取args
            f1 = BlankFragment1.newInstance("f1","data1");

            // 如果不使用setArguments，fragment 自动restore的时候，无法重新获取args
//            f1 = new BlankFragment1("f1","data1");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frag_root, f1,"f1");
            ft.commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG,"onFragmentInteraction");
    }

}
