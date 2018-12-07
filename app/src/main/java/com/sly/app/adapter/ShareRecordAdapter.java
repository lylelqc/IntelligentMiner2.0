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
package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.ShareRecord;

import java.util.List;

/**
 * 文 件 名: ShareRecordAdapter
 * 创 建 人: By k
 * 创建日期: 2017/11/4 17:38
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class ShareRecordAdapter extends CommonAdapter<ShareRecord> {
    public ShareRecordAdapter(Context context, List<ShareRecord> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ShareRecord shareRecord, int i) {
        holder.setText(R.id.tv_name, shareRecord.getMemberCode())
                .setText(R.id.tv_type, shareRecord.getMemberLeveName())
                .setText(R.id.tv_data, "邀请时间："+ shareRecord.getRegisterTime());
        holder.setImageURLHP(R.id.iv_hp,shareRecord.getImageURl().replace("9897","9898"));

    }
}
