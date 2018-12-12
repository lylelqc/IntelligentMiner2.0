package com.sly.app.adapter.master;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.model.yunw.repair.RepairSparesBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterSpareCheckRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RepairSparesBean> beanList;
    private int index = -1;//标记当前选择的选项

    public MasterSpareCheckRecyclerViewAdapter(Context context, List<RepairSparesBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_machine_type, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final RepairSparesBean bean = beanList.get(position);

        viewHolder.rbMachineTypeStatus.setText(bean.getPartName());
        viewHolder.rbMachineTypeStatus.setChecked(position == index);
        viewHolder.rbMachineTypeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = -1;
                if(((CheckBox)view).isChecked()){
                    index = position;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rb_machine_type_status)
        CheckBox rbMachineTypeStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndexNull() {
        index = -1;
    }
}
