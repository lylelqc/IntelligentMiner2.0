/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.activity.mine;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.TimeUtils;
import com.sly.app.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;

/**
 * 文 件 名: RelationDQCActivity
 * 创 建 人: By k
 * 创建日期: 2017/9/29 12:33
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class RelationDQCActivity extends BaseActivity {
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
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.textView3)
    TextView mTextView3;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_get_code)
    Button mTvGetCode;
    private String MemberCode, Token;
    private String Mobile, DQC_Account, DQC_pwd, VerificationCode;
    private TimeUtils timeUtils;
    @Override
    protected void initView() {
        mTvMainTitle.setText("绑定美好地球村账号");
        mTvMainRight.setText("绑定");
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_relation;
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right, R.id.btn_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_main_right:
                DQC_Account = mEtAccount.getText().toString();
                DQC_pwd = mEtPwd.getText().toString();
                VerificationCode = mEtCode.getText().toString();
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    if (RegInfo(DQC_Account, DQC_pwd, VerificationCode)==true) {
                        ProRelation(MemberCode, Token, DQC_Account, DQC_pwd, VerificationCode);
                    }
                }
                break;
            case R.id.btn_get_code:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    timeUtils=new TimeUtils(mTvGetCode,"获取验证码");
                    timeUtils.RunTimer();
                    sendMsm(MemberCode,Token);
                }
                break;
        }
    }

    /**
     * 绑定
     *
     * @param memberCode
     * @param token
     * @param dqc_account
     * @param dqc_pwd
     * @param verificationCode
     */
    private void ProRelation(String memberCode, String token, String dqc_account, String dqc_pwd, String verificationCode) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("SafetyCode", EncryptUtil.MD5(dqc_pwd));
        map.put("Code", verificationCode);
        map.put("BindMember", dqc_account);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_RELATION, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_RELATION, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("已成功绑定地球村账号");
                    startActivityWithoutExtras(DefendActivity.class);
                    finish();
                }else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });

    }

    /**
     * 校验信息
     *
     * @param dqc_account
     * @param dqc_pwd
     * @param verificationCode
     * @return
     */
    private boolean RegInfo(String dqc_account, String dqc_pwd, String verificationCode) {
        if (dqc_account.isEmpty()) {
            ToastUtils.showToast("地球村账号为空，请填写");
            return false;
        }
        if (dqc_pwd.isEmpty()) {
            ToastUtils.showToast("地球村账户密码为空，请填写");
            return false;
        }
        if (verificationCode.isEmpty()) {
            ToastUtils.showToast("验证码为空，请填写");
            return false;
        }
        return true;
    }

    /**
     * 发送验证码
     * @param memberCode
     * @param token
     */
    private void sendMsm(String memberCode, String token) {
        initProgressDialog("发送验证码中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                }else {
                    ToastUtils.showToast(mReturnBean.getData());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

}
