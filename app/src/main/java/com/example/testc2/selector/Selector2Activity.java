package com.example.testc2.selector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.testc2.R;

public class Selector2Activity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager layoutManager;
    int currentIndex = 0;

    private boolean canUpdateCurrentIndex(int delta){
        int newIndex = currentIndex + delta;
        if(newIndex>=5){
            return false;
        }
        if(newIndex<0){
            return false;
        }
        return true;
    }

    private boolean updateCurrentIndex(int delta){
        int newIndex = currentIndex + delta;
        if(newIndex>=5){
            return false;
        }
        if(newIndex<0){
            return false;
        }
        currentIndex = newIndex;
        return true;
    }

    int getDelta(int direction){
        // 0 -> 1
        if( currentIndex==0 && direction>0) {
            int delta = getDeltaInner();
            return delta;
        }
        // 1 -> 0
        if( currentIndex==1 && direction<0) {
            int delta = getDeltaInner();
            return delta;
        }

        // 3 -> 4
        if( currentIndex==3 && direction>0) {
            int delta = getDeltaInner();
            return delta;
        }

        // 4 -> 3
        if( currentIndex==4 && direction<0) {
            int delta = getDeltaInner();
            return delta;
        }

        // item view height
        return 400;
        
    }

    private int getDeltaInner() {
        // padding top height
        int paddingTopHeight = 40;

        // item view height
        int itemHeight = 400;

        // rv height
        int ScrollRegionHeight = 600;

        int itemOffset = (ScrollRegionHeight-itemHeight)/2;
        return paddingTopHeight + itemHeight - itemOffset;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector2);
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
        rv.setAdapter(new SelectorAdapter2());


        findViewById(R.id.upbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canUpdateCurrentIndex(-1)){
                    if(rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                        int dy = 0 - getDelta(-1);
                        setVhsSet(false);
                        monitor.setTotal(dy);
                        rv.smoothScrollBy(0,dy,null,1000);
                        updateCurrentIndex(-1);
                    }
                }
            }
        });
        findViewById(R.id.downbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canUpdateCurrentIndex(1)){
                    if(rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                        int dy = 0 + getDelta(1);
                        setVhsSet(false);
                        monitor.setTotal(dy);
                        rv.smoothScrollBy(0,dy,null,1000);
                        updateCurrentIndex(1);
                    }
                }
            }
        });



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
                    vh1 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first);
                    vh2 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first+1);
                    vh3 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first+2);
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



        rv.post(new Runnable() {
            @Override
            public void run() {
                initScales();
            }
        });

    }


    ScrollMonitor monitor;
    SelectorAdapter2.VH vh1,vh2,vh3;
    boolean vhsSet = false;
    public void setVhsSet(boolean vhsSet) {
        this.vhsSet = vhsSet;
    }
    public boolean isVhsSet() {
        return vhsSet;
    }

    private void initScales(){
        int first = layoutManager.findFirstVisibleItemPosition();
        SelectorAdapter2.VH vh1 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first);
        SelectorAdapter2.VH vh2 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first+1);
        SelectorAdapter2.VH vh3 = (SelectorAdapter2.VH) rv.findViewHolderForLayoutPosition(first+2);
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



}
