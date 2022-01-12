package com.ww.performancechore.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ww.performancechore.R;
import com.ww.performancechore.rv.util.VG3;

public class TestOffsetActivity extends AppCompatActivity {

    View vv;
    VG3 vg3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_offset);
        vv = findViewById(R.id.tv1);
        vv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d("OffsetTest","onLayoutChange    top:"+top+"  oldTop:"+oldTop);
            }
        });
        vg3 = findViewById(R.id.myroot);
        vg3.setKey(VG3.generteKey());
        findViewById(R.id.btn11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OffsetTest","top:"+vv.getTop()+"  bottom:"+vv.getBottom());

                vv.offsetTopAndBottom(100);
                // this will not trigger layout / measure ...
                // this may violate viewgroup's layout logic


                // what's the difference ???
                //  offsetTopAndBottom ---> only changes top/bottom and invalidate() ...  not layout
//                vv.layout(vv.getLeft(),vv.getTop()+100,vv.getRight(),vv.getBottom()+100);

                Log.d("OffsetTest","top:"+vv.getTop()+"  bottom:"+vv.getBottom());



                //  if you call request layout, offsetting position might be reset ...
//                vv.requestLayout();  // this will make offsetTopAndBottom call useless ...
//                vv.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("OffsetTest","top:"+vv.getTop()+"  bottom:"+vv.getBottom());
//                    }
//                });



                // why ???
                //   offsetTopAndBottom will change  mTop/mBottom values
                //   requestLayout will change it back ....


            }
        });
    }
}