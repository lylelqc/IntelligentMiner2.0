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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import vip.devkit.library.Logcat;

/**
 * 文 件 名: FloatingView
 * 创 建 人: By k
 * 创建日期: 2017/12/27 16:15
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class FloatingView extends BaseFloatingView  {
    private Context mContext;
    private ImageView img;
    private View root;

    public FloatingView(Context context,View mView) {
        super(context);
        this.mContext = context;
        this.root = mView;
        initView(context);
    }

    private void initView(Context context) {
        show();
    }

    public FloatingView(Context context,int mLayout) {
        super(context);
        this.mContext = context;
    }

    public boolean show(){
        Logcat.i("------------------Util-------");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = Util.dip2px(mContext, 160);
        lp.height = Util.dip2px(mContext, 90);

        if (root.getParent() == null)
            addView(root, lp);
        super.showView(this);
        return true;
    }
    public void dismiss() {
        super.hideView();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        dismiss();
        return true;
    }
}
