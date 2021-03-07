package com.example.testc2.selector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.testc2.R;

public class SelectorActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager layoutManager;
    ScrollMonitor monitor;


    SelectorAdapter.VH vh1,vh2,vh3;
    boolean vhsSet = false;
    public void setVhsSet(boolean vhsSet) {
        this.vhsSet = vhsSet;
    }
    public boolean isVhsSet() {
        return vhsSet;
    }

    private void initScales(){
        int first = layoutManager.findFirstVisibleItemPosition();
        SelectorAdapter.VH vh1 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first);
        SelectorAdapter.VH vh2 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first+1);
        SelectorAdapter.VH vh3 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first+2);
        if(vh1!=null){
            vh1.setScale(monitor.getMIN_RATIO());
        }
        if(vh2!=null) {
            vh2.setScale(monitor.getMAX_RATIO());
        }
        if(vh3!=null) {
            vh3.setScale(monitor.getMIN_RATIO());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        rv  = findViewById(R.id.rrr1);


        monitor = new ScrollMonitor();

        layoutManager = new LinearLayoutManager(this){
            /**
             * {@link RecyclerView#SCROLL_STATE_IDLE }
             *
             *
             * @param state
             */
            @Override
            public void onScrollStateChanged(int state) {
                Log.i("gaga", "--- onScrollStateChanged  --- state:" +state);
            }
        };
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new SelectorAdapter());

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(monitor.isStarted()){
                    monitor.onStep(dy);
                } else {
                    return;
                }

                int first = layoutManager.findFirstVisibleItemPosition();
                int firstComplete = layoutManager.findFirstCompletelyVisibleItemPosition();

                Log.i("gaga", "--- onScrolled  --- first:" + first+"    firstComplete:"+firstComplete);

                //
                if(!isVhsSet()){
                    vh1 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first);
                    vh2 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first+1);
                    vh3 = (SelectorAdapter.VH) rv.findViewHolderForLayoutPosition(first+2);
                    setVhsSet(true);
                }

                if(dy>0){
                    if(vh1!=null){
                        vh1.setScale(monitor.getMIN_RATIO());
                    }
                    if(vh2!=null) {
                        vh2.setScale(monitor.getContractRatio());
                    }
                    if(vh3!=null) {
                        vh3.setScale(monitor.getExpandRatio());
                    }
                }
                if(dy<0){
                    if(vh1!=null){
                        vh1.setScale(monitor.getExpandRatio());
                    }
                    if(vh2!=null) {
                        vh2.setScale(monitor.getContractRatio());
                    }
                    if(vh3!=null) {
                        vh3.setScale(monitor.getMIN_RATIO());
                    }
                }


            }
        });




        findViewById(R.id.upbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                    Log.i("gaga", "--- start scroll  --- dy:-400" );
                    setVhsSet(false);
                    monitor.setTotal(-400);
                    rv.smoothScrollBy(0,-400,null,1000);
                }

            }
        });
        findViewById(R.id.downbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                    Log.i("gaga", "--- start scroll  --- dy:400" );
                    setVhsSet(false);
                    monitor.setTotal(400);
                    rv.smoothScrollBy(0,400,null,1000);
                }
            }
        });

        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.scrollBy(0,300 + 400*50);
                initScales();
            }
        });

    }
}
