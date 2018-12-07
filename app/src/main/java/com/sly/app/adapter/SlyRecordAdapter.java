package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.SlyRecordListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlyRecordAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<SlyRecordListBean> notes;

    public SlyRecordAdapter(Context Context, List<SlyRecordListBean> mResultList) {
        mContext = Context;
        notes = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_business, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final SlyRecordListBean bean = notes.get(position);
        String time = bean.getPay11_Time();
        String[] data = time.split(" ");
        viewHolder.tvData.setText(data[0]);
        viewHolder.tvTime.setText(data[1]);

        String formatSum = String.format("%.2f", Double.valueOf(bean.getPay11_Sum()));
        viewHolder.tvSum.setText(formatSum + "元");
        String pay11_note = bean.getPay11_Note();
        String[] remark;
        if (pay11_note.contains("(")) {
            remark = pay11_note.split("\\(");
        } else if(pay11_note.contains(",")){
            remark = pay11_note.split(",");
        }else{
            remark = pay11_note.split(":");
        }
        if (remark != null) {
            viewHolder.tvRemark.setText(remark[0]);
        } else {
            viewHolder.tvRemark.setText(pay11_note);
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
        @BindView(R.id.iv_status)
        ImageView ivStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}
