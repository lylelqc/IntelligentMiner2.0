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

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 文 件 名: MoveType
 * 创 建 人: By k
 * 创建日期: 2017/12/27 16:12
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class MoveType {
    static final int fixed = 0;
    public static final int free = 1;
    public static final int active = 2;
    public static final int slide = 3;
    public static final int back = 4;

    @IntDef({fixed, free, active, slide, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface MOVE_TYPE {
    }
}

