package com.sly.app.adapter.yunw.machine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineTypeBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineTypeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineTypeBean> beanList;
    private int index = -1;//标记当前选择的选项

    public MachineTypeRecyclerViewAdapter(Context context, List<MachineTypeBean> beanList) {
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
        viewHolder.rbMachineTypeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    index = position;
                }else{
                    index = -1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rb_machine_type_status)
        RadioButton rbMachineTypeStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public int getIndex() {
        return index;
    }
}
