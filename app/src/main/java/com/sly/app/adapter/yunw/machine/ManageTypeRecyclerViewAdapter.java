package com.sly.app.adapter.yunw.machine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineTypeBean;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageTypeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineTypeBean> beanList;
    private Set<Integer> indexSet = new TreeSet<>();

    public ManageTypeRecyclerViewAdapter(Context context, List<MachineTypeBean> beanList) {
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
        final MachineTypeBean bean = beanList.get(position);

        viewHolder.rbMachineTypeStatus.setText(bean.getModelName());
        viewHolder.rbMachineTypeStatus.setChecked(indexSet.contains(position));
        viewHolder.rbMachineTypeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexSet.clear();
                if(((CheckBox)view).isChecked()){
                    if(!indexSet.contains(position)){
                        indexSet.add(position);
                    }
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

    public Set<Integer> getAllTypeSet(){
        return indexSet;
    }

    public void setAllTypeSetNull(){
        indexSet.clear();
    }

}
