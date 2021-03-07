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

public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.VH> {

    List<DataBean> data;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selector_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tv.setText("item:"+position);
    }

    @Override
    public int getItemCount() {
        return 9999;
    }


    public static class VH extends RecyclerView.ViewHolder{
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
