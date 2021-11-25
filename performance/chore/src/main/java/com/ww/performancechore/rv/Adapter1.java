package com.ww.performancechore.rv;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ww.performancechore.rv.rv.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.VH> {


    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        return new VH(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        holder.tv.setText("hello rv"+position);
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    static class VH extends RecyclerView.ViewHolder {

        public VH(@NonNull @NotNull View itemView) {
            super(itemView);
            tv= (TextView) itemView;
        }
        TextView tv;
    }
}
