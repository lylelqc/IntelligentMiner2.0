package com.sly.app.view.PopupView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.UserNameBean;
import com.sly.app.view.FlowLayout.FlowLayout;
import com.sly.app.view.FlowLayout.TagAdapter;
import com.sly.app.view.FlowLayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

public class CheckPopupView extends PopupWindow {

    private View contentView;
    private Context mContext;
    private TagFlowLayout flRepairStatus;

    private View shapedow;

    private TextView tvSearch;

    private EditText etBillID;
    private EditText etAreaIP;
    private EditText etBeginpTime;
    private EditText etEndpTime;
    private EditText etBeginTime;
    private EditText etEndTime;

    private List<UserNameBean> statusData;
    private Set<Integer> StatusSet;


    public CheckPopupView(final Activity context, List<UserNameBean> data) {
        this.mContext = context;
        this.statusData = data;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_repair_check, null);

        shapedow = contentView.findViewById(R.id.popup_goods_noview);

        flRepairStatus = contentView.findViewById(R.id.flow_repair_status);
        etBillID = contentView.findViewById(R.id.et_repair_bill_id);
        etAreaIP = contentView.findViewById(R.id.et_repair_area_ip);
        etBeginpTime = contentView.findViewById(R.id.et_begin_ptime);
        etEndpTime = contentView.findViewById(R.id.et_end_ptime);
        etBeginTime = contentView.findViewById(R.id.et_begin_time);
        etEndTime = contentView.findViewById(R.id.et_end_time);
        tvSearch = contentView.findViewById(R.id.tv_search_list);

        initStatusFlow();
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPopupView.this.dismiss();
                if(mOnSearchClickListener != null){
                    mOnSearchClickListener.onSearchClick(view, 0);
                }
            }
        });

        shapedow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPopupView.this.dismiss();
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

    private void initStatusFlow() {
        flRepairStatus.setMaxSelectCount(1);
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        flRepairStatus.setAdapter(new TagAdapter(statusData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_online_area_tv,
                        flRepairStatus, false);
                UserNameBean bean = (UserNameBean)o;
                areaName.setText(bean.getName());
                return areaName;
            }
        });

        flRepairStatus.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
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

    public Set<Integer> getChooseInfo(){
        if(StatusSet != null && StatusSet.size() > 0){
            return StatusSet;
        }
        return null;
    }

    public String[] getEditTextInfo(){
        String[] repairInfo = new String[6];
        repairInfo[0] = etBillID.getText().toString().trim();
        repairInfo[1] = etBeginpTime.getText().toString().trim();
        repairInfo[2] = etEndpTime.getText().toString().trim();
        repairInfo[3] = etBeginTime.getText().toString().trim();
        repairInfo[4] = etEndTime.getText().toString().trim();
        repairInfo[5] = etAreaIP.getText().toString().trim();
        return repairInfo;
    }

    public void setSearchClickListener(CheckPopupView.OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private CheckPopupView.OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public static interface OnSearchClickListener {
        void onSearchClick(View view, int position);
    }
}
