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
package com.sly.app.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 文 件 名: BaseLayout
 * 创 建 人: By k
 * 创建日期: 2018/1/8 17:50
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 说    明：
 * 修改时间：
 * 修改备注： 此Layout是为解决 当设定状态栏为透明后，android:windowSoftInputMode="adjustResize"
 */
public class BaseLayout  extends  RelativeLayout {

    private int[] mInsets = new int[4];

    public BaseLayout(Context context) {
        super(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    /**
     * 此方法以过期，当应用最低API支持为20后，可以重写以下方法
     * @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    mInsets[0] = insets.getSystemWindowInsetLeft();
    mInsets[1] = insets.getSystemWindowInsetTop();
    mInsets[2] = insets.getSystemWindowInsetRight();
    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
    insets.getSystemWindowInsetBottom()));
    } else {
    return insets;
    }
    }
     *  未测试……
     */
    @Override
    protected final boolean fitSystemWindows(Rect insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            return super.fitSystemWindows(insets);
        } else {
            return super.fitSystemWindows(insets);
        }
    }
}