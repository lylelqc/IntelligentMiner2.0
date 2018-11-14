package com.sly.app.view.PopupView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.RepairDetailsBean;

import java.util.List;

public class RepairDetailsPopupView extends PopupWindow{

    private View contentView;
    private Context mContext;

    private View shapedow;

    private TextView tvDetailStatus;
    private TextView tvConfirmTime;
    private TextView tvConfirmInfo;
    private TextView tvRepairDesciption;
    private TextView tvRepairFree;
    private TextView tvRepairNote;
    private TextView tvRepairTip;
    private TextView tvRepairForm;
    private TextView tvRepairDeal;
    private LinearLayout llRepairDetails1;
    private LinearLayout llRepairDetails2;
    private TextView tvRepairTip2;


    public RepairDetailsPopupView(final Activity context, RepairDetailsBean data, String resultCode) {
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_repair_details, null);

        shapedow = contentView.findViewById(R.id.popup_goods_noview);
        tvDetailStatus = contentView.findViewById(R.id.tv_detail_status);
        tvConfirmTime = contentView.findViewById(R.id.tv_confirm_time);
        tvConfirmInfo = contentView.findViewById(R.id.tv_confirm_info);
        tvRepairDesciption = contentView.findViewById(R.id.tv_repair_desciption);
        tvRepairFree = contentView.findViewById(R.id.tv_repair_free);
        tvRepairNote = contentView.findViewById(R.id.tv_repair_note);
        tvRepairTip = contentView.findViewById(R.id.tv_repair_tip);
        tvRepairForm = contentView.findViewById(R.id.tv_repair_form);
        tvRepairDeal = contentView.findViewById(R.id.tv_repaire_deal);
        llRepairDetails1 = contentView.findViewById(R.id.ll_repair_detail1);
        llRepairDetails2 = contentView.findViewById(R.id.ll_repair_detail2);
        tvRepairTip2 = contentView.findViewById(R.id.tv_repair_tip2);

        if(resultCode != null){
            if(resultCode.equals("04") || resultCode.equals("05") || resultCode.equals("06")){
                llRepairDetails1.setVisibility(View.VISIBLE);
                llRepairDetails2.setVisibility(View.GONE);
                tvDetailStatus.setText(data.getIsAccept() != null && !"null".equals(data.getIsAccept()) ? data.getIsAccept() : "");
                tvConfirmTime.setText(data.getPtime() != null && !"null".equals(data.getPtime()) ? data.getPtime() : "");
                tvConfirmInfo.setText(data.getAcceptInfo() != null && !"null".equals(data.getAcceptInfo()) ? data.getAcceptInfo() : "");
                tvRepairDesciption.setText(data.getUseInfo() != null && !"null".equals(data.getUseInfo()) ? data.getUseInfo() : "");
                tvRepairFree.setText(((int)data.getRepairSum())+"");
                tvRepairNote.setText(data.getRemark() != null && !"null".equals(data.getRemark()) ? data.getRemark() : "");
                tvRepairTip.setText(data.getRepairInfo() != null && !"null".equals(data.getRepairInfo()) ? data.getRepairInfo() : "");
            }else{
                llRepairDetails1.setVisibility(View.GONE);
                llRepairDetails2.setVisibility(View.VISIBLE);
                tvRepairTip2.setText(data.getRepairInfo() != null && !"null".equals(data.getRepairInfo()) ? data.getRepairInfo() : "");
            }

            if(resultCode.equals("00") || resultCode.equals("01")){
                tvRepairForm.setVisibility(View.VISIBLE);
                tvRepairDeal.setVisibility(View.VISIBLE);
                tvRepairForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairDetailsPopupView.this.dismiss();
                        if(mOnBtnClickListener != null){
                            mOnBtnClickListener.onBtnClick(view, 1);
                        }
                    }
                });

                tvRepairDeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mOnBtnClickListener != null){
                            mOnBtnClickListener.onBtnClick(view, 2);
                        }
                    }
                });
            }else{
                tvRepairForm.setVisibility(View.GONE);
                tvRepairDeal.setVisibility(View.GONE);
            }
        }

        shapedow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepairDetailsPopupView.this.dismiss();
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

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent);
            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public void setOnBtnClickListener(RepairDetailsPopupView.OnBtnClickListener listener) {
        this.mOnBtnClickListener = listener;
    }

    private RepairDetailsPopupView.OnBtnClickListener mOnBtnClickListener = null;

    //define interface
    public interface OnBtnClickListener {
        void onBtnClick(View view, int position);
    }

}
