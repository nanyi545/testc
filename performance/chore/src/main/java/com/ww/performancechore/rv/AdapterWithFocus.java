package com.ww.performancechore.rv;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ww.performancechore.R;
import com.ww.performancechore.rv.rv.LinearLayoutManager;
import com.ww.performancechore.rv.rv.RecyclerView;
import com.ww.performancechore.rv.util.Logger;
import com.ww.performancechore.rv.util.VG3;

import org.jetbrains.annotations.NotNull;

public class AdapterWithFocus extends RecyclerView.Adapter<AdapterWithFocus.VH> {

    RecyclerView.LayoutParams p1  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,300);
    RecyclerView.LayoutParams p2  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,400);
    RecyclerView.LayoutParams p3  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);

    RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,300);
    RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,400);

    int active=0;

    RecyclerView rv;
    LinearLayoutManager llm;

    public void test1(){
    }

    public AdapterWithFocus(RecyclerView rv, LinearLayoutManager llm) {
        this.rv = rv;
        this.llm = llm;
        this.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState!=RecyclerView.SCROLL_STATE_IDLE){
                    return;
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int e = llm.getChildCount();
                            for(int i=0;i<e;i++){
                                View v = llm.getChildAt(i);
                                if(v==null){
                                    continue;
                                }
                                RecyclerView.ViewHolder vh = rv.getChildViewHolder(v);
                                if(vh!=null && vh instanceof VH){
                                    VH cast = (VH) vh;
                                    cast.loadImgIfNotLoaded();
                                } else {
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        VG3 vg  = (VG3) LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_focus,parent,false);
        vg.setKey(VG3.generteKey());

//        VG3 vg = new VG3(parent.getContext());
//        vg.setLayoutParams(p3);   // why this will cause collapse .....

        Logger.log(Logger.ADAPTER2_TAG,"   k"+vg.getKey()+" w:"+vg.getLayoutParams().width+"  h:"+vg.getLayoutParams().height);

        return new VH(vg);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        Logger.log(Logger.ADAPTER2_TAG,"   position"+position);
        holder.dataPos = position;

        if(active==position){
//            holder.tv.setLayoutParams(p5);
            holder.tv.setText("**** item index:"+position+"  holder key:"+holder.vg.getKey());
        } else{
//            holder.tv.setLayoutParams(p4);
            holder.tv.setText("item index:"+position+"  holder key:"+holder.vg.getKey());
        }


        holder.iv.setText("loading");

        holder.setImgUrl(VH.getImgUlr(position));

        if(rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
            holder.setImgState(VH.IMG_STARTED);
            holder.loadImgAsync(position);
        } else {
            holder.setImgState(VH.IMG_NOT_LOAD);
        }


    }



    @Override
    public int getItemCount() {
        return 1000;
    }

    static class VH extends RecyclerView.ViewHolder {

        private static String getImgUlr(int index){
            return "url:"+index+".png";
        }

        private static int generateRandomDelay(){
            int t = (int) (1000 + Math.random() * 2000);
            return t;
        }


        // index of data in the list
        int dataPos;

        public VH(@NonNull @NotNull View itemView) {
            super(itemView);
            vg = (VG3) itemView;
            tv = itemView.findViewById(R.id.my_tv1);
            iv = itemView.findViewById(R.id.my_iv1);
            tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        vg.setBackgroundColor(Color.RED);
                    } else {
                        vg.setBackgroundColor(Color.WHITE);
                    }
                }
            });
        }
        VG3 vg;
        Button tv;
        TextView iv;

        public void loadImgIfNotLoaded(){
            Logger.log(Logger.IMG_LOAD_TAG," loadImgIfNotLoaded  pos:"+dataPos);
            String expectedUrl = getImgUlr(dataPos);
            String gotUrl = imgUrl;
            // img already loaded....  do nothing ...
            if(expectedUrl.equals(gotUrl) && (imgState == IMG_LOADED)){
                Logger.log(Logger.IMG_LOAD_TAG," loadImgIfNotLoaded --- already loaded  pos:"+dataPos);
                return;
            }
            loadImgAsync(dataPos);
        }

        /**
         *  0 no state
         *  1 started
         *  2 img loaded
         */
        int imgState = IMG_NOT_LOAD;
        public static final int IMG_NOT_LOAD = 0;
        public static final int IMG_STARTED = 1;
        public static final int IMG_LOADED = 2;

        public void setImgState(int imgState) {
            this.imgState = imgState;
        }

        String imgUrl;

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void loadImgAsync(int position){
            Logger.log(Logger.IMG_LOAD_TAG,"start img loading  pos:"+position);
            iv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.log(Logger.IMG_LOAD_TAG," img loading  completed  pos:"+position);
                    iv.setText(VH.getImgUlr(position));
                    setImgState(VH.IMG_LOADED);
                }
            },VH.generateRandomDelay());
        }

    }

    private static final String TAG = "Adapter2Tag";
}
