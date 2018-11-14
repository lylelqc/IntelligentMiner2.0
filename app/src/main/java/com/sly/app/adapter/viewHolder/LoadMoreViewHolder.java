package com.sly.app.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.ReplareTaketRecyclerViewAdapter;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public LoadMoreViewHolder(View itemView) {
        super(itemView);
    }

    public LoadMoreViewHolder(View itemView, int flag) {
        super(itemView);
        ImageView mEmpty = itemView.findViewById(R.id.img_foot);
        ProgressBar mBar = itemView.findViewById(R.id.progressbar_moredata);
        TextView mLoading = itemView.findViewById(R.id.tip_text_layout);
        if (flag == 5) {
            mEmpty.setVisibility(View.VISIBLE);
            mBar.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        } else if(flag == 6){
            mEmpty.setVisibility(View.GONE);
            mBar.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
            mBar.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        }
    }
}
