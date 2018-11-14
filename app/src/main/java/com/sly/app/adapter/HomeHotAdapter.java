package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.GoodsBean;

import java.util.List;

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
public class HomeHotAdapter extends CommonAdapter<GoodsBean> {

    public HomeHotAdapter(Context context, List<GoodsBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, GoodsBean goodsBean, int i) {
        holder.setImageURL(R.id.iv_goods_img, goodsBean.getDescript().replace("40-40","400-400"));
        holder.setText(R.id.txt_goods_name, goodsBean.getComTitle());
    }
}
