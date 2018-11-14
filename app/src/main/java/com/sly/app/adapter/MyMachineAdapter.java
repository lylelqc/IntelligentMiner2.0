package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.MachineDetailActivity;
import com.sly.app.activity.sly.mine.MineMachineDetailActivity;
import com.sly.app.adapter.viewHolder.LoadMoreViewHolder;
import com.sly.app.model.sly.MyMachineBean;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vip.devkit.library.Logcat;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MyMachineAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MyMachineBean> notes;

    private static final int EMPTY_VIEW = 5;
    private static final int LOAD_MORE_VIEW = 6;

    private boolean isAddList = true;

    public MyMachineAdapter(Context context, List<MyMachineBean> noteList) {
        mContext = context;
        notes = noteList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.foot_view_fragment, parent, false);
            if (notes.size() > 14) {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, LOAD_MORE_VIEW);
                view.setVisibility(View.VISIBLE);
                return holder;
            } else if(notes.size() == 0){
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, 0);
                view.setVisibility(View.GONE);
                return holder;
            } else {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, EMPTY_VIEW);
                view.setVisibility(View.VISIBLE);
                return holder;
            }
        } else {
            View v = View.inflate(parent.getContext(), R.layout.adapter_my_machine_item, null);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            final MyMachineBean myMachineBean = notes.get(position);

            int runHours = ((Number) (Float.parseFloat(myMachineBean.getRunHours()))).intValue();
            String formatRunHours = String.format("%.2f", Double.valueOf(myMachineBean.getRunHours()));
            String statusName1 = myMachineBean.getStatusName();
            if(!CommonUtils.isBlank(statusName1)){
                if (statusName1.contains("异常") || statusName1.contains("离线")) {
                    viewHolder.tvCode.setTextColor(Color.parseColor("#f22a2a"));
                    viewHolder.tvPowerSum.setTextColor(Color.parseColor("#f22a2a"));
                    viewHolder.tvMachineName.setTextColor(Color.parseColor("#f22a2a"));
                    viewHolder.tvRunHours.setTextColor(Color.parseColor("#f22a2a"));
                    viewHolder.tvRunRate.setTextColor(Color.parseColor("#f22a2a"));
                    viewHolder.tvStatusName.setTextColor(Color.parseColor("#f22a2a"));
                } else {
                    viewHolder.tvCode.setTextColor(Color.parseColor("#666666"));
                    viewHolder.tvPowerSum.setTextColor(Color.parseColor("#666666"));
                    viewHolder.tvMachineName.setTextColor(Color.parseColor("#666666"));
                    viewHolder.tvRunHours.setTextColor(Color.parseColor("#666666"));
                    viewHolder.tvRunRate.setTextColor(Color.parseColor("#666666"));
                    viewHolder.tvStatusName.setTextColor(Color.parseColor("#666666"));
                }
            }

            viewHolder.tvCode.setText(myMachineBean.getMachineCode() + "");
            viewHolder.tvPowerSum.setText(String.format("%.2f", myMachineBean.getPowerSum()) + "元");
            viewHolder.tvMachineName.setText(myMachineBean.getMineName() + "");
            viewHolder.tvRunHours.setText(myMachineBean.getTimeShow() + "");
            viewHolder.tvRunRate.setText(String.format("%.2f", myMachineBean.getRunRate() * 100) + "%");

            String[] statusName = myMachineBean.getStatusName().split("\\(");
            viewHolder.tvStatusName.setText(statusName[0] + "");

            viewHolder.llItemRapare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("machineSysCod", myMachineBean.getMachineSysCode());
//                    CommonUtil2.goActivity(mContext, MachineDetailActivity.class, bundle);
                    Intent intent = new Intent(mContext, MineMachineDetailActivity.class);
                    intent.putExtra("machineSysCode", myMachineBean.getMachineSysCode());
                    intent.putExtra("mineCode", myMachineBean.getMineCode());
                    intent.putExtra("isMaster", "true");
                    mContext.startActivity(intent);
                }
            });
            viewHolder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
//        return notes.size();
        return notes.size() > 0 ? notes.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (notes.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void wtihNoListToSetting(boolean flag){
        isAddList = flag;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_code)
        TextView tvCode;
        @BindView(R.id.tv_machine_name)
        TextView tvMachineName;
        @BindView(R.id.tv_run_hours)
        TextView tvRunHours;
        @BindView(R.id.tv_run_rate)
        TextView tvRunRate;
        @BindView(R.id.tv_power_sum)
        TextView tvPowerSum;
        @BindView(R.id.tv_status_name)
        TextView tvStatusName;
        @BindView(R.id.ll_item_rapare)
        LinearLayout llItemRapare;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


}
