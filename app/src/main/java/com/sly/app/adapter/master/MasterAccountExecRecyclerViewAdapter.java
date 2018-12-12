package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterAccountExecBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterAccountExecRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<MasterAccountExecBean> list;

    public MasterAccountExecRecyclerViewAdapter(Context context, List<MasterAccountExecBean> noteList) {
        mContext = context;
        list = noteList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_master_account, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MasterAccountExecBean bean = list.get(position);

        viewHolder.tvChildAccountExec.setText(bean.getToMobile());

        viewHolder.tvMasterManageAccount.setTag(position);
        viewHolder.tvMasterManageAccount.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return list.size();
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

        @BindView(R.id.tv_master_child_accountexec)
        TextView tvChildAccountExec;
        @BindView(R.id.tv_master_manage_account)
        TextView tvMasterManageAccount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
