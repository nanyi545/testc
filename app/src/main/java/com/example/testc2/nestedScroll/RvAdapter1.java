package com.example.testc2.nestedScroll;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapter1 extends RecyclerView.Adapter<RvAdapter1.VH> {

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        VH vh = new VH(tv);
        vh.setTv(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tv.setText("aaa:"+position+"  hex:"+Integer.toHexString(position));
        holder.tv.setBackgroundColor(Color.parseColor("#9999"+to2(Integer.toHexString(position))));
    }

    private String to2(String in){
        if(TextUtils.isEmpty(in)){
            return "00";
        }
        if(in.length()==1){
            return "0"+in;
        }
        if(in.length()==2){
            return in;
        }
        return "00";
    }


    @Override
    public int getItemCount() {
        return 255;
    }

    static class VH extends RecyclerView.ViewHolder{
        TextView tv;

        public void setTv(TextView tv) {
            this.tv = tv;
        }

        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
