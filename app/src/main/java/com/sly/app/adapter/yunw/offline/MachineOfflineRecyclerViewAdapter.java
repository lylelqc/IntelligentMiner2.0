package com.sly.app.adapter.yunw.offline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineListBean;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineOfflineRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineListBean> list;
    private Set<Integer> indexSet;

    public MachineOfflineRecyclerViewAdapter(Context Context, List<MachineListBean> mResultList, Set<Integer> set) {
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
        final MachineListBean bean = list.get(position);

        viewHolder.tvMachineOfflineIP.setText(bean.getIP());
        if(bean.getStatusCode().equals("00")){
            viewHolder.tvMachineOfflineStatus.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvMachineOfflineStatus.setText(bean.getStatusName());
        }else if(bean.getStatusCode().equals("01")){
            viewHolder.tvMachineOfflineStatus.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvMachineOfflineStatus.setText(bean.getStatusName().substring(0,2));
        }else if(bean.getStatusCode().equals("02")){
            viewHolder.tvMachineOfflineStatus.setTextColor(mContext.getResources().getColor(R.color.color_f6a800));
            viewHolder.tvMachineOfflineStatus.setText(bean.getStatusName());
        }else{
            viewHolder.tvMachineOfflineStatus.setTextColor(mContext.getResources().getColor(R.color.color_777777));
            viewHolder.tvMachineOfflineStatus.setText(bean.getStatusName());
        }
        viewHolder.tvMachineOfflineType.setText(bean.getModel());
        viewHolder.tvMachineOfflineArea.setText(bean.getAreaName());

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
        @BindView(R.id.tv_machine_offline_ip)
        TextView tvMachineOfflineIP;
        @BindView(R.id.tv_machine_offline_type)
        TextView tvMachineOfflineType;
        @BindView(R.id.tv_machine_offline_status)
        TextView tvMachineOfflineStatus;
        @BindView(R.id.tv_machine_offline_area)
        TextView tvMachineOfflineArea;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
