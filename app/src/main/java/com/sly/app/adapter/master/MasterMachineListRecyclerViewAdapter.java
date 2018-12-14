package com.sly.app.adapter.master;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.yunw.machine.MachineDetailsActivity;
import com.sly.app.model.master.MasterMachineListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterMachineListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MasterMachineListBean> list;

    public MasterMachineListRecyclerViewAdapter(Context Context, List<MasterMachineListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_sly2_machine_list, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MasterMachineListBean bean = list.get(position);
        viewHolder.tvMachineListIP.setText(bean.getWorker());
        if(bean.getStatusName().contains("在线")){
            viewHolder.tvMachineListStatus.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvMachineListStatus.setText(bean.getStatusName());
        }else if(bean.getStatusName().contains("离线")){
            viewHolder.tvMachineListStatus.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvMachineListStatus.setText(bean.getStatusName().substring(0,2));
        }else if(bean.getStatusName().contains("算力异常")){
            viewHolder.tvMachineListStatus.setTextColor(mContext.getResources().getColor(R.color.color_f6a800));
            viewHolder.tvMachineListStatus.setText(bean.getStatusName());
        }else{
            viewHolder.tvMachineListStatus.setTextColor(mContext.getResources().getColor(R.color.color_777777));
            viewHolder.tvMachineListStatus.setText(bean.getStatusName());
        }
        viewHolder.tvMachineListSuanli.setText(String.format("%.2f",bean.getNowCal())+"T");
        viewHolder.tvMachineListArea.setText(bean.getModel());
        viewHolder.llMachineListDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("machineSysCode", bean.getMachineSysCode());
                bundle.putBoolean("isMaster", true);
                AppUtils.goActivity(mContext, MachineDetailsActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_machine_list_ip)
        TextView tvMachineListIP;
        @BindView(R.id.tv_machine_list_status)
        TextView tvMachineListStatus;
        @BindView(R.id.tv_machine_list_suanli)
        TextView tvMachineListSuanli;
        @BindView(R.id.tv_machine_list_area)
        TextView tvMachineListArea;
        @BindView(R.id.ll_machine_list_details)
        LinearLayout llMachineListDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
