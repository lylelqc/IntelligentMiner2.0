package com.sly.app.adapter.yunw.goline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.sly.app.R;
import com.sly.app.model.yunw.goline.GolineAreaBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineGolineRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineManageAreaBean> beanList;
    private Set<Integer> indexSet = new TreeSet<>();

    public MachineGolineRecyclerViewAdapter(Context context, List<MachineManageAreaBean> beanList) {
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
        final MachineManageAreaBean bean = beanList.get(position);

        viewHolder.cbGolinePlan.setText(bean.getAreaName());
        viewHolder.cbGolinePlan.setChecked(indexSet.contains(position));
        viewHolder.cbGolinePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    if(!indexSet.contains(position)){
                        indexSet.add(position);
                    }
                }else{
                    if(indexSet.contains(position)){
                        indexSet.remove(position);
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
