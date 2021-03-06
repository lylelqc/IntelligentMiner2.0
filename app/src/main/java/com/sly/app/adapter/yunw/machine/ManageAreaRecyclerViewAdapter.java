package com.sly.app.adapter.yunw.machine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageAreaRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MachineManageAreaBean> beanList;
    private Set<Integer> indexSet = new TreeSet<>();
//    private Integer index;

    public ManageAreaRecyclerViewAdapter(Context context, List<MachineManageAreaBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_manage_area, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final MachineManageAreaBean bean = beanList.get(position);

        viewHolder.tvline.setVisibility(position % 2 == 1 ? View.VISIBLE : View.GONE);
        viewHolder.cbChoseItem.setText(bean.getAreaName());
        viewHolder.cbChoseItem.setChecked(indexSet.contains(position));
        viewHolder.cbChoseItem.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.v_manage_line)
        View tvline;
        @BindView(R.id.cb_chose_item)
        CheckBox cbChoseItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public Set<Integer> getAreaSet(){
        return indexSet;
    }

    public void setAreaSetNull(){
        indexSet.clear();
    }
}
