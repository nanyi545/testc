package com.ww.performancechore.recy_scroll;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.performancechore.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;


/**
 *
 *
 * https://stackoverflow.com/questions/40299494/focus-lost-on-refresh-in-recyclerview
 *
 * notifyDataSetChanged();   ---> lost focus
 *
 *
 */
public class Adapter1 extends RecyclerView.Adapter<Adapter1.VH> {

    static class Data {
        String str;
        public Data(String str) {
            this.str = str;
        }
    }

    static List<Data> getData(){
        List<Data> arr = new ArrayList<>();
        for (int i=0;i<300;i++){
            arr.add(new Data("item:"+i));
        }
        return arr;
    }


    List<Data> data = getData();


    RecyclerView rv;
    public Adapter1(RecyclerView rv) {
        setHasStableIds(true);
        this.rv = rv;
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if(newState==SCROLL_STATE_IDLE){
                    rv.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    @Override public long getItemId(int position) { return position; }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VH.TPYE_1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item2, parent, false);
            return new VH1(view);
        }
        if(viewType==VH.TPYE_3){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item3, parent, false);
            return new VH3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        long t1 = System.currentTimeMillis();
        final Data item = data.get(position);
        if(holder instanceof VH1){
            VH1 cast1 = (VH1) holder;
            cast1.setup1(item);
        }
        if(holder instanceof VH3){
            VH3 cast1 = (VH3) holder;
            cast1.setupMock(item, Adapter1.this);
            // *** use this to increase list FPS during scroll ....
            if(rv.getScrollState()!=SCROLL_STATE_IDLE){
                return;
            }
            // 模拟较长时间.....
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cast1.setup1(item, Adapter1.this);
        }
        long t2 = System.currentTimeMillis();
        Log.d("aaadapter","onbind:"+(t2-t1)+"  position:"+position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return VH.TPYE_1;
        return VH.TPYE_3;
    }



    Data currentF = null;




    static class VH1 extends VH {
        TextView tv1;
        public VH1(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.btn1);
        }
        private void setup1(Data data){
            VH1 holder = this;
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        holder.tv1.setTextColor(Color.RED);
                    }else {
                        holder.tv1.setTextColor(Color.BLUE);
                    }
                }
            });
            holder.tv1.setText(data.str);
            if(holder.itemView.isFocused()){
                holder.tv1.setTextColor(Color.RED);
            } else {
                holder.tv1.setTextColor(Color.BLUE);
            }
        }
    }


    static class VH3 extends VH {

        TextView channelTitle;
        ImageView video;
        ImageView focusBg, focusSubRightBg, focusBg2, playingIcon, focusIcon;
        TextView watching;
        float[] cornersFocus, cornersNonFocus;


        private boolean isItemFocusing(Data data, Adapter1 adapter) {
            if (adapter.currentF == null) {
                return false;
            }
            if (data.str.equalsIgnoreCase(adapter.currentF.str)) {
                return true;
            }
            return false;
        }


        public VH3(@NonNull View itemView) {
            super(itemView);
            channelTitle = (TextView) itemView.findViewById(R.id.title);
            video = (ImageView) itemView.findViewById(R.id.video_img);
            watching = (TextView) itemView.findViewById(R.id.watching_count);
            focusBg = (ImageView) itemView.findViewById(R.id.focus_bg);
            focusSubRightBg = (ImageView) itemView.findViewById(R.id.focus_right_sub_bg);
            playingIcon = (ImageView) itemView.findViewById(R.id.playing_icon);
        }
        private void setupMock(Data data, Adapter1 adapter){
            VH3 holder = this;
            holder.channelTitle.setText("");
        }
        private void setup1(Data data, Adapter1 adapter){
            VH3 holder = this;

            holder.channelTitle.setTextColor(ContextCompat.getColor(holder.channelTitle.getContext(), R.color.colorPrimary));
            holder.watching.setTextColor(ContextCompat.getColor(holder.channelTitle.getContext(), R.color.colorAccent));
            holder.channelTitle.setText(data.str);

            holder.channelTitle.setAlpha(1f);
            holder.watching.setAlpha(1f);


            boolean focusing = isItemFocusing(data, adapter);
//            boolean focusing = itemView.isFocused();
            holder.showFocusBg(focusing);
            holder.showDisplayingIcon(focusing);


            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("aaadapter1","onFocusChange  hasFocus:"+hasFocus+"   data:"+data.str);
                    if (hasFocus) {
                        adapter.currentF = data;
//                        holder.showFocusBg(true);
                    } else {
                        adapter.currentF = null;
//                        holder.showFocusBg(false);
                    }
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });

        }


        void showFocusBg(boolean show) {
            if (show) {
                focusBg.setVisibility(View.VISIBLE);
                focusSubRightBg.setVisibility(View.VISIBLE);
            } else {
                focusBg.setVisibility(View.INVISIBLE);
                focusSubRightBg.setVisibility(View.INVISIBLE);
            }
        }

        void showDisplayingIcon(boolean show) {
            if (show) {
                playingIcon.setVisibility(View.VISIBLE);
            } else {
                playingIcon.setVisibility(View.INVISIBLE);
            }
        }



    }


    static class VH extends RecyclerView.ViewHolder {

        static int TPYE_1 = 11;   // VH1 ......
        static int TPYE_3 = 13;   // VH3 ......

        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
