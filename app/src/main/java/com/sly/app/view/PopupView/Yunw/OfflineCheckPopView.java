package com.sly.app.view.PopupView.Yunw;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.yunw.machine.MachineTypeRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.ManageAllAreaRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.ManageTypeRecyclerViewAdapter;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.util.List;

public class OfflineCheckPopView extends PopupWindow implements View.OnClickListener {

    private MachineTypeRecyclerViewAdapter adapter;
    private ManageAllAreaRecyclerViewAdapter adapter2;

    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;

    private EditText etMinerCode;
    private EditText etMachineCode;
    private EditText etIpCode;
    private EditText etBeginIP;
    private EditText etEndIP;


    private List<MachineTypeBean> machineTypeList;
    private List<MachineManageAreaBean> machineAreaList;

    public OfflineCheckPopView(final Activity context, List<MachineTypeBean> typeList, List<MachineManageAreaBean> areaList){
        this.mContext = context;
        this.machineTypeList = typeList;
        this.machineAreaList = areaList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_offline_check, null);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(lineVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MachineTypeRecyclerViewAdapter(mContext, typeList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        recyclerView2 = contentView.findViewById(R.id.recycler_area_view);
        MyStaggeredGridLayoutManager mLayoutManager2 = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical2 = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.addItemDecoration(lineVertical2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        adapter2 = new ManageAllAreaRecyclerViewAdapter(mContext, areaList);
        recyclerView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        etMachineCode = contentView.findViewById(R.id.et_list_machine_code);
        etMinerCode = contentView.findViewById(R.id.et_list_miner_code);
        etIpCode = contentView.findViewById(R.id.et_list_ip_code);
        etBeginIP = contentView.findViewById(R.id.et_list_begin_ip);
        etEndIP = contentView.findViewById(R.id.et_list_end_ip);

        tvSearch = contentView.findViewById(R.id.tv_machine_list_reset);
        tvReSet = contentView.findViewById(R.id.tv_machine_list_confirm);

        initView();
        tvReSet.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = mContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight((h - statusBarHeight2 - AppUtils.dp2px(mContext,46)));
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    private void initView() {

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
        String[] repairInfo = new String[7];
        repairInfo[0] = etMachineCode.getText().toString().trim();
        repairInfo[1] = etMinerCode.getText().toString().trim();
        repairInfo[2] = etIpCode.getText().toString().trim();
        repairInfo[3] = etBeginIP.getText().toString().trim();
        repairInfo[4] = etEndIP.getText().toString().trim();
        repairInfo[5] = adapter.getIndex() == -1 ? "" : machineTypeList.get(adapter.getIndex()).getModelCode();
        int position2 = -1;
        if(adapter2.getAllAreaSet().size() > 0){
            position2 = adapter2.getAllAreaSet().iterator().next();
        }
        repairInfo[6] = (position2 == -1) ? "" : machineAreaList.get(position2).getAreaSysCode();
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
                etMachineCode.setText("");
                etMinerCode.setText("");
                etIpCode.setText("");
                etBeginIP.setText("");
                etEndIP.setText("");
                adapter.setIndexNull();
                adapter.notifyDataSetChanged();
                adapter2.setAllAreaSetNull();
                adapter2.notifyDataSetChanged();
                break;
            case R.id.tv_machine_list_confirm:
                this.dismiss();
                mOnSearchClickListener.onSearchClick(tvSearch, 0);
                break;
        }
    }

    private boolean isThanBefore(String before, String after){
        String[] btime = before.split(".");
        String[] atime = after.split(".");
        if(Integer.parseInt(btime[0]) > Integer.parseInt(atime[0])){
            ToastUtils.showToast(mContext.getString(R.string.ip_than_before));
            return true;
        }else {
            if(Integer.parseInt(btime[1]) > Integer.parseInt(atime[1])){
                ToastUtils.showToast(mContext.getString(R.string.ip_than_before));
                return true;
            }else{
                if(Integer.parseInt(btime[2]) > Integer.parseInt(atime[2])){
                    ToastUtils.showToast(mContext.getString(R.string.ip_than_before));
                    return true;
                }
                return false;
            }
        }
    }
}
