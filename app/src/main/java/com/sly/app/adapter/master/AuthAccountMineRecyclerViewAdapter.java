package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.sly.app.R;
import com.sly.app.model.master.MasterMineBean;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthAccountMineRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<MasterMineBean> beanList;
    private Set<Integer> indexSet = new TreeSet<>();

    public AuthAccountMineRecyclerViewAdapter(Context context, List<MasterMineBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_goline_area, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final MasterMineBean bean = beanList.get(position);

        viewHolder.cbGolinePlan.setText(bean.getMineName());
        viewHolder.cbGolinePlan.setTag(position);
        viewHolder.cbGolinePlan.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
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

        @BindView(R.id.cb_goline_plan)
        CheckBox cbGolinePlan;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public Set<Integer> getAreaSet(){
        return indexSet;
    }

    public void clearAreaSet(){
        indexSet.clear();
    }
}
