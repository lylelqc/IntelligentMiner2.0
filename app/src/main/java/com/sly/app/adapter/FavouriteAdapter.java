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
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.FavouriteBean;

import java.util.List;

/**
 * 文 件 名: FavouriteAdapter
 * 创 建 人: By k
 * 创建日期: 2017/11/7 15:22
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class FavouriteAdapter extends CommonAdapter<FavouriteBean> {
    public FavouriteAdapter(Context context, List<FavouriteBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, FavouriteBean favouriteBean, int i) {
        RelativeLayout relativeLayout = holder.getView(R.id.rl_item_layout);
        relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.setText(R.id.tv_item_title, favouriteBean.getComTitle())
                .setText(R.id.tv_item_attrs, "编号：" + favouriteBean.getComCode())
                .setText(R.id.tv_item_price, "价格：" + favouriteBean.getDefaultPrice() + "")
                .setText(R.id.tv_item_count, "X " + 1 + "");
        holder.setImageURL(R.id.iv_item_img,  favouriteBean.getImgList().get(0).replace("40-40","400-400"));
    }
}
