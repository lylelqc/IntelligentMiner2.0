package com.sly.app.view.PopupView.Miner;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.miner.MinerTypeRecyclerViewAdapter;
import com.sly.app.model.master.MasterMachineTypeBean;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MinerTypePopView extends PopupWindow implements View.OnClickListener{

    private MinerTypeRecyclerViewAdapter adapter;
    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView;
    private List<MasterMachineTypeBean> BeanList;

    public MinerTypePopView(final Activity context, List<MasterMachineTypeBean> typeList){
        this.mContext = context;
        this.BeanList = typeList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_manage_area, null);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MinerTypeRecyclerViewAdapter(mContext, typeList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tvSearch = contentView.findViewById(R.id.tv_manage_area_reset);
        tvReSet = contentView.findViewById(R.id.tv_manage_area_confirm);

        tvReSet.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String getMinerTypeCode(){
        if(!adapter.getTypeSet().isEmpty()){
            String areaCode = BeanList.get(adapter.getTypeSet().iterator().next()).getModel();
            return areaCode;
        }
        return "";
    }

    public void setTypeSearchClickListener(OnTypeClickListener listener) {
        this.mOnTypeClickListener = listener;
    }

    private OnTypeClickListener mOnTypeClickListener = null;

    //define interface
    public interface OnTypeClickListener {
        void onTypeSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manage_area_reset:
                clearAll();
                break;
            case R.id.tv_manage_area_confirm:
                this.dismiss();
                mOnTypeClickListener.onTypeSearchClick(tvSearch, 0);
                break;
        }
    }

    public void clearAll(){
        adapter.setTypeSetNull();
        adapter.notifyDataSetChanged();
    }
}
