package com.sly.app.adapter.yunw.goline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.goline.AllMachineListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllMachineListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AllMachineListBean> list;

    public AllMachineListRecyclerViewAdapter(Context Context, List<AllMachineListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_all_machine, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final AllMachineListBean bean = list.get(position);
        viewHolder.tvAllMachineType.setText(bean.getMine77_Model());
        viewHolder.tvAllMachineIP.setText(bean.getMine77_IP());
        viewHolder.tvAllMachineMinerCode.setText(bean.getMine77_Worker1());
        viewHolder.tvAllMachineArea.setText(bean.getAreaName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_all_machine_type)
        TextView tvAllMachineType;
        @BindView(R.id.tv_all_machine_ip)
        TextView tvAllMachineIP;
        @BindView(R.id.tv_all_machine_miner_code)
        TextView tvAllMachineMinerCode;
        @BindView(R.id.tv_all_machine_area)
        TextView tvAllMachineArea;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
