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
package com.sly.app.view.floatViewUtil;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 文 件 名: FloatView
 * 创 建 人: By k
 * 创建日期: 2017/12/27 16:01
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class FloatView {
    private View mView;
    private int mLayoutId;
    private int mMoveType;
    private Context mContext;

    /**
     * 初始化Activity
     * With FloatView
     *
     * @param mContext the activity
     * @return
     */
    public static FloatView with(@NonNull Context mContext) {
        if (mContext == null)
            throw new IllegalArgumentException("mContext不能为null");
        return new FloatView(mContext);
    }

    /**
     * 初始化
     *
     * @param mContext the activity
     */
    public FloatView(Context mContext) {
        this.mContext = mContext;
    }

    public FloatView setView(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("View不能为null");
        }
        mView = view;
        return this;
    }

    public FloatView setView(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    public FloatView setMoveType(@MoveType.MOVE_TYPE int moveType) {
        mMoveType = moveType;
        return this;
    }

    public void init() {
        new FloatingView(mContext, mView);
    }
}
