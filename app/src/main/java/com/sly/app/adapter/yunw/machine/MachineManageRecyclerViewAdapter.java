package com.sly.app.adapter.yunw.machine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineListBean;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineManageRecyclerViewAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<MachineListBean> list;
    //保存选中的项目集合下标
    private Set<Integer> indexSet;
    // 保存
//    private Map<Integer, Boolean> selectedMap;

    public MachineManageRecyclerViewAdapter(Context Context, List<MachineListBean> mResultList, Set<Integer> set) {
        mContext = Context;
        list = mResultList;
        indexSet = set;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_machine_manage, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final MachineListBean bean = list.get(position);

        viewHolder.tvManageMinerCode.setText(bean.getMinerSysCode());
        viewHolder.tvManageMachineArea.setText(bean.getAreaName());
        viewHolder.tvManageMachineType.setText(bean.getModel());

        if(bean.getStatusCode().equals("00")){
            viewHolder.tvManageMachineStatus.setTextColor(mContext.getResources().getColor(R.color.color_27ae0c));
            viewHolder.tvManageMachineStatus.setText(bean.getStatusName());
        }else if(bean.getStatusCode().equals("01")){
            viewHolder.tvManageMachineStatus.setTextColor(mContext.getResources().getColor(R.color.color_fb3a2d));
            viewHolder.tvManageMachineStatus.setText(bean.getStatusName().substring(0,2));
        }else if(bean.getStatusCode().equals("02")){
            viewHolder.tvManageMachineStatus.setTextColor(mContext.getResources().getColor(R.color.color_f6a800));
            viewHolder.tvManageMachineStatus.setText(bean.getStatusName());
        }else{
            viewHolder.tvManageMachineStatus.setTextColor(mContext.getResources().getColor(R.color.color_777777));
            viewHolder.tvManageMachineStatus.setText(bean.getStatusName());
        }
        viewHolder.tvManageMachineCals.setText("算力"+String.format("%.2f",bean.getNowCal())+"T");

        viewHolder.tvManageMachineIp.setText(bean.getIP());
        viewHolder.cbChoseStatus.setChecked(indexSet.contains(position));
        viewHolder.cbChoseStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        @BindView(R.id.cb_chose_status)
        CheckBox cbChoseStatus;
        @BindView(R.id.tv_manage_miner_code)
        TextView tvManageMinerCode;
        @BindView(R.id.tv_manage_mahcine_area)
        TextView tvManageMachineArea;
        @BindView(R.id.tv_manage_machine_type)
        TextView tvManageMachineType;
        @BindView(R.id.tv_manage_mahcine_status)
        TextView tvManageMachineStatus;
        @BindView(R.id.tv_manage_machine_cals)
        TextView tvManageMachineCals;
        @BindView(R.id.tv_manage_mahcine_ip)
        TextView tvManageMachineIp;
        @BindView(R.id.rl_manage_details)
        RelativeLayout rlManageDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
