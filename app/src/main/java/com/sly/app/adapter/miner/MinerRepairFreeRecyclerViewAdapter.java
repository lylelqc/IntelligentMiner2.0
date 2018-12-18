package com.sly.app.adapter.miner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.miner.MinerFreeDetailsActivty;
import com.sly.app.activity.sly.mine.notice.NoticeOldDetailsActivity;
import com.sly.app.model.miner.MinerRepairFreeListBean;
import com.sly.app.model.notice.YunwNoticeOldListBean;
import com.sly.app.utils.AppUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerRepairFreeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MinerRepairFreeListBean> list;
    private String Model;

    public MinerRepairFreeRecyclerViewAdapter(Context Context, List<MinerRepairFreeListBean> mResultList, String model) {
        mContext = Context;
        list = mResultList;
        Model = model;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_maintenance_cost, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MinerRepairFreeListBean bean = list.get(position);

        if(bean.getMine25_RepairSum() ==  0){
            viewHolder.tvRepairIcon.setBackgroundDrawable(
                    mContext.getResources().getDrawable(R.drawable.noshebei_kuanggongkuang_icon));
        }else {
            viewHolder.tvRepairIcon.setBackgroundDrawable(
                    mContext.getResources().getDrawable(R.drawable.shebei_kuanggongkuang_icon));
        }
        viewHolder.tvRepairFreeDate.setText(bean.getMine24_Time());
        viewHolder.tvRepairFreeModel.setText(Model);
        viewHolder.tvRepairFreeMoney.setText(bean.getMine25_RepairSum()+"");
        viewHolder.rlRepairFreeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("BillNo", bean.getMine24_BillNo());
                AppUtils.goActivity(mContext, MinerFreeDetailsActivty.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_miner_repair_icon)
        TextView tvRepairIcon;
        @BindView(R.id.tv_miner_free_model)
        TextView tvRepairFreeModel;
        @BindView(R.id.tv_repair_free_date)
        TextView tvRepairFreeDate;
        @BindView(R.id.tv_miner_repair_free_money)
        TextView tvRepairFreeMoney;
        @BindView(R.id.rl_miner_repair_free_details)
        RelativeLayout rlRepairFreeDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
