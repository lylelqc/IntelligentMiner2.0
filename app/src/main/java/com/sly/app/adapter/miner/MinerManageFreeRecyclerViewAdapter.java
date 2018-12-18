package com.sly.app.adapter.miner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.NoticeOldDetailsActivity;
import com.sly.app.model.miner.MinerManageFreeListBean;
import com.sly.app.model.miner.MinerRepairFreeListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerManageFreeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MinerManageFreeListBean> list;
    private String Model;

    public MinerManageFreeRecyclerViewAdapter(Context Context, List<MinerManageFreeListBean> mResultList, String model) {
        mContext = Context;
        list = mResultList;
        Model = model;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_free_manage, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MinerManageFreeListBean bean = list.get(position);

        viewHolder.tvManageFreeMoney.setText(bean.getMine28_Sum()+"");
        viewHolder.tvManageFreeModel.setText(Model);
        viewHolder.tvManageFreeDate.setText(bean.getMine28_GenerateTime()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_miner_manage_free_money)
        TextView tvManageFreeMoney;
        @BindView(R.id.tv_miner_free_model)
        TextView tvManageFreeModel;
        @BindView(R.id.tv_manage_free_date)
        TextView tvManageFreeDate;
        @BindView(R.id.rl_miner_manage_free_details)
        RelativeLayout rlManageFreeDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
