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

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.Constants;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.RecommandBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.TimeUtils;
import com.sly.app.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * 文 件 名: JoinLevelActivity
 * 创 建 人: By k
 * 创建日期: 2017/12/11 17:39
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class JoinLevelActivity extends BaseActivity {

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
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.sp_join_2)
    Spinner mSpJoin2;
    @BindView(R.id.et_join_member)
    TextView mEtJoinMember;
    @BindView(R.id.et_join_code)
    EditText mEtJoinCode;
    @BindView(R.id.btn_send_code)
    Button mBtnSendCode;
    @BindView(R.id.et_join_sbm)
    TextView mEtJoinSbm;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    TimeUtils timeUtils;
    private String MemberCode, Token;
    private String type;
    private String input, Name, PayPrice;
    private Handler handler = new Handler();
    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            if (mEtJoinMember.isFocused()) {
                Logcat.i("isFocused 1" + mEtJoinMember.isFocused());
            } else {
                Logcat.i("isFocused 2" + mEtJoinMember.isFocused());
                getSbm(input);
            }
        }
    };


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_join_level;
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("加盟晋升");
        mEtJoinMember.setText(MemberCode);
        getSbm(MemberCode);
        mEtJoinMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                input = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 1000);
            }
        });
        if (mEtJoinMember.isFocused()) {
        } else {
            if (delayRun != null) {
                handler.removeCallbacks(delayRun);
            }
            input = mEtJoinMember.getText().toString();
            handler.postDelayed(delayRun, 1000);
        }

    }

    @Override
    protected void setListener() {
        mSpJoin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] join = getResources().getStringArray(R.array.sp_join_2);
                Logcat.i("你点击的是:" + join[position]);
                type = join[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @OnClick({R.id.btn_main_back, R.id.btn_send_code, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_send_code:
                String mEtMember = mEtJoinMember.getText().toString();
                if (CommonUtils.isBlank(mEtMember)) {
                    ToastUtils.showToast("请填写加盟会员");
                } else {
                    timeUtils = new TimeUtils(mBtnSendCode, "发送验证码");
                    timeUtils.RunTimer();
                    sendMsm(mEtMember);
                }
                break;
            case R.id.btn_submit:
                regJoinData();
                break;
        }
    }

    /**
     * 校验参数
     *
     * @param
     */
    private void regJoinData() {
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
        String id = mEtJoinMember.getText().toString();
        String sbm = mEtJoinSbm.getText().toString();
        String code = mEtJoinCode.getText().toString();
        Logcat.i("--------" + type + "/" + id + "/" + code + "/" + sbm);
        if (CommonUtils.isBlank(type) || CommonUtils.isBlank(id) || CommonUtils.isBlank(code) || CommonUtils.isBlank(sbm)) {
            ToastUtils.showToast("加盟信息不能为空");
        } else {
            ProJoin(MemberCode, Token, type, id, code);
        }
    }

    /**
     * @param type
     * @param member
     * @param code
     */
    private void ProJoin(String memberCode, String token, String type, String member, String code) {
        initProgressDialog("处理中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("JoinMember", member);
        map.put("Code", code);
        if (type.equals("请选择礼包类型")) {
            dismissProgressDialog();
            ToastUtils.showToast("请选择礼包类型");
            return;
        } else if (type.equals("VIP会员")) {
            map.put("JoinLevel", "04");
            PayPrice = "300";
        } else if (type.equals("品牌大使")) {
            map.put("JoinLevel", "05");
            PayPrice = "1500";
        } else if (type.equals("金牌代理")) {
            map.put("JoinLevel", "06");
            PayPrice = "7500";
        } else if (type.equals("钻石代理")) {
            map.put("JoinLevel", "07");
            PayPrice = "15000";
        } else if (type.equals("合伙人")) {
            map.put("JoinLevel", "08");
            PayPrice = "100000";
        }
        final String json = CommonUtils.GsonToJson(map);
        Logcat.i("json -:" + json);
        HttpClient.postJson(NetWorkConstant.USER_JOIN_ORDER, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_JOIN_ORDER, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", mReturnBean.getData());
                    mBundle.putString("MallType", Constants.MAIL_TYPE_WS);
                    mBundle.putString("PayPrice", PayPrice);
                    startActivityWithExtras(JoinOrderPayActivity.class, mBundle);
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

    private void getSbm(String member) {
        initProgressDialog("处理中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", member);
        map.put("UserMember", member);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.W(NetWorkConstant.USER_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    RecommandBean mBean = JSON.parseObject(mReturnBean.getData(), RecommandBean.class);
                    mTvName.setText(mBean.getOwnMemberName());
                    mEtJoinSbm.setText(mBean.getRecommandCode()+"("+mBean.getRecommandName()+")");

                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
        dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEtJoinMember.isFocused()) {
        } else {
            if (delayRun != null) {
                handler.removeCallbacks(delayRun);
            }
            input = mEtJoinMember.getText().toString();
            handler.postDelayed(delayRun, 1000);
        }
    }

    /**
     * 发送验证码
     *
     * @param MemberCode
     */
    private void sendMsm(String MemberCode) {
        initProgressDialog("发送验证码中...", true);
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
                    dismissProgressDialog();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                    dismissProgressDialog();
                }
                dismissProgressDialog();
                return false;
            }
        });
        dismissProgressDialog();
    }


}
