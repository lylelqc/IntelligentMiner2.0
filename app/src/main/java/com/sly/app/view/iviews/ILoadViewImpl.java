package com.sly.app.view.iviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.Contants;
import com.sly.app.listener.LoadMoreClickListener;


/**
 */
public class ILoadViewImpl implements ILoadView, View.OnClickListener{

    public Context mContext;

    public LoadMoreClickListener mClickListener;

    public View rootView;

    public ILoadViewImpl(Context context, LoadMoreClickListener clickListener){
        mContext = context;
        mClickListener = clickListener;

        rootView = inflate();
    }

    @Override
    public View inflate() {
        return LayoutInflater.from(mContext).inflate(R.layout.load_more_item, null);
    }

    @Override
    public void showLoadingView(View parentView) {
        ProgressBar progressBar = parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.VISIBLE);
        loadingTv.setText(Contants.LoadView.LOADING);

        parentView.setClickable(false);
    }

    @Override
    public void showErrorView(View parentView) {
        ProgressBar progressBar = parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.GONE);
        loadingTv.setText(Contants.LoadView.CLICKLOAD);

        parentView.setOnClickListener(this);
    }

    @Override
    public void showFinishView(View parentView) {
        ProgressBar progressBar = parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.GONE);
        loadingTv.setText("没数据了！");

    }

    @Override
    public void onClick(View v) {
        mClickListener.clickLoadMoreData();
    }
}
