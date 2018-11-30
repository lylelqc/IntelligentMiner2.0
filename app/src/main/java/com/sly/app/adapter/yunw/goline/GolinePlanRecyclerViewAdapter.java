package com.sly.app.adapter.yunw.goline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.goline.GolinePlanListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GolinePlanRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<GolinePlanListBean> beanList;
    // 选中计划位置
    private int index = -1;

    public GolinePlanRecyclerViewAdapter(Context context, List<GolinePlanListBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_goline_plan, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final GolinePlanListBean bean = beanList.get(position);

        viewHolder.tvPlanID.setText(bean.getMine68_PlanID());
        String runTime = bean.getMine68_IsExecutedTime();
        if(runTime != null){
            viewHolder.tvPlanRunTime.setText(runTime.substring(0,runTime.length()-3));
        }else{
            viewHolder.tvPlanTime.setVisibility(View.GONE);
            viewHolder.tvPlanRunTime.setVisibility(View.GONE);
//            viewHolder.tvPlanRunTime.setText("2018-11-21 20:23");
        }
        viewHolder.tvPlanScanNum.setText(bean.getScanCount()+"");
        if(bean.isMine68_IsExecuted()){
            viewHolder.tvPlanScanStatus.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvPlanScanStatus.setText(mContext.getString(R.string.machine_scaned));
        }else{
            viewHolder.tvPlanScanStatus.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvPlanScanStatus.setText(mContext.getString(R.string.machine_no_scaned));
        }
        viewHolder.tvPlanOnlineStatus.setText(bean.getOnlineStatus());
        viewHolder.llPlanDetails.setTag(position);
        viewHolder.llPlanDetails.setOnClickListener(this);
        viewHolder.llPlanDetails.setBackgroundColor(index == position ?
                mContext.getResources().getColor(R.color.sly_bg_f2f7ff) : mContext.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return beanList.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_goline_plan_id)
        TextView tvPlanID;
        @BindView(R.id.tv_goline_plan_time)
        TextView tvPlanTime;
        @BindView(R.id.tv_goline_plan_run_time)
        TextView tvPlanRunTime;
        @BindView(R.id.tv_goline_plan_scan_num)
        TextView tvPlanScanNum;
        @BindView(R.id.tv_goline_plan_scan_status)
        TextView tvPlanScanStatus;
        @BindView(R.id.tv_goline_plan_online_status)
        TextView tvPlanOnlineStatus;
        @BindView(R.id.ll_goline_plan_details)
        LinearLayout llPlanDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setIndex(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }
}
