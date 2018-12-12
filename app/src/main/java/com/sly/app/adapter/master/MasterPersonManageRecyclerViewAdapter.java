package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterPersonListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterPersonManageRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    private List<MasterPersonListBean> list;

    public MasterPersonManageRecyclerViewAdapter(Context Context, List<MasterPersonListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_master_person_manage, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MasterPersonListBean bean = list.get(position);

        viewHolder.tvPersonName.setText(bean.getMineName());
        if(bean.getGrade() == 2){
            viewHolder.tvPersonLeaderIcon.setVisibility(View.VISIBLE);
            viewHolder.tvPersonAddModify.setBackground(mContext.getResources().getDrawable(R.drawable.miners_increase));
        }else{
            viewHolder.tvPersonLeaderIcon.setVisibility(View.GONE);
            viewHolder.tvPersonAddModify.setBackground(mContext.getResources().getDrawable(R.drawable.miners_modify));
        }
        viewHolder.tvPersonAddModify.setTag(position);
        viewHolder.tvPersonAddModify.setOnClickListener(this);

        viewHolder.btnItemDelete.setTag(position);
        viewHolder.btnItemDelete.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return list.size();
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
        @BindView(R.id.tv_master_person_name)
        TextView tvPersonName;
        @BindView(R.id.tv_master_person_leader)
        TextView tvPersonLeaderIcon;
        @BindView(R.id.tv_master_person_add_modify)
        TextView tvPersonAddModify;
        @BindView(R.id.delete)
        Button btnItemDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
