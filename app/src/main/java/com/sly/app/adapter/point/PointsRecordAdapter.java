/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.adapter.point;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.PointRecordBean;

import java.util.List;

/**
 * 文 件 名: PointsRecordAdapter
 * 创 建 人: By k
 * 创建日期: 2017/10/11 16:37
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class PointsRecordAdapter extends CommonAdapter<PointRecordBean> {
    public PointsRecordAdapter(Context context, List<PointRecordBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, PointRecordBean bean, int i) {
        holder.setText(R.id.tv_record_type,bean.getP002_NotesSystem()).setText(R.id.tv_date,bean.getP002_Time());
        TextView textView = holder.getView(R.id.tv_num);
        if (bean.getP002_BalanceBefore()>bean.getP002_BalanceAfter()){
            holder.setText(R.id.tv_num," "+bean.getP002_Sum());
            textView.setTextColor(Color.parseColor("#333333"));
        }else {
            holder.setText(R.id.tv_num," +"+bean.getP002_Sum());
            textView.setTextColor(Color.parseColor("#0aaadc"));
        }

    }
}
