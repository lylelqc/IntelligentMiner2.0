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

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.GoodsBean;

import java.util.List;

/**
 * 文 件 名: PointHotAdapter
 * 创 建 人: By k
 * 创建日期: 2017/10/11 11:18
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class PointHotAdapter extends CommonAdapter<GoodsBean> {

    public PointHotAdapter(Context context, List<GoodsBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, GoodsBean mBean, int i) {
        holder.setImageURL(R.id.iv_goods_img, mBean.getDescript().replace("40-40","400-400"));
        holder.setText(R.id.txt_goods_name, mBean.getComTitle());

    }
}
