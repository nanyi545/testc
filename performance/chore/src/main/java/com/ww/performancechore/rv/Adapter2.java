package com.ww.performancechore.rv;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ww.performancechore.R;
import com.ww.performancechore.rv.rv.RecyclerView;
import com.ww.performancechore.rv.util.Logger;
import com.ww.performancechore.rv.util.VG3;

import org.jetbrains.annotations.NotNull;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.VH> {

    RecyclerView.LayoutParams p1  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,300);
    RecyclerView.LayoutParams p2  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,400);
    RecyclerView.LayoutParams p3  = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);

    RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,300);
    RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,400);


    int active=0;

    Handler h = new Handler();

    public void add(){
        int copy = active;
        active++;
        h.post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(copy);
                notifyItemChanged(active);
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        VG3 vg  = (VG3) LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_,parent,false);
        vg.setKey(VG3.generteKey());

//        VG3 vg = new VG3(parent.getContext());
//        vg.setLayoutParams(p3);   // why this will cause collapse .....

        Logger.log(Logger.ADAPTER2_TAG,"   k"+vg.getKey()+" w:"+vg.getLayoutParams().width+"  h:"+vg.getLayoutParams().height);

        return new VH(vg);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        Logger.log(Logger.ADAPTER2_TAG,"   position"+position);

        if(active==position){
//            holder.tv.setLayoutParams(p5);
            holder.tv.setText("**** item index:"+position+"  holder key:"+holder.vg.getKey());
        } else{
//            holder.tv.setLayoutParams(p4);
            holder.tv.setText("item index:"+position+"  holder key:"+holder.vg.getKey());
        }
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    static class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull @NotNull View itemView) {
            super(itemView);
            vg = (VG3) itemView;
            tv = itemView.findViewById(R.id.my_tv1);
        }
        VG3 vg;
        TextView tv;
    }

    private static final String TAG = "Adapter2Tag";
}
