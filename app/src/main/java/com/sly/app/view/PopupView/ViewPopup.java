package com.sly.app.view.PopupView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.UserNameBean;
import com.sly.app.view.FlowLayout.FlowLayout;
import com.sly.app.view.FlowLayout.TagAdapter;
import com.sly.app.view.FlowLayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViewPopup extends PopupWindow {

    private View contentView;
    private Context mContext;
    private TagFlowLayout flManaegeArea;
    private TagFlowLayout flMachineType;
    private TagFlowLayout flMachineStatus;

    private View shapedow;

    private TextView tvSearch;
    private EditText etMinerCode;
    private EditText etUser;
    private LinearLayout llAreaNo;
    private TextView tvEmtry;
    private View line;

    private List<UserNameBean> areaData;
    private List<UserNameBean> typeData;
    private List<UserNameBean> statusData;
    private Set<Integer> AreaSet;
    private Set<Integer> TypeSet;
    private Set<Integer> StatusSet;

    private boolean isMinerMaster;


    public ViewPopup(final Activity context, final List<UserNameBean> data1,
                     final List<UserNameBean> data2, final List<UserNameBean> data3, boolean isMinerMaster) {
        this.mContext = context;
        this.areaData = data1;
        this.typeData = data2;
        this.statusData = data3;
        this.isMinerMaster = isMinerMaster;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.my_popupview, null);

        shapedow = contentView.findViewById(R.id.popup_goods_noview);
        flManaegeArea = contentView.findViewById(R.id.flow_manage_area);
        flMachineType = contentView.findViewById(R.id.flow_machine_type);
        flMachineStatus = contentView.findViewById(R.id.flow_machine_status);
        tvSearch = contentView.findViewById(R.id.tv_search_list);
        etMinerCode = contentView.findViewById(R.id.et_miner_code);
        etUser = contentView.findViewById(R.id.et_user);

        llAreaNo = contentView.findViewById(R.id.ll_area_no);
        tvEmtry = contentView.findViewById(R.id.tv_emtry);
        line = contentView.findViewById(R.id.view_line1);
        if(isMinerMaster){
            llAreaNo.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            tvEmtry.setVisibility(View.VISIBLE);
        }

        initAreaFlow();
        initTypeFlow();
        initStatusFlow();
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPopup.this.dismiss();
                if(mOnSearchClickListener != null){
                    mOnSearchClickListener.onSearchClick(view, 0);
                }
            }
        });

        shapedow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPopup.this.dismiss();
            }
        });

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
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contentView);
        this.setWidth(w);
        this.setHeight(h - statusBarHeight2);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.update();

    }

    private void initAreaFlow() {
        flManaegeArea.setMaxSelectCount(1);
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        flManaegeArea.setAdapter(new TagAdapter(areaData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_online_area_tv,
                        flManaegeArea, false);
                UserNameBean bean = (UserNameBean)o;
                areaName.setText(bean.getName());
                return areaName;
            }
        });

        flManaegeArea.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                Log.d("tag","choose:" + selectPosSet.toString());
                AreaSet = selectPosSet;
            }
        });

    }

    private void initTypeFlow() {
        flMachineType.setMaxSelectCount(1);
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        flMachineType.setAdapter(new TagAdapter(typeData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_online_area_tv,
                        flMachineType, false);
                UserNameBean bean = (UserNameBean)o;
                areaName.setText(bean.getName());
                return areaName;
            }
        });

        flMachineType.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                Log.d("tag","choose:" + selectPosSet.toString());
                TypeSet = selectPosSet;
            }
        });

    }

    private void initStatusFlow() {
        flMachineStatus.setMaxSelectCount(1);
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        flMachineStatus.setAdapter(new TagAdapter(statusData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_online_area_tv,
                        flMachineStatus, false);
                UserNameBean bean = (UserNameBean)o;
                areaName.setText(bean.getName());
                return areaName;
            }
        });

        flMachineStatus.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                Log.d("tag","choose:" + selectPosSet.toString());
                StatusSet = selectPosSet;
            }
        });

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent);
            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public List<Set<Integer>> getChooseInfo(){
        List<Set<Integer>> list = new ArrayList<>();
        list.add(0, AreaSet);
        list.add(1, TypeSet);
        list.add(2, StatusSet);
        return list;
    }

    public String[] getEditTextInfo(){
        String[] useInfo = new String[2];
        String minerCode = etMinerCode.getText().toString().trim();
        String user = etUser.getText().toString().trim();
        useInfo[0] = minerCode;
        useInfo[1] = user;
        return useInfo;
    }

    public void setSearchClickListener(ViewPopup.OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private ViewPopup.OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onSearchClick(View view, int position);
    }
}
