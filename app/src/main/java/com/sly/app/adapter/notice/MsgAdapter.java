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
package com.sly.app.adapter.notice;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.MsgBean;

import java.util.List;

/**
 * 文 件 名: MsgAdapter
 * 创 建 人: By k
 * 创建日期: 2017/12/25 16:40
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class MsgAdapter extends CommonAdapter<MsgBean> {
    public MsgAdapter(Context context, List<MsgBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, MsgBean msgBean, int i) {
        holder.setText(R.id.tv_msg_title, msgBean.getTitle())
                .setText(R.id.tv_msg_class, msgBean.getClassName())
                .setText(R.id.tv_msg_abstract,msgBean.getIntro())
                .setText(R.id.tv_msg_date,msgBean.getAddTime());
        holder.setImageURL(R.id.iv_msg_img,msgBean.getLogo().replace("UploadThumbnail40-40","/Upload/Thumbnail/400-400/"));

    }
}
