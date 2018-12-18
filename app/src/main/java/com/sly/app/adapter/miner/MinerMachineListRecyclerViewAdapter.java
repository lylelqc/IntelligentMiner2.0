package com.sly.app.adapter.miner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.miner.MinerMachineDetailsActivity;
import com.sly.app.model.miner.MinerMachineListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMachineListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MinerMachineListBean> list;

    public MinerMachineListRecyclerViewAdapter(Context Context, List<MinerMachineListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_miner_machine_list, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MinerMachineListBean bean = list.get(position);
        viewHolder.tvMachineListCode.setText(bean.getMachineCode());
        if(bean.getStatusName().contains("在线")){
            viewHolder.tvMachineListState.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvMachineListState.setText(bean.getStatusName());
        }else if(bean.getStatusName().contains("离线")){
            viewHolder.tvMachineListState.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvMachineListState.setText(bean.getStatusName().substring(0,2));
        }else if(bean.getStatusName().contains("算力异常")){
            viewHolder.tvMachineListState.setTextColor(mContext.getResources().getColor(R.color.color_f6a800));
            viewHolder.tvMachineListState.setText(bean.getStatusName());
        }else{
            viewHolder.tvMachineListState.setTextColor(mContext.getResources().getColor(R.color.color_777777));
            viewHolder.tvMachineListState.setText(bean.getStatusName());
        }
        viewHolder.tvMachineListModel.setText(bean.getModel());
        viewHolder.tvMachineListCalc.setText(String.format("%.2f", bean.getNowCal())+"T");
        viewHolder.llMachineListDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("machineSysCode", bean.getMachineSysCode());
                bundle.putString("MineCode", bean.getMineCode());
                AppUtils.goActivity(mContext, MinerMachineDetailsActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mahcine_list_code)
        TextView tvMachineListCode;
        @BindView(R.id.tv_machine_list_model)
        TextView tvMachineListModel;
        @BindView(R.id.tv_machine_list_state)
        TextView tvMachineListState;
        @BindView(R.id.tv_machine_list_calc)
        TextView tvMachineListCalc;
        @BindView(R.id.ll_machine_list_details)
        LinearLayout llMachineListDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
