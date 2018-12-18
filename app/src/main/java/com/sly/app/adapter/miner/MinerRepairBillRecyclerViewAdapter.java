package com.sly.app.adapter.miner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.miner.MinerRepairDetailsActivity;
import com.sly.app.activity.yunw.repair.RepairBillDetailsActivity;
import com.sly.app.model.yunw.repair.RepairBillBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerRepairBillRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<RepairBillBean> list;
    private boolean isHistory;

    public MinerRepairBillRecyclerViewAdapter(Context Context, List<RepairBillBean> mResultList, boolean history) {
        mContext = Context;
        list = mResultList;
        isHistory = history;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_miner_repair_bill, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final RepairBillBean bean = list.get(position);
        viewHolder.tvRepairMachineType.setText(bean.getModel());
        viewHolder.tvBeginPTime.setText(bean.getPtime().substring(0, bean.getPtime().length()-3));
        viewHolder.tvRepairSum.setText(bean.getRepairSum()+"");
        viewHolder.tvRepairDescription.setText(bean.getInfo());

        final boolean ishistory = isHistory;
        viewHolder.rlDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("BillNo", bean.getBillNo());
                bundle.putBoolean("isHistory", ishistory);
                AppUtils.goActivity(mContext, MinerRepairDetailsActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_repair_machine_type)
        TextView tvRepairMachineType;
        @BindView(R.id.tv_begin_ptime)
        TextView tvBeginPTime;
        @BindView(R.id.tv_repair_sum)
        TextView tvRepairSum;
        @BindView(R.id.tv_repair_desciption)
        TextView tvRepairDescription;
        @BindView(R.id.rl_details)
        RelativeLayout rlDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
