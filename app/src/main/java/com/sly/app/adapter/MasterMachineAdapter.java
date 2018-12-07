package com.sly.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.MyMachineBean;
import com.sly.app.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterMachineAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private Context mContext;
    private List<MyMachineBean> notes;

    public MasterMachineAdapter(Context context, List<MyMachineBean> noteList) {
        mContext = context;
        notes = noteList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.adapter_my_machine_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final MyMachineBean myMachineBean = notes.get(position);

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

            viewHolder.llItemRapare.setTag(position);
            viewHolder.llItemRapare.setOnClickListener(this);
            viewHolder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
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
