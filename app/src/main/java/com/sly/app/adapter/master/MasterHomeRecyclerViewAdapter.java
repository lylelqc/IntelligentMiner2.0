package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterMineBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterHomeRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private final LayoutInflater mLayoutInflater;
    private List<MasterMineBean> beanList;
    private int index = 0;

    public MasterHomeRecyclerViewAdapter(Context context, List<MasterMineBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_master_mine, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MasterMineBean bean = beanList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvMasterMiner.setText(bean.getMineName());
        viewHolder.tvMasterMiner.setOnClickListener(this);
        viewHolder.tvMasterMiner.setTextColor(position == index ?
                context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.sly_text_d6e7ff));
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
        @BindView(R.id.tv_master_mine)
        TextView tvMasterMiner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
