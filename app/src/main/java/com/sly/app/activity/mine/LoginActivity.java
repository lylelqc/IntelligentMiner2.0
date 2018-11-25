package com.sly.app.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.KgFullInfoBean;
import com.sly.app.model.sly.LoginInfoBean;
import com.sly.app.model.sly.MinerMasterInfoBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class LoginActivity extends BaseActivity {
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
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.rl_username)
    RelativeLayout mRlUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView mTvForgetPwd;
    @BindView(R.id.tv_forget_account)
    TextView mTvForgetAccount;
    private String appinfo = null;
    private Intent intent;
    private ReturnBean bean;
    private String MemberCode;
    private String mRounter = "";

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).keyboardEnable(true).init();
    }

    @Override
    protected void initData() {
        intent = getIntent();
        if (intent != null) {
            Bundle mBundle = intent.getExtras();
            if (mBundle != null) {
                MemberCode = mBundle.getString("MemberCode");
            }
        }else{
            MemberCode = SharedPreferencesUtil.getString(LoginActivity.this,"User",null);
        }
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("登录");
        MemberCode = SharedPreferencesUtil.getString(LoginActivity.this,"User",null);
        if (!CommonUtils.isBlank(MemberCode)&&!"None".equals(MemberCode)) {
            mEtUsername.setText(MemberCode);
            mEtUsername.clearFocus();
        }
        TextChange textChange = new TextChange();
        mEtUsername.addTextChangedListener(textChange);
        mEtPassword.addTextChangedListener(textChange);
        mBtLogin.addTextChangedListener(textChange);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setListener() {
    }


    /**
     * 用户登录
     */
    private void login() {
        final String MemberCode = mEtUsername.getText().toString().trim();
        String Password = mEtPassword.getText().toString().trim();
        SharedPreferencesUtil.putString(this, "User", MemberCode);
        if (TextUtils.isEmpty(MemberCode) || TextUtils.isEmpty(Password)) {
            ToastUtils.showToast("用户名或密码不能为空哦");
            return;
        }
//        if (!CommonUtils.isMobile(MemberCode) || !CommonUtils.isEmail(MemberCode)) {
//            ToastUtils.showToast("请输入正确格式的手机号或邮箱号");
//            return;
//        }
        Map map = new HashMap();

        map.put("Token", "None");
        map.put("LoginType", "None" );
//        map.put("Rounter", "Member.002");
        map.put("Rounter", "Sly.009");
        map.put("User", MemberCode);
        map.put("sys", MemberCode);
        map.put("password", Password);
        map.put("ip", "None");
        map.put("remark", "None");

//        Logcat.i("\n返回值："+ ApiSIgnUtil.init(this).getSign(map,"5110F191-FB00-45B8-814D-C7328CCAFC73"));
//        Logcat.i("\n返回值："+ ApiSIgnUtil.init(this).getSign(map,"5110F191-FB00-45B8-814D-C7328CCAFC73")+"\nMD5："+ EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map,"5110F191-FB00-45B8-814D-C7328CCAFC73")));
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, "None")));

        Gson gson = new Gson();
        final String json = gson.toJson(mapJson);
        Logcat.i("提交的参数：" + json + " 地址 :" + NetWorkCons.BASE_URL);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + content + " headers :" + headers);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                        try {
                            bean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (("1").equals(bean.getStatus()) && ("成功").equals(bean.getMsg())) {
                                ToastUtils.showToast("登录成功");
                                LoginInfoBean loginInfoBean = JSON.parseObject(bean.getData(), LoginInfoBean.class);
                                SharedPreferencesUtil.putString(LoginActivity.this, "User", loginInfoBean.getUser());
                                SharedPreferencesUtil.putString(LoginActivity.this, "Token", loginInfoBean.getToken());
                                SharedPreferencesUtil.putString(LoginActivity.this, "FrSysCode", loginInfoBean.getFrSysCode());
                                SharedPreferencesUtil.putString(LoginActivity.this, "FMasterCode", loginInfoBean.getFMasterCode());
                                SharedPreferencesUtil.putString(LoginActivity.this, "PersonSysCode", loginInfoBean.getPersonSysCode());
                                SharedPreferencesUtil.putString(LoginActivity.this, "Key", loginInfoBean.getKey());
                                /**角色Role**/
//                                if(loginInfoBean.getRole().equals("MineMaster")){
//                                    SharedPreferencesUtil.putString(LoginActivity.this,"LoginType","MinerMaster");
//                                }else{
                                SharedPreferencesUtil.putString(LoginActivity.this,"LoginType",loginInfoBean.getRole());
                                SharedPreferencesUtil.putString(LoginActivity.this,"mineType",loginInfoBean.getRole());
//                                }


                                String sysCode = "None";
                                String role = SharedPreferencesUtil.getString(mContext, "LoginType","None");
                                if(role.equals("Miner")){
                                    sysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode","None");
                                }else if(role.equals("MinerMaster")){
                                    sysCode = SharedPreferencesUtil.getString(mContext, "FMasterCode","None");
                                }else{
                                    sysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode","None");
                                }
                                getUserInfo(mContext,SharedPreferencesUtil.getString(mContext, "LoginType","None"), SharedPreferencesUtil.getString(mContext, "User","None"),
                                        sysCode, SharedPreferencesUtil.getString(mContext, "Key","None"),
                                        SharedPreferencesUtil.getString(mContext, "Token","None"));
//                                RxBus.getInstance().post(BusEvent.UPDATE_HOSTING_DATA);
                                EventBus.getDefault().post(new PostResult(EventBusTags.UPDATE_HOSTING_DATA));
                                EventBus.getDefault().post(new PostResult(EventBusTags.LOGIN_SUCCESS));
                            } else {
                                SharedPreferencesUtil.putString(LoginActivity.this, "User", null);
                                SharedPreferencesUtil.putString(LoginActivity.this, "Token", null);
                                SharedPreferencesUtil.putString(LoginActivity.this, "FrSysCode", null);
                                SharedPreferencesUtil.putString(LoginActivity.this, "FMasterCode", null);
                                SharedPreferencesUtil.putString(LoginActivity.this, "PersonSysCode", null);

                                /**角色Role**/
                                SharedPreferencesUtil.putString(LoginActivity.this,"LoginType",null);
                                SharedPreferencesUtil.putString(LoginActivity.this,"mineType",null);
                                SharedPreferencesUtil.putString(LoginActivity.this, "Key", null);
                                ToastUtils.showToast("请确认账号密码是否正确");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            SharedPreferencesUtil.putString(LoginActivity.this, "User", null);
                            SharedPreferencesUtil.putString(LoginActivity.this, "Token", null);
                            SharedPreferencesUtil.putString(LoginActivity.this, "FrSysCode", null);
                            SharedPreferencesUtil.putString(LoginActivity.this, "FMasterCode", null);
                            SharedPreferencesUtil.putString(LoginActivity.this, "PersonSysCode", null);
                            /**角色Role**/
                            SharedPreferencesUtil.putString(LoginActivity.this,"LoginType",null);
                            SharedPreferencesUtil.putString(LoginActivity.this,"mineType",null);
                            SharedPreferencesUtil.putString(LoginActivity.this, "Key", null);
                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        ToastUtils.showToast("登录失败，请检查网络");
                        SharedPreferencesUtil.putString(LoginActivity.this, "User", null);
                        SharedPreferencesUtil.putString(LoginActivity.this, "Token", null);
                        SharedPreferencesUtil.putString(LoginActivity.this, "FrSysCode", null);
                        SharedPreferencesUtil.putString(LoginActivity.this, "FMasterCode", null);
                        SharedPreferencesUtil.putString(LoginActivity.this, "PersonSysCode", null);
                        /**角色Role**/
                        SharedPreferencesUtil.putString(LoginActivity.this,"LoginType",null);
                        SharedPreferencesUtil.putString(LoginActivity.this, "Key", null);
                        Logcat.e("错误信息：" + request.toString() + "/" + e);
                    }
                }
        );

    }

    //V8955979
    private void getUserInfo(final Context mContext, final String loginType, final String user, final String sysCode, final String userKey, String token) {
        Map map = new HashMap();
        if (loginType.equals("MinerMaster")){
            mRounter = "MineMaster.002";
            map.put("mineMasterCode", sysCode);
        }else{
            mRounter = "Miner.002";
            map.put("minerSysCode", sysCode);
        }

        map.put("Token", token);
        map.put("LoginType", loginType);
        map.put("Rounter", mRounter);
        map.put("User", user);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, userKey)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        final ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                            if (returnBean.getStatus().equals("1")&& returnBean.getMsg().equals("成功")) {
                                if (loginType.equals("Miner")) {
                                    KgFullInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), KgFullInfoBean.class);
                                    SharedPreferencesUtil.putString(LoginActivity.this,"UserHeadImg",userInfoBean.getF18_ImageURl());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"NickName",userInfoBean.getF18_NickName());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Name",userInfoBean.getF20_Name());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Phone",userInfoBean.getF20_Mobile());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Email",userInfoBean.getF20_Email());
                                } else {
                                    MinerMasterInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), MinerMasterInfoBean.class);
                                    SharedPreferencesUtil.putString(LoginActivity.this,"UserHeadImg",userInfoBean.getF35_Pic());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"NickName",userInfoBean.getF35_NickName());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Name",userInfoBean.getF36_Name());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Phone",userInfoBean.getF36_Mobile());
                                    SharedPreferencesUtil.putString(LoginActivity.this,"Email",userInfoBean.getF36_Email());
                                }
                                startActivityWithoutExtras(MainActivity.class);
                                finish();
                            }else{
                                SharedPreferencesUtil.putString(LoginActivity.this,"UserHeadImg",null);
                                SharedPreferencesUtil.putString(LoginActivity.this,"NickName",null);
                                SharedPreferencesUtil.putString(LoginActivity.this,"Name",null);
                                SharedPreferencesUtil.putString(LoginActivity.this,"Phone",null);
                                SharedPreferencesUtil.putString(LoginActivity.this,"Email",null);
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


    @OnClick({R.id.btn_main_back, R.id.tv_forget_pwd, R.id.tv_forget_account, R.id.tv_register, R.id.bt_login})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_main_back:
                startActivityWithoutExtras(MainActivity.class);
                finish();
                break;
            case R.id.tv_register:
//                intent.setClass(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_forget_pwd:
//                intent.setClass(LoginActivity.this, ForgetStep1Activity.class);
//                startActivity(intent);
                break;
            case R.id.tv_forget_account:
//                intent.setClass(LoginActivity.this, ForgetMemberActivity.class);
//                startActivity(intent);
                break;
            case R.id.bt_login:
                login();
                break;
        }
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
            if (mEtPassword.getText().toString().isEmpty() || mEtUsername.getText().toString().isEmpty()) {
                mBtLogin.setBackgroundResource(R.drawable.bg_login_btn_b);
                mBtLogin.setTextColor(Color.WHITE);
                return;
            }
            if (mEtUsername.length() > 0 &&
                    mEtPassword.length() > 0) {
                mBtLogin.setBackgroundResource(R.drawable.bg_login_btn_a);
                mBtLogin.setTextColor(Color.WHITE);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivityWithoutExtras(MainActivity.class);
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
