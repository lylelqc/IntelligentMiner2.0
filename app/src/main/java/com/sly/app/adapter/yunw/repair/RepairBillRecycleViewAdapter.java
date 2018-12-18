package com.sly.app.adapter.yunw.repair;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.yunw.repair.RepairBillDetailsActivity;
import com.sly.app.model.yunw.repair.RepairBillBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairBillRecycleViewAdapter extends RecyclerView.Adapter {

    private boolean isHistory;
    private Context mContext;
    private List<RepairBillBean> list;

    public RepairBillRecycleViewAdapter(Context Context, List<RepairBillBean> mResultList, boolean history) {
        mContext = Context;
        list = mResultList;
        isHistory = history;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_repair_bill, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final RepairBillBean bean = list.get(position);
        viewHolder.tvRepairMachineType.setText(bean.getModel());
        viewHolder.tvRepairArea.setText(bean.getAreaName());
        viewHolder.tvBeginPTime.setText(bean.getPtime().substring(0, bean.getPtime().length()-3));
        viewHolder.tvRepairIp.setText(bean.getIP());
        viewHolder.tvRepairDescription.setText(bean.getInfo());
        viewHolder.rlDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("BillNo", bean.getBillNo());
                bundle.putBoolean("isHistory", isHistory);
                AppUtils.goActivity(mContext, RepairBillDetailsActivity.class, bundle);
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
        @BindView(R.id.tv_repair_area)
        TextView tvRepairArea;
        @BindView(R.id.tv_begin_ptime)
        TextView tvBeginPTime;
        @BindView(R.id.tv_repair_ip)
        TextView tvRepairIp;
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
