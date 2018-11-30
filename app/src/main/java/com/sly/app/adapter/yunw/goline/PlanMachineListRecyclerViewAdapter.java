package com.sly.app.adapter.yunw.goline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.goline.GolinePlanListBean;
import com.sly.app.model.yunw.goline.PlanMachineListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanMachineListRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<PlanMachineListBean> beanList;
    // 选中子item位置
    private int index = -1;
    private boolean isCheckBox;

    public PlanMachineListRecyclerViewAdapter(Context context, List<PlanMachineListBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_plan_machine_list, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final PlanMachineListBean bean = beanList.get(position);

        if (isCheckBox) {
            viewHolder.rlPlanMachineCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlPlanMachineCheckBox.setVisibility(View.GONE);
        }
        viewHolder.cbPlanMachineChoseItem.setChecked(index == position);
        viewHolder.cbPlanMachineChoseItem.setTag(position);
        viewHolder.cbPlanMachineChoseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    index = position;
                }else {
                    index = -1;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (int) view.getTag());
                }
            }
        });

        viewHolder.tvPlanMachineType.setText(bean.getModel());
        viewHolder.tvPlanMachineCount.setText(bean.getScanCount() + "");
        viewHolder.tvPlanMachineMinerCode.setText(bean.getWorker());
        viewHolder.tvPlanMachineVipCode.setText(
                AppUtils.isBlank(bean.getOwner()) ?
                        mContext.getResources().getString(R.string.machine_no_bind) : bean.getOwner());

        viewHolder.llPlanMachineDetails.setTag(position);
        viewHolder.llPlanMachineDetails.setOnClickListener(this);
        viewHolder.llPlanMachineDetails.setBackgroundColor(index == position ?
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

        @BindView(R.id.rl_plan_machine_checkbox)
        RelativeLayout rlPlanMachineCheckBox;
        @BindView(R.id.cb_plan_machine_chose_item)
        CheckBox cbPlanMachineChoseItem;

        @BindView(R.id.tv_plan_mahcine_type)
        TextView tvPlanMachineType;
        @BindView(R.id.tv_plan_mahcine_count)
        TextView tvPlanMachineCount;
        @BindView(R.id.tv_plan_mahcine_miner_code)
        TextView tvPlanMachineMinerCode;
        @BindView(R.id.tv_plan_mahcine_vip_code)
        TextView tvPlanMachineVipCode;
        @BindView(R.id.ll_plan_machine_details)
        LinearLayout llPlanMachineDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }

    public void setCheckBox(boolean checkBox) {
        isCheckBox = checkBox;
        notifyDataSetChanged();
    }
}
