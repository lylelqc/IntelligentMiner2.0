package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.MachineListBean;

import java.util.List;

import vip.devkit.library.Logcat;

public class MachineListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<MachineListBean> beanList;

    public MachineListRecyclerViewAdapter(Context context, List<MachineListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Logcat.e("MachineListRecyclerViewAdapter - " + beanList.toString());
        View view = LayoutInflater.from(context).inflate(R.layout.item_machine_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Logcat.e("MachineListRecyclerViewAdapter - " + beanList.size());
        if(beanList.size() != 0){
            Logcat.e(beanList.size()+"");
            final MachineListBean mlBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            eholder.mType.setText(mlBean.getMine77_Model());
            eholder.mIP.setText(mlBean.getMine77_IP());
            eholder.mName.setText(mlBean.getAreaName());
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mType;
        TextView mIP;
        TextView mName;

        public ViewHolder(View view) {
            super(view);
            mType = (TextView) view.findViewById(R.id.tv_machine_type);
            mIP = (TextView) view.findViewById(R.id.tv_machine_ip);
            mName = (TextView) view.findViewById(R.id.tv_machine_area);
        }
    }
}
