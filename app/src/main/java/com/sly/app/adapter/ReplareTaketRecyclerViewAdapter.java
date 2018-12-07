package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.mine.TaketDetailActivity;
import com.sly.app.adapter.viewHolder.LoadMoreViewHolder;
import com.sly.app.model.ReplaceListBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ReplareTaketRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private final LayoutInflater mLayoutInflater;
    private List<ReplaceListBean> beanList;

    public ReplareTaketRecyclerViewAdapter(Context context, List<ReplaceListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_replare_taket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ReplaceListBean gcBean = beanList.get(position);
        ViewHolder eholder = (ViewHolder) holder;
        eholder.mRepairID.setText(gcBean.getBillNo());
        eholder.mStatus.setText(gcBean.getBillStatus());
        if ("未处理".equals(gcBean.getBillStatus())) {
            eholder.mStatus.setTextColor(context.getResources().getColor(R.color.app_text_blue2));
        } else {
            eholder.mStatus.setTextColor(Color.parseColor("#828a8c"));
        }
        eholder.mTime.setText(gcBean.getBillTime());
        eholder.mDesciption.setText(gcBean.getReason());
        eholder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaketDetailActivity.class);
                intent.putExtra("bean", gcBean);
                context.startActivity(intent);
            }
        });
        //将position保存在itemView的Tag中，以便点击时进行获取
        eholder.itemView.setTag(position);
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


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mRepairID;
        TextView mStatus;
        TextView mTime;
        TextView mDesciption;
        RelativeLayout mRlItem;

        public ViewHolder(View view) {
            super(view);
            mRepairID = (TextView) view.findViewById(R.id.tv_repair_id);
            mStatus = (TextView) view.findViewById(R.id.tv_repair_status);
            mTime = (TextView) view.findViewById(R.id.tv_begin_time);
            mDesciption = (TextView) view.findViewById(R.id.tv_question_desciption);
            mRlItem = (RelativeLayout) view.findViewById(R.id.rl_details);
        }
    }
}
