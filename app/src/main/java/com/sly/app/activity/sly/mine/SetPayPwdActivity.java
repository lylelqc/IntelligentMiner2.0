package com.sly.app.activity.sly.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.TimeUtils;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/6/13.
 */
public class SetPayPwdActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout llCommLayout;
    @BindView(R.id.et_newpassword)
    EditText etNewpassword;
    @BindView(R.id.rl_newpassword)
    RelativeLayout rlNewpassword;
    @BindView(R.id.et_twopassword)
    EditText etTwopassword;
    @BindView(R.id.rl_twopassword)
    RelativeLayout rlTwopassword;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private TimeUtils timeUtils;
    String pay = "";
    private String User, Token, LoginType, Key, SysCode, mTarget;
    private String code,pw,pwtwo;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(llCommLayout).init();
    }

    @Override
    protected void initView() {
        tvMainTitle.setText("设置安全密码");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");

        String SysCodekey = LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode";
        SysCode = SharedPreferencesUtil.getString(this, SysCodekey, "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
//        if (LoginType.equals("Miner")) {
//            mTarget = SharedPreferencesUtil.getString(this, "Phone", "None");
//        } else {
//            mTarget = SharedPreferencesUtil.getString(this, "Email", "None");
//        }

        timeUtils = new TimeUtils(btnSendCode, "获取验证码");
        code = etCode.getText().toString().trim();
        pw = etNewpassword.getText().toString().trim();
        pwtwo = etTwopassword.getText().toString().trim();
        TextChange textChange = new TextChange();
        etTwopassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pw.length() != 6) {
                    ToastUtils.showToast("请设置6位支付密码");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pw.equals(pwtwo)) {
                    ToastUtils.showToast("两次输入的密码不一致！");
                }
            }
        });
        etCode.addTextChangedListener(textChange);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    protected void setListener() {
        btnMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYanzhenma();
            }
        });
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String code = etCode.getText().toString().trim();
                String pw = etNewpassword.getText().toString().trim();
                String pwtwo = etTwopassword.getText().toString().trim();
                if (!pw.equals(pwtwo)) {
                    ToastUtils.showToast("两次输入的密码不一致！");
                    return;
                }

                if (code.isEmpty() || pw.isEmpty() || pwtwo.isEmpty()) {
                    ToastUtils.showToast("信息填写不完整！");
                    return;
                }
                if (pw.length() != 6) {
                    ToastUtils.showToast("请设置6位支付密码");
                    return;
                }
                checkoutCode(pw,User, code);
                break;
        }
    }

    private void checkoutCode(final String pwd,final String number, final String code) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Message.002");
        map.put("User", number);
        map.put("target", number);
        map.put("code", code);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        final ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if (returnBean.getStatus().equals("1")) {
                            if (returnBean.getMsg().equals("验证码不正确")){
                                ToastUtils.showToast(returnBean.getMsg());
                            }else{
                                UpPayPassword(pwd);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }
    private void UpPayPassword(String pwd) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Member.004");
        map.put("User", User);
        map.put("target", User);
        map.put("newPassword", pwd);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("修改密码" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("statusCode " + statusCode);
                NetLogCat.W(NetWorkCons.BASE_URL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getMsg().equals("成功")) {
                    ToastUtils.showToast("修改密码成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                ToastUtils.showToast("修改支付密码失败，请检查网络");
            }
        });

    }

    private void getYanzhenma() {
        timeUtils = new TimeUtils(btnSendCode, "获取验证码");//确保每次都改变
        timeUtils.RunTimer();
        requestAuth(User);
    }

    /**
     * 获取验证码
     */
    private void requestAuth(String number) {
        Logcat.e("-----key----" + Key);
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Message.004");
        map.put("User", number);
        map.put("target", number);

        Logcat.i("获取验证码信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        Gson gson = new Gson();
        final String json = gson.toJson(jsonMap);
        Logcat.e("提交的参数：" + json + "地址:" + NetWorkCons.BASE_URL);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                Logcat.e("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + CommonUtils.proStr(content));
                ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (returnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("验证码已发出，请接收信息");
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnSendCode.setText("重发");
                        }
                    });
                } else {
                    ToastUtils.showToast(returnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                ToastUtils.showToast("请求失败");
                timeUtils.StopTimer();
                Logcat.e("错误信息：" + e);
            }
        });
    }

    //EditText的监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etNewpassword.getText().toString().isEmpty() || etTwopassword.getText().toString().isEmpty()||etCode.getText().toString().isEmpty()) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_b);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(false);
                return;
            }
            if (etNewpassword.length() > 0 &&
                    etTwopassword.length() > 0 &&
                    etCode.length() > 0) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_a);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(true);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

