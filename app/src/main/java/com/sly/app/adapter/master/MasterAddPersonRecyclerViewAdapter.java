package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterPersonListBean;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterAddPersonRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private Context mContext;
    private List<MasterPersonListBean> list;
    private Set<Integer> indexSet;

    public MasterAddPersonRecyclerViewAdapter(Context Context, List<MasterPersonListBean> mResultList, Set<Integer> set) {
        mContext = Context;
        list = mResultList;
        indexSet = set;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_machine_offline, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MasterPersonListBean bean = list.get(position);

        viewHolder.tvMasterYunwName.setText(bean.getMineName());
        viewHolder.cbChoseItem.setTag(position);
        viewHolder.cbChoseItem.setChecked(indexSet.contains(position));
        viewHolder.cbChoseItem.setOnClickListener(this);
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

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cb_chose_item)
        CheckBox cbChoseItem;
        @BindView(R.id.tv_master_yunw_person_name)
        TextView tvMasterYunwName;
        @BindView(R.id.ll_master_chose_icon)
        LinearLayout llMasterChoseIcon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
