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
package com.sly.app.activity.mine.league;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文 件 名: JoinPolicyActivity
 * 创 建 人: By k
 * 创建日期: 2017/12/11 18:33
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */

public class JoinPolicyActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_join_policy;
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("加盟政策");
    }

    @Override
    protected void setListener() {

    }


    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
}
