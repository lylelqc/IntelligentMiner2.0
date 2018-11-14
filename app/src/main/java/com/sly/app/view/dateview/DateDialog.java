/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.view.dateview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.view.CityDialog;

/**
 * 文 件 名: DateDialog
 * 创 建 人: By k
 * 创建日期: 2017/12/12 10:21
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class DateDialog {
    private static android.content.Context Context;
    private static DateDialog single = null;

    public DateDialog() {
    }

    public static DateDialog getInstance() {
        if (single == null) {
            single = new DateDialog();
        }
        return single;
    }

    public interface onInputNameEvent {
        void onClick(String data);
    }

    public Dialog setNicknameDialog(android.content.Context context, final CityDialog.onInputNameEvent listener, int sceenwith) {
        Context = context;
        final AlertDialog dialog = new AlertDialog.Builder(Context, R.style.quick_option_dialog).create();
        LayoutInflater inflaterDl = (LayoutInflater) Context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflaterDl.inflate(R.layout.dialog_date_wheel, null);
        dialog.setView(layout);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.show();
        //   Window mWindow = dialog.getWindow();
        //   mWindow.setContentView(R.layout.address_wheel);
        //   setShowWith(sceenwith, mWindow, 0.95);
        TextView mSure = dialog.findViewById(R.id.dialogtool_time_select_sure);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final DateView addressDialog = dialog.findViewById(R.id.city_picker);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = addressDialog.getProvince();
                String month = addressDialog.getCity();
                String day = addressDialog.getDisteic();
                if (month.length() == 2) {
                    month = "0" + month;
                }
                if (day.length() == 2) {
                    day = "0" + day;
                }
                String date = year+month+day;
                date = date.replace("年","-");
                date = date.replace("月","-");
                date = date.replace("日","");
                StringBuffer city = new StringBuffer().append(addressDialog.getProvince())
                        .append(" ").append(addressDialog.getCity()).append(" ").append(addressDialog.getDisteic());
                listener.onClick(date,addressDialog.getPid(),addressDialog.getCid()+"",addressDialog.getAid()+"");
                dialog.dismiss();

            }
        });
        return dialog;
    }

    private void setShowWith(int mWidth, Window mWindow, double d) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = (int) (mWidth * d);
        mWindow.setAttributes(lp);
    }
}
