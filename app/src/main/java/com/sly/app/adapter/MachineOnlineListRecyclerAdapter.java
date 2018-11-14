package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.yunw.CheckAreaActivity;
import com.sly.app.activity.sly.mine.yunw.MachineListActivity;
import com.sly.app.activity.sly.mine.yunw.ScanDetailsActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.MachineOnlineListBean;

import java.util.List;

import vip.devkit.library.Logcat;

public class MachineOnlineListRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private static final int EMPTY_VIEW = 5;

    private List<MachineOnlineListBean> beanList;
    private final LayoutInflater mLayoutInflater;

    public MachineOnlineListRecyclerAdapter(Context context, List<MachineOnlineListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_online_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder eholder = (ViewHolder) holder;

        if (beanList.size()!=0){
            final MachineOnlineListBean bean = beanList.get(position);
            eholder.mPlanID.setText(bean.getMine68_PlanID());
            eholder.mScanCount.setText(bean.getScanCount()+"");
            if(bean.isMine68_IsExecuted() && bean.getMine68_Executor() !=null && bean.getMine68_IsExecutedTime() != null){
                eholder.mExecute.setText("已执行");
                eholder.mExecuteTimel.setText("执行时间: ");
                eholder.mStartTime.setText(bean.getMine68_IsExecutedTime());
                if(eholder.mCheckArea.getVisibility() == View.GONE){
                    eholder.mCheckArea.setVisibility(View.VISIBLE);
                }
                eholder.mMachineList.setText("设备列表");
            }else{
                eholder.mExecute.setText("未执行");
                eholder.mStartTime.setText("");
                eholder.mExecuteTimel.setText("");
                eholder.mCheckArea.setVisibility(View.GONE);
                eholder.mMachineList.setText("查看区域");
            }
            eholder.mStatus.setText(bean.getOnlineStatus());

            eholder.mCheckArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logcat.e("CheckArea????================");
                    Intent intent = new Intent(context, CheckAreaActivity.class);
                    intent.putExtra("PlanID", bean.getMine68_PlanID());
                    context.startActivity(intent);
                }
            });
            eholder.mMachineList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logcat.e("MachineList????================");
                    Intent intent;
                    if(bean.isMine68_IsExecuted()){
                        intent = new Intent(context, MachineListActivity.class);
                        intent.putExtra("eventType", NetWorkCons.EventTags.GET_ONLINE_MACHINE_LIST + "");
                    }else{
                        intent = new Intent(context, CheckAreaActivity.class);
                    }
                    intent.putExtra("PlanID", bean.getMine68_PlanID());
                    context.startActivity(intent);
                }
            });
            eholder.mDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logcat.e("Details????================");
                    Intent intent = new Intent(context, ScanDetailsActivity.class);
                    intent.putExtra("PlanID", bean.getMine68_PlanID());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPlanID;
        TextView mCheckArea;
        TextView mMachineList;
        TextView mScanCount;
        TextView mExecute;
        TextView mStartTime;
        TextView mStatus;
        TextView mExecuteTimel;
        RelativeLayout mBindStatus;
        RelativeLayout mDetails;

        public ViewHolder(View view) {
            super(view);
            mPlanID = view.findViewById(R.id.tv_plan_id);
            mCheckArea = view.findViewById(R.id.tv_check_area);
            mMachineList = view.findViewById(R.id.tv_machine_list);
            mScanCount = view.findViewById(R.id.tv_scan_count);
            mExecute = view.findViewById(R.id.tv_execute);
            mStartTime = view.findViewById(R.id.tv_execute_time);
            mStatus = view.findViewById(R.id.tv_bind_status);
            mExecuteTimel = view.findViewById(R.id.tv_start_time);
            mBindStatus = view.findViewById(R.id.rl_status);
            mDetails = view.findViewById(R.id.rl_details);
        }
    }
}