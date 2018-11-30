package com.sly.app.adapter.yunw.changepool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.model.yunw.machine.MachineListBean;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineSetPoolRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineListBean> list;
    private Set<Integer> indexSet;

    public MachineSetPoolRecyclerViewAdapter(Context Context, List<MachineListBean> mResultList, Set<Integer> set) {
        mContext = Context;
        list = mResultList;
        indexSet = set;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_set_pool, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MachineListBean bean = list.get(position);

        viewHolder.tvMachineSetPoolIP.setText(bean.getIP());
        if(bean.getStatusCode().equals("00")){
            viewHolder.tvMachineSetPoolStatus.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvMachineSetPoolStatus.setText(bean.getStatusName());
        }else if(bean.getStatusCode().equals("01")){
            viewHolder.tvMachineSetPoolStatus.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvMachineSetPoolStatus.setText(bean.getStatusName().substring(0,2));
        }else if(bean.getStatusCode().equals("02")){
            viewHolder.tvMachineSetPoolStatus.setTextColor(mContext.getResources().getColor(R.color.color_f6a800));
            viewHolder.tvMachineSetPoolStatus.setText(bean.getStatusName());
        }else{
            viewHolder.tvMachineSetPoolStatus.setTextColor(mContext.getResources().getColor(R.color.color_777777));
            viewHolder.tvMachineSetPoolStatus.setText(bean.getStatusName());
        }
        viewHolder.tvMachineSetPoolVipcode.setText(bean.getMinerSysCode());
        viewHolder.tvMachineSetPoolArea.setText(bean.getAreaName());

        viewHolder.cbChoseItem.setChecked(indexSet.contains(position));
        viewHolder.cbChoseItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(!indexSet.contains(position)){
                        indexSet.add(position);
                    }
                }else{
                    if(indexSet.contains(position)){
                        indexSet.remove(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cb_chose_item)
        CheckBox cbChoseItem;
        @BindView(R.id.tv_nachine_set_pool_vipCode)
        TextView tvMachineSetPoolVipcode;
        @BindView(R.id.tv_machine_set_pool_ip)
        TextView tvMachineSetPoolIP;
        @BindView(R.id.tv_machine_set_pool_status)
        TextView tvMachineSetPoolStatus;
        @BindView(R.id.tv_machine_set_pool_area)
        TextView tvMachineSetPoolArea;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
