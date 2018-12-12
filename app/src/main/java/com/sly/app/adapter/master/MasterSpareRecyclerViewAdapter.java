package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterMachineListBean;
import com.sly.app.model.master.MasterSpareListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterSpareRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MasterSpareListBean> list;

    public MasterSpareRecyclerViewAdapter(Context Context, List<MasterSpareListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_sly2_machine_list, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MasterSpareListBean bean = list.get(position);
        viewHolder.tvItemSpareName.setText(bean.getPartName());
        viewHolder.tvItemSpareNum.setText(bean.getUseCount());
        viewHolder.tvItemSpareDate.setText(bean.getTimes());
        viewHolder.tvItemSpareWorker.setText(bean.getWorker());
        viewHolder.tvItemSpareType.setText(bean.getModel());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_spare_name)
        TextView tvItemSpareName;
        @BindView(R.id.tv_item_spare_num)
        TextView tvItemSpareNum;
        @BindView(R.id.tv_item_spare_date)
        TextView tvItemSpareDate;
        @BindView(R.id.tv_item_spare_worker)
        TextView tvItemSpareWorker;
        @BindView(R.id.tv_item_spare_type)
        TextView tvItemSpareType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
