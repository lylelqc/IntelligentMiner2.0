package com.sly.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.mine.MyMachineActivity;
import com.sly.app.activity.sly.MachineDetailActivity;
import com.sly.app.activity.sly.mine.FaultMachineActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.MasterMineBean;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineMachineAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MasterMineBean> notes;

    public MineMachineAdapter(Context context, List<MasterMineBean> noteList) {
        mContext = context;
        notes = noteList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_master_machine, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final MasterMineBean masterMachineBean = notes.get(position);

            GlideImgManager.glideLoader(mContext, NetWorkCons.IMG_URL,
                    R.drawable.device_list_images, R.drawable.device_list_images, viewHolder.ivHeadImg);
            viewHolder.tvMineName.setText(masterMachineBean.getMineName());
            viewHolder.tvMachineModel.setText(masterMachineBean.getModelname());
            double rate = masterMachineBean.getRunrate() * 100;
            viewHolder.tvOperationRate.setText(String.format("%.2f", rate) + "");
            viewHolder.tvMachineNum.setText(masterMachineBean.getAcount()+"");
            viewHolder.tvCalForcce.setText(masterMachineBean.getCalcFore()+ "");
            viewHolder.tvBadCount.setText(masterMachineBean.getFaultcount()+"");

            viewHolder.tvMachineList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mineCode", masterMachineBean.getMineCode());
                    bundle.putString("model", masterMachineBean.getModel());
                    CommonUtil2.goActivity(mContext, MyMachineActivity.class, bundle);
                }
            });
            viewHolder.tvBadList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mineCode", masterMachineBean.getMineCode());
                    bundle.putString("model", masterMachineBean.getModel());
                    CommonUtil2.goActivity(mContext, FaultMachineActivity.class, bundle);
                }
            });
            viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_head_img)
        ImageView ivHeadImg;
        @BindView(R.id.tv_mine_name_1)
        TextView tvMineName;
        @BindView(R.id.tv_machine_model)
        TextView tvMachineModel;
        @BindView(R.id.tv_operation_rate)
        TextView tvOperationRate;
        @BindView(R.id.tv_machine_num)
        TextView tvMachineNum;
        @BindView(R.id.tv_cal_force)
        TextView tvCalForcce;
        @BindView(R.id.tv_bad_count)
        TextView tvBadCount;
        @BindView(R.id.tv_machine_list)
        TextView tvMachineList;
        @BindView(R.id.tv_bad_list)
        TextView tvBadList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
