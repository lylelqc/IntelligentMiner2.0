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
package com.sly.app.activity.mine.wallet;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.balance.BalanceInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 文 件 名: FundActivity
 * 创 建 人: By k
 * 创建日期: 2017/12/9 15:10
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class FundActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.tv_fund_a)
    TextView mTvFundA;
    @BindView(R.id.tv_fund_b)
    TextView mTvFundB;
    @BindView(R.id.tv_fund_c)
    TextView mTvFundC;
    private String MemberCode, Token;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        getBalance(MemberCode, Token);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fund;
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("我的基金");


    }

    @Override
    protected void setListener() {

    }

    private void getBalance(String memberCode, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        map.put("CurrencyCode", "TravelFund,BuyHourseFund,BuyCardFund");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSER_DATE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logcat.e("返回码:" + statusCode);
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.PURSER_DATE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<BalanceInfo> balanceInfo = JSON.parseArray(mReturnBean.getData(), BalanceInfo.class);
                    for (int i = 0; i < balanceInfo.size(); i++) {
                        if (balanceInfo.get(i).getPurseType().equals("BuyHourseFund")) {
                            mTvFundA.setText("￥" + balanceInfo.get(i).getBalance());
                        } else if (balanceInfo.get(i).getPurseType().equals("TravelFund")) {
                            mTvFundB.setText("￥" + balanceInfo.get(i).getBalance());
                        }else if (balanceInfo.get(i).getPurseType().equals("BuyCardFund")) {
                            mTvFundC.setText("￥" + balanceInfo.get(i).getBalance());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
}
