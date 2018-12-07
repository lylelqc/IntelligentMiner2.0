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
import com.sly.app.model.UserGroupTeam;

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
public class GroupTeamAdapter extends CommonAdapter<UserGroupTeam.RowsBean> {
    public GroupTeamAdapter(Context context, List<UserGroupTeam.RowsBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, UserGroupTeam.RowsBean userGroupTeam, int i) {
        holder.setText(R.id.tv_num, i+1 + "")
                .setText(R.id.tv_user_id, userGroupTeam.getChildMember())
                .setText(R.id.tv_user_name, userGroupTeam.getName())
                .setText(R.id.tv_user_level_1, userGroupTeam.getLayer()+"层")
                .setText(R.id.tv_user_level_2, userGroupTeam.getLevelName());

    }
}
