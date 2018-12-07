package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.RepairDocBean;

import java.util.List;

import vip.devkit.library.Logcat;

public class RepairDocRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<RepairDocBean> beanList;

    public RepairDocRecyclerAdapter(Context context, List<RepairDocBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Logcat.e("MachineListRecyclerViewAdapter - " + beanList.toString());
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_doc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(beanList.size() != 0){
            Logcat.e(beanList.size()+"");
            final RepairDocBean rdBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            eholder.mRepairID.setText(rdBean.getBillno());
            eholder.mRepairStatus.setText(rdBean.getResultname());
            eholder.mRepairIP.setText(rdBean.getIp());
            eholder.mRepairArea.setText(rdBean.getAreaname());
            eholder.mRepairBegin.setText(rdBean.getPtime());
            if(rdBean.getResultname().equals("未处理")){
                eholder.mRepairEndTime.setVisibility(View.GONE);
                eholder.mRepairStatus.setTextColor(context.getResources().getColor(R.color.app_text_blue2));
            }else{
                eholder.mRepairEndTime.setVisibility(View.VISIBLE);
                eholder.mRepairEnd.setText(rdBean.getEndtime());
            }
            eholder.mQuestionDesciption.setText(rdBean.getInfo());
            eholder.mRepairDocDetails.setTag(position);
            eholder.mRepairDocDetails.setOnClickListener(this);
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
        private RelativeLayout mRepairDocDetails;
        private RelativeLayout mRepairEndTime;
        private TextView mRepairID;
        private TextView mRepairStatus;
        private TextView mRepairIP;
        private TextView mRepairArea;
        private TextView mRepairBegin;
        private TextView mRepairEnd;
        private TextView mQuestionDesciption;

        public ViewHolder(View view) {
            super(view);
            mRepairID = (TextView) view.findViewById(R.id.tv_repair_id);
            mRepairStatus = (TextView) view.findViewById(R.id.tv_repair_status);
            mRepairIP = (TextView) view.findViewById(R.id.tv_repair_IP);
            mRepairArea = (TextView) view.findViewById(R.id.tv_repair_area);
            mRepairBegin = (TextView) view.findViewById(R.id.tv_begin_time);
            mRepairEnd = (TextView) view.findViewById(R.id.tv_end_time);
            mQuestionDesciption = (TextView) view.findViewById(R.id.tv_question_desciption);
            mRepairDocDetails = (RelativeLayout) view.findViewById(R.id.rl_details);
            mRepairEndTime = (RelativeLayout) view.findViewById(R.id.rl_repair_end_time);
        }
    }
}
