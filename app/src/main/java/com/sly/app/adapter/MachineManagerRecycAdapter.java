package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.MachineManageBean;

import java.util.List;

import vip.devkit.library.Logcat;

public class MachineManagerRecycAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<MachineManageBean> beanList;

    public MachineManagerRecycAdapter(Context context, List<MachineManageBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Logcat.e("MachineListRecyclerViewAdapter - " + beanList.toString());
        View view = LayoutInflater.from(context).inflate(R.layout.item_machine_manage, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(beanList.size() != 0){
            Logcat.e(beanList.size()+"");
            final MachineManageBean mmBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            eholder.mMachineID.setText(mmBean.getMachinecode());
            double rate = mmBean.getRunRate() * 100;
            if(rate == 100){
                eholder.mRunRate.setText("100");
            }else{
                eholder.mRunRate.setText(String.format("%.2f", rate));
            }

            if(mmBean.getScode().equals("00")){
                eholder.mRunStatus.setBackground(context.getResources().getDrawable(R.drawable.shape_status_green));
            }else if(mmBean.getScode().equals("02")){
                eholder.mRunStatus.setBackground(context.getResources().getDrawable(R.drawable.shape_status_yellow));
            }else{
                eholder.mRunStatus.setBackground(context.getResources().getDrawable(R.drawable.shape_status_red));
            }
            eholder.mRunType.setText(mmBean.getModel());
            eholder.mIP.setText(mmBean.getIp());
            eholder.mRunTime.setText(mmBean.getRunTime());
            eholder.mMachineDetails.setTag(position);
            eholder.mMachineDetails.setOnClickListener(this);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMachineID;
        TextView mRunStatus;
        TextView mRunRate;
        TextView mRunType;
        TextView mIP;
        TextView mRunTime;
        RelativeLayout mMachineDetails;

        public ViewHolder(View view) {
            super(view);
            mMachineID = (TextView) view.findViewById(R.id.tv_machine_id);
            mRunStatus = (TextView) view.findViewById(R.id.iv_run_status);
            mRunRate = (TextView) view.findViewById(R.id.tv_run_rate);
            mRunType = (TextView) view.findViewById(R.id.tv_run_type);
//            mIP = (TextView) view.findViewById(R.id.tv_IP);
//            mRunTime = (TextView) view.findViewById(R.id.tv_run_time);
            mMachineDetails = (RelativeLayout) view.findViewById(R.id.rl_details);
        }
    }
}
