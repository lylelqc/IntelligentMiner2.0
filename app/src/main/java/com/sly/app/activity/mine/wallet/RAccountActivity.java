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

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.pwd.SercurityDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;

/**
 * 文 件 名: RAccountActivity
 * 创 建 人: By k
 * 创建日期: 2017/11/4 16:10
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class RAccountActivity extends BaseActivity {
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
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private String MemberCode, Token;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).keyboardEnable(false).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("转账");
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wallet_zz;
    }

    @Override
    protected void setListener() {

    }


    private void RegData(final String MemberCode, final String Token, final String account, final String sum) {
        if (CommonUtils.isBlank(account)) {
            ToastUtils.showToast("请输入要转入的账户");
            return;
        }
        if (CommonUtils.isBlank(sum)) {
            ToastUtils.showToast("请输入要转入的金额");
            return;
        }
        SercurityDialog mDialog = new SercurityDialog(this, sum);
        mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
            @Override
            public void inputComplete(String passWord) {
                ProWalletZZ(MemberCode, Token, account, sum, passWord);
            }
        });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void ProWalletZZ(String memberCode, String token, String account, String sum, String passWord) {
        initProgressDialog("系统处理中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("FromMember", memberCode);
        map.put("Token", token);
        map.put("ToMember", account);
        map.put("Sum", sum);
        map.put("PayPassword", passWord);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_WALLET_ZZ, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
                NetLogCat.W(NetWorkConstant.USER_WALLET_ZZ, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });

    }


    @OnClick({R.id.btn_main_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_submit:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    RegData(MemberCode, Token, mEtAccount.getText().toString().trim(), mEtNum.getText().toString().trim());
                }
                break;
        }
    }
}
