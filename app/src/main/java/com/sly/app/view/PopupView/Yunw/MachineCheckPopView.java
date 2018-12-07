package com.sly.app.view.PopupView.Yunw;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.yunw.machine.MachineTypeRecyclerViewAdapter;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vip.devkit.library.Logcat;

public class MachineCheckPopView extends PopupWindow implements View.OnClickListener {

    private MachineTypeRecyclerViewAdapter adapter;
    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView;

    private EditText etMinerCode;
    private EditText etVipCode;
    private EditText etBeginIP;
    private EditText etEndIP;

    private RadioButton cbStatus1;
    private RadioButton cbStatus2;
    private RadioButton cbStatus3;
    private RadioButton cbStatus4;

    private LinearLayout llListType;
    private LinearLayout llChooseStatus;

    private int mCount = 1;
    private List<MachineTypeBean> machineTypeList;

    public MachineCheckPopView(final Activity context, List<MachineTypeBean> typeList){
        this.mContext = context;
        this.machineTypeList = typeList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_machine_check, null);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(lineVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MachineTypeRecyclerViewAdapter(mContext, typeList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etMinerCode = contentView.findViewById(R.id.et_list_miner_code);
        etVipCode = contentView.findViewById(R.id.et_list_vip_code);
        etBeginIP = contentView.findViewById(R.id.et_list_begin_ip);
        etEndIP = contentView.findViewById(R.id.et_list_end_ip);

        llListType = contentView.findViewById(R.id.ll_machine_list_type);
        llChooseStatus = contentView.findViewById(R.id.ll_chooseStatus);

        cbStatus1 = contentView.findViewById(R.id.rb_machine_check_status1);
        cbStatus2 = contentView.findViewById(R.id.rb_machine_check_status2);
        cbStatus3 = contentView.findViewById(R.id.rb_machine_check_status3);
        cbStatus4 = contentView.findViewById(R.id.rb_machine_check_status4);

        tvSearch = contentView.findViewById(R.id.tv_machine_list_reset);
        tvReSet = contentView.findViewById(R.id.tv_machine_list_confirm);

        initView();
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
        String[] repairInfo = new String[9];
        repairInfo[0] = adapter.getIndex() == -1 ? "" : machineTypeList.get(adapter.getIndex()).getModelCode();
        repairInfo[1] = etMinerCode.getText().toString().trim();
        repairInfo[2] = etVipCode.getText().toString().trim();
        repairInfo[3] = etBeginIP.getText().toString().trim();
        repairInfo[4] = etEndIP.getText().toString().trim();
        repairInfo[5] = cbStatus1.isChecked() ? "true" : "false";
        repairInfo[6] = cbStatus2.isChecked() ? "true" : "false";
        repairInfo[7] = cbStatus3.isChecked() ? "true" : "false";
        repairInfo[8] = cbStatus4.isChecked() ? "true" : "false";
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
                adapter.setIndexNull();
                adapter.notifyDataSetChanged();
                etMinerCode.setText("");
                etVipCode.setText("");
                etBeginIP.setText("");
                etEndIP.setText("");
                cbStatus1.setChecked(false);
                cbStatus2.setChecked(false);
                cbStatus3.setChecked(false);
                cbStatus4.setChecked(false);
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
            ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
            return true;
        }else {
            if(Integer.parseInt(btime[1]) > Integer.parseInt(atime[1])){
                ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                return true;
            }else{
                if(Integer.parseInt(btime[2]) > Integer.parseInt(atime[2])){
                    ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                    return true;
                }
                return false;
            }
        }
    }
}
