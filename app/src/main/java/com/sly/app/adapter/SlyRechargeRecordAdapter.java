package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.SlyRechargeListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlyRechargeRecordAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<SlyRechargeListBean> notes;

    public SlyRechargeRecordAdapter(Context Context, List<SlyRechargeListBean> mResultList) {
        mContext = Context;
        notes = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_recharge_record, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final SlyRechargeListBean bean = notes.get(position);
        String time = bean.getTimes();
        String[] data = time.split(" ");
        viewHolder.tvData.setText(data[0]);
        viewHolder.tvTime.setText(data[1]);

        String formatSum = String.format("%.2f", Double.valueOf(bean.getTruesum()));
        viewHolder.tvSum.setText(formatSum + "元");
        String pay11_note = bean.getInfo();
        String[] remark;
        if (pay11_note.contains("(")) {
            remark = pay11_note.split("\\(");
        } else if (pay11_note.contains(",")) {
            remark = pay11_note.split(",");
        } else {
            remark = pay11_note.split(":");
        }
        if (remark != null) {
            viewHolder.tvRemark.setText(remark[0]);
        } else {
            viewHolder.tvRemark.setText(pay11_note);
        }
        //Pass
        //Waitting
        //Refuse
        String statues = bean.getStatues();
        if ("Pass".equals(statues)){
            viewHolder.tvStatus.setText("审核通过");
        }else if ("Waitting".equals(statues)){
            viewHolder.tvStatus.setText("等待审核");
        }else if ("Refuse".equals(statues)){
            viewHolder.tvStatus.setText("审核驳回");
        }

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_sum)
        TextView tvSum;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}
