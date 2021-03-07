package com.example.testc2.selector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testc2.R;

import java.util.List;

public class SelectorAdapter2 extends RecyclerView.Adapter<SelectorAdapter2.VH> {

    List<DataBean> data;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH vh;
        if(viewType == View_TYPE_Content){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selector_item2, parent, false);
            vh = new VH(v);
            vh.viewType = View_TYPE_Content;
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selector_item2p, parent, false);
            vh = new VH(v);
            vh.viewType = View_TYPE_Padding;
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tv.setText("item:"+(position-1) );
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    int View_TYPE_Padding = 1;
    int View_TYPE_Content = 2;


    @Override
    public int getItemViewType(int position) {
        if( position == 0 || position == 6 ){
            return View_TYPE_Padding;
        }
        return View_TYPE_Content;
    }

    public static class VH extends RecyclerView.ViewHolder{
        int viewType;
        ImageView iv;
        TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv1);
            tv = itemView.findViewById(R.id.tv1);
        }

        public void setScale(float f){
            if(iv==null){
                return;
            }
            iv.setScaleX(f);
            iv.setScaleY(f);
        }
    }

    public static class ScrollMonitor {

    }

    public static class DataBean {
        String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

}
