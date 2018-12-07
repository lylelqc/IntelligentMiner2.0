package com.sly.app.view.PopupView.Yunw;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.yunw.machine.ManageAllAreaRecyclerViewAdapter;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.util.List;

public class SetPoolCheckPopView extends PopupWindow implements View.OnClickListener {

    private ManageAllAreaRecyclerViewAdapter adapter;
    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView;

    private EditText etMinerCode;
    private EditText etVipCode;
    private EditText etIP;

    private List<MachineManageAreaBean> machineAreaList;

    public SetPoolCheckPopView(final Activity context, List<MachineManageAreaBean> areaList){
        this.mContext = context;
        this.machineAreaList = areaList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_set_pool_check, null);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(lineVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ManageAllAreaRecyclerViewAdapter(mContext, areaList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etMinerCode = contentView.findViewById(R.id.et_list_miner_code);
        etVipCode = contentView.findViewById(R.id.et_list_vip_code);
        etIP = contentView.findViewById(R.id.et_list_ip);

        tvSearch = contentView.findViewById(R.id.tv_machine_list_reset);
        tvReSet = contentView.findViewById(R.id.tv_machine_list_confirm);

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

    public String[] getTextInfo(){
        String[] repairInfo = new String[4];
        int position = -1;
        if(adapter.getAllAreaSet().size() > 0){
            position = adapter.getAllAreaSet().iterator().next();
        }
        repairInfo[0] = position == -1 ? "" : machineAreaList.get(position).getAreaSysCode();
        repairInfo[1] = etMinerCode.getText().toString().trim();
        repairInfo[2] = etVipCode.getText().toString().trim();
        repairInfo[3] = etIP.getText().toString().trim();
        return repairInfo;
    }

    public void setSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_machine_list_reset:
                adapter.setAllAreaSetNull();
                adapter.notifyDataSetChanged();
                etMinerCode.setText("");
                etVipCode.setText("");
                etIP.setText("");
                break;
            case R.id.tv_machine_list_confirm:
                this.dismiss();
                mOnSearchClickListener.onSearchClick(tvSearch, 0);
                break;
        }
    }
}
