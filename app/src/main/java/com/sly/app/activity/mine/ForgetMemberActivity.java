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
package com.sly.app.activity.mine;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.sly.app.utils.TimeUtils;
import com.sly.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;

/**
 * 文 件 名: ForgetMemberActivity
 * 创 建 人: By k
 * 创建日期: 2018/1/8 14:55
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 说    明：
 * 修改时间：
 * 修改备注：
 */
public class ForgetMemberActivity extends BaseActivity {

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
    @BindView(R.id.tv_mobilephone)
    TextView mTvMobilephone;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.rl_username)
    LinearLayout mRlUsername;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    @BindView(R.id.tv_member_list)
    TextView mTvMemBerList;
    @BindView(R.id.tv_tag_title)
    TextView mTvTagTitle;
    @BindView(R.id.sc_view)
    ScrollView mScView;
    TimeUtils timeUtils;
    private String Mobile, Code;

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
        return R.layout.activity_forget_member;
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("找回账号");
        mTvMainRight.setText("找回");

    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.btn_main_back, R.id.ll_right, R.id.btn_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.ll_right:
                Code = mEtCode.getText().toString();
                if (!CommonUtils.isBlank(Mobile)) {
                    if (!Code.isEmpty()) {
                        getData(Mobile, Code);
                    } else {
                        ToastUtils.showToast("请输入验证码");
                        return;
                    }
                } else {
                    ToastUtils.showToast("请输入电话号码并获取验证码");
                    return;
                }
                break;
            case R.id.btn_code:
                Mobile = mEtPhone.getText().toString().trim();
                if (Mobile.isEmpty()) {
                    ToastUtils.showToast("请输入电话号码");
                    return;
                } else {
                    timeUtils = new TimeUtils(mBtnCode, "发送验证码");
                    timeUtils.RunTimer();
                    sendMsm(Mobile);
                }
                break;
        }
    }

    private void sendMsm(String MemberCode) {
        initProgressDialog("发送验证码中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getMsg());
                } else {
                    mTvTagTitle.setText(mReturnBean.getMsg());
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

    private void getData(String Mobile, String Code) {
        initProgressDialog("发送验证码中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("Mobile", Mobile);
        map.put("Code", Code);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_MEMBER, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_MEMBER, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    timeUtils.StopTimer();
                    mTvTagTitle.setText("该手机号注册的会员列表(长按复制)");
                    String str = mReturnBean.getData().replace("[", "").replace("]", "");
                    List<String> member = new ArrayList<>();
                    if (str.contains(",")) {
                        String[] list = str.split(",");
                        for (int i = 0; i < list.length; i++) {
                            member.add("会员账号" + (i + 1) + ":\t" + list[i] + "\n\n");
                        }
                    } else {
                        member.add("会员账号" +1 + ":\t" + str + "\n\n");
                    }
                    String list = member.toString().replace("[", "").replace("]", "").replace(",","");
                    mTvMemBerList.setText(list);
                } else {
                    mTvTagTitle.setText(mReturnBean.getMsg());
                    ToastUtils.showToast(mReturnBean.getMsg());
                    timeUtils.StopTimer();
                }
                dismissProgressDialog();
                return false;
            }
        });
    }
}
