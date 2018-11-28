package com.sly.app.view.PopupView;

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
import com.sly.app.adapter.yunw.machine.MachineTypeRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.ManageAllAreaRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.ManageAreaRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.ManageAreaRecyclerViewAdapter$ViewHolder$$ViewBinder;
import com.sly.app.adapter.yunw.machine.ManageTypeRecyclerViewAdapter;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ManageAllChoicePopView extends PopupWindow implements View.OnClickListener {

    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private ManageAllAreaRecyclerViewAdapter adapter1;
    private ManageTypeRecyclerViewAdapter adapter2;

    private EditText etMinerCode;
    private EditText etVipCode;
    private EditText etBeginIP;
    private EditText etEndIP;

    private RadioButton cbStatus1;
    private RadioButton cbStatus2;
    private RadioButton cbStatus3;
    private RadioButton cbStatus4;

    private List<MachineTypeBean> machineTypeList;
    private List<MachineManageAreaBean> machineAreaList;

    private Set<Integer> typeSet = new TreeSet<>();
    private Set<Integer> areaSet = new TreeSet<>();

    public ManageAllChoicePopView(final Activity context, List<MachineManageAreaBean> areaList, List<MachineTypeBean> typeList){
        this.mContext = context;
        this.machineAreaList = areaList;
        this.machineTypeList = typeList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_manage_all_choice, null);

        recyclerView1 = contentView.findViewById(R.id.recycler_area_view);
        MyStaggeredGridLayoutManager mLayoutManager1 = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical1 = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.addItemDecoration(lineVertical1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        adapter1 = new ManageAllAreaRecyclerViewAdapter(mContext, areaList);
        recyclerView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        recyclerView2 = contentView.findViewById(R.id.recycler_type_view);
        MyStaggeredGridLayoutManager mLayoutManager2 = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical2 = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.addItemDecoration(lineVertical2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        adapter2 = new ManageTypeRecyclerViewAdapter(mContext, typeList);
        recyclerView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        etMinerCode = contentView.findViewById(R.id.et_manage_miner_code);
        etVipCode = contentView.findViewById(R.id.et_manage_vip_code);

        cbStatus1 = contentView.findViewById(R.id.rb_manage_check_status1);
        cbStatus2 = contentView.findViewById(R.id.rb_manage_check_status2);
        cbStatus3 = contentView.findViewById(R.id.rb_manage_check_status3);
        cbStatus4 = contentView.findViewById(R.id.rb_manage_check_status4);

        tvSearch = contentView.findViewById(R.id.tv_manage_reset);
        tvReSet = contentView.findViewById(R.id.tv_manage_confirm);

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
        this.setHeight((h - statusBarHeight2 - AppUtils.dp2px(mContext,97)));
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 50);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String getAllAreaCode(){
        if(!adapter1.getAllAreaSet().isEmpty()){
            String areaCode = machineAreaList.get(adapter1.getAllAreaSet().iterator().next()).getAreaSysCode();
            return areaCode;
        }
        return "";
    }

    public String getAllTypeSet(){
        if(!adapter2.getAllTypeSet().isEmpty()){
            String typeCode = machineTypeList.get(adapter2.getAllTypeSet().iterator().next()).getModelCode();
            return typeCode;
        }
        return "";
    }

    public String[] getTextInfo(){
        String[] repairInfo = new String[6];
        repairInfo[0] = etMinerCode.getText().toString().trim();
        repairInfo[1] = etVipCode.getText().toString().trim();
        repairInfo[2] = cbStatus1.isChecked() ? "true" : "false";
        repairInfo[3] = cbStatus2.isChecked() ? "true" : "false";
        repairInfo[4] = cbStatus3.isChecked() ? "true" : "false";
        repairInfo[5] = cbStatus4.isChecked() ? "true" : "false";
        return repairInfo;
    }

    public void setAllChoiceSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onAllChoiceSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manage_reset:
                clearAll();
                break;
            case R.id.tv_manage_confirm:
                this.dismiss();
                mOnSearchClickListener.onAllChoiceSearchClick(tvSearch, 0);
                break;
        }
    }

    public void clearAll() {
        adapter1.setAllAreaSetNull();
        adapter1.notifyDataSetChanged();
        adapter2.setAllTypeSetNull();
        adapter2.notifyDataSetChanged();
        etMinerCode.setText("");
        etVipCode.setText("");
        cbStatus1.setChecked(false);
        cbStatus2.setChecked(false);
        cbStatus3.setChecked(false);
        cbStatus4.setChecked(false);
    }
}
