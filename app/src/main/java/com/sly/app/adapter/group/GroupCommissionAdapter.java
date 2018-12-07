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
package com.sly.app.adapter.group;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.UserGroup;
import com.sly.app.utils.CommonUtils;

import java.util.List;

/**
 * 文 件 名: GroupTeamAdapter
 * 创 建 人: By k
 * 创建日期: 2017/12/15 14:33
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class GroupCommissionAdapter extends CommonAdapter<UserGroup.QunBean> {
    public GroupCommissionAdapter(Context context, List<UserGroup.QunBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, UserGroup.QunBean userGroupTeam, int i) {
        holder.setText(R.id.tv_num, i + 1 + "\u3000")
                .setText(R.id.tv_date, userGroupTeam.getBonusTime())
                .setText(R.id.tv_sum,  CommonUtils.getDoubleStr(Double.valueOf(userGroupTeam.getBonus())))
                .setText(R.id.tv_state, userGroupTeam.getBonusStatus() +"");

    }
}
