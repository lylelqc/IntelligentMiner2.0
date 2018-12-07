package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.ScanDetailsBean;

import java.util.List;

public class ScanDetailsRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private static final int EMPTY_VIEW = 5;

    private List<ScanDetailsBean> beanList;
    private final LayoutInflater mLayoutInflater;

    public ScanDetailsRecyclerViewAdapter(Context context, List<ScanDetailsBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scan_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder eholder = (ViewHolder) holder;

        if (beanList.size()!=0){
            final ScanDetailsBean bean = beanList.get(position);
            eholder.mMachineCount.setText(bean.getScanCount()+"");
            eholder.mMinerCode.setText(bean.getWorker());
            String bindNum = bean.getOwner();
            if(bindNum != null && !TextUtils.isEmpty(bindNum)){
                eholder.mBindNum.setText(bean.getOwner());
                eholder.mBindBtn.setText("更改绑定");
            }else{
                eholder.mBindNum.setText("未绑定");
                eholder.mBindBtn.setText("绑定会员");
            }
            eholder.mBindBtn.setTag(position);
            eholder.mMachineListBtn.setTag(position);
            eholder.mBindBtn.setOnClickListener(this);
            eholder.mMachineListBtn.setOnClickListener(this);
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

    public void setOnItemClickListener(ScanDetailsRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    private ScanDetailsRecyclerViewAdapter.OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMachineCount;
        TextView mMinerCode;
        TextView mBindNum;
        TextView mBindBtn;
        TextView mMachineListBtn;

        public ViewHolder(View view) {
            super(view);
            mMachineCount = (TextView) view.findViewById(R.id.tv_machine_count);
            mMinerCode = (TextView) view.findViewById(R.id.tv_mine_number);
            mBindNum = (TextView) view.findViewById(R.id.tv_bind_number);
            mBindBtn = (TextView) view.findViewById(R.id.tv_bind_btn);
            mMachineListBtn = (TextView) view.findViewById(R.id.tv_machine_list_btn);
        }
    }
}
