package com.ww.performancechore.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        vg3 = findViewById(R.id.myroot);
        vg3.setKey(VG3.generteKey());
        findViewById(R.id.btn11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.offsetTopAndBottom(100);
                // this will not trigger layout / measure ...
                // this may violate viewgroup's layout logic


                //  if you call request layout, offsetting position might be reset ...
//                vv.requestLayout();  // this will make offsetTopAndBottom call useless ...

                // why ???
                //   offsetTopAndBottom will change  mTop/mBottom values
                //   requestLayout will change it back ....


            }
        });
    }
}