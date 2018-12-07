package com.sly.app.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;


/**
 * Created by 01 on 2016/10/17.
 */
public class RegisterActivity extends BaseActivity {
    boolean auth = true;
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
    @BindView(R.id.et_tell)
    EditText mEtTell;
    @BindView(R.id.et_tag_1)
    EditText mEtTag1;
    @BindView(R.id.et_tag_2)
    EditText mEtTag2;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_send_code)
    Button mBtnSendCode;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    @BindView(R.id.iv_consent)
    ImageView mIvConsent;
    @BindView(R.id.tv_consent)
    TextView mTvConsent;
    @BindView(R.id.tv_tag_name)
    TextView mTvTagName;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    private String phone = "None", name = "", password = "", yanzheng = "", number = "", email = "None";
    private TimeUtils timeUtils;
    private int staut = 0;
    private String regTag1, regTag2;
    private String[] data;
    private String Key;
    private String input, Name, PayPrice;
    private Handler handler = new Handler();
    private int count = 1;
    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {

            if (null != mEtTag1 && mEtTag1.isFocused()) {
                Logcat.i("isFocused 1" + mEtTag1.isFocused());
            } else {
                Logcat.i("isFocused 2" + mEtTag1.isFocused());
//                getSbm(input);
            }
        }
    };

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).keyboardEnable(true).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("立即注册");
        TextChange textChange = new TextChange();
        TextChange textChange1 = new TextChange();
        mEtTell.addTextChangedListener(textChange);
        mEtCode.addTextChangedListener(textChange);
        mEtPwd.addTextChangedListener(textChange);
        mBtnSubmit.addTextChangedListener(textChange1);
    }

    @Override
    protected void initData() {
        verifyStoragePermissions(RegisterActivity.this);
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        setFocus();

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setListener() {
    }

    private void setFocus() {
        mEtTell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    mEtTell.setFocusable(true);
                    mEtTell.setFocusableInTouchMode(true);
                } else {
                    mEtTell.clearFocus();
                }
            }
        });
        mEtCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    mEtCode.setFocusable(true);
                    mEtCode.setFocusableInTouchMode(true);
                } else {
                    mEtCode.clearFocus();
                }
            }
        });
        mEtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    mEtPwd.setFocusable(true);
                    mEtPwd.setFocusableInTouchMode(true);
                } else {
                    mEtPwd.clearFocus();
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void requestAuth() {
        Logcat.e("-----b----" + i++);
        Logcat.e("-----key----" + Key);
        Map map = new HashMap();

        map.put("Token", "None");
        map.put("LoginType", "None");
        map.put("Rounter", "Message.004");
        map.put("User", phone);
        map.put("target", phone);

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
                            mBtnSendCode.setText("重发");
                        }
                    });
                } else {
                    ToastUtils.showToast(returnBean.getMsg());
                    auth = true;
                }
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

    /**
     * 用户点击注册
     */
    private void register() {
        number = mEtTell.getText().toString().trim();
        password = mEtPwd.getText().toString().trim();
        yanzheng = mEtCode.getText().toString().trim();
        if (number.isEmpty() || password.isEmpty() || yanzheng.isEmpty()) {
            ToastUtils.showToast("姓名、手机号、密码、验证码不能为空");
            return;
        }
        if (!CommonUtils.isMobile(number) && !CommonUtils.isEmail(number)) {
            ToastUtils.showToast("请输入正确格式的手机号或邮箱号");
            return;
        }
        if (CommonUtils.isMobile(number)) {
            phone = number;
            email = "None";
        } else if (CommonUtils.isEmail(number)) {
            phone = "None";
            email = number;
        }


        String pwdRegex = "[0-9A-Za-z]{6,12}";
        if (!password.matches(pwdRegex)) {
            ToastUtils.showToast("密码为6-12位,只能以字母开头");
            return;
        }
        if (password.length() < 6) {
            ToastUtils.showToast("密码不能少于6位");
            return;
        }
        if (staut == 0) {
            ToastUtils.showToast("需同用户协议注册协议");
            return;
        }

        Map map = new HashMap();

        map.put("Token", "None");
        map.put("LoginType", "None");
        map.put("Rounter", "Member.001");
        map.put("User", phone);
        map.put("mobile", phone);
        map.put("email", email);
        map.put("password", password);
        map.put("registerType", "00");
        map.put("remark", "None");
        map.put("code", yanzheng);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        Gson gson = new Gson();
        final String json = gson.toJson(jsonMap);
        Logcat.i("注册提交的参数：" + json + " 地址：" + NetWorkCons.BASE_URL);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                String backlogJsonStr = content;
                String backlogJsonStrTmp = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStrTmp.substring(1, backlogJsonStrTmp.length() - 1);
                System.out.println("result----------" + backlogJsonStr);
                Log.e("result-----", backlogJsonStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        ToastUtils.showToast("注册成功");
                        Bundle mBundle = new Bundle();
                        mBundle.putString("MemberCode", number);
                        SharedPreferencesUtil.putString(mContext,"User",number);
                        startActivityWithExtras(LoginActivity.class, mBundle);
                        finish();
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 动态添加权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);

        }
    }

    /**
     * 动态请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
        }
    }

    int i = 1;

    @OnClick({R.id.btn_main_back, R.id.btn_send_code, R.id.tv_consent, R.id.iv_consent, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_send_code:
                phone = mEtTell.getText().toString();
                if (phone.isEmpty()) {
                    ToastUtils.showToast("请输入手机号码");
                } else {
                    if (!CommonUtils.isMobile(phone) && !CommonUtils.isEmail(phone)) {
                        ToastUtils.showToast("请输入正确格式的手机号或邮箱号");
                        auth = true;
                    } else {
                        timeUtils = new TimeUtils(mBtnSendCode, "获取验证码");//确保每次都改变
                        timeUtils.RunTimer();
                        requestAuth();
                    }
                }
                break;
            case R.id.tv_consent:
                startActivityWithoutExtras(ProtocolActivity.class);
                break;
            case R.id.iv_consent:
                if (staut == 0) {
                    Drawable drawable = getResources().getDrawable(R.drawable.register_choice);
                    mIvConsent.setImageDrawable(drawable);
                    staut = 1;
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.register_unselected);
                    mIvConsent.setImageDrawable(drawable);
                    staut = 0;
                }
                break;
            case R.id.btn_submit:
                register();
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
            if (mEtTell.getText().toString().isEmpty() || mEtCode.getText().toString().isEmpty() || mEtPwd.getText().toString().isEmpty()) {
                mBtnSubmit.setBackgroundResource(R.drawable.bg_login_btn_b);
                mBtnSubmit.setTextColor(Color.WHITE);
                mBtnSubmit.setClickable(false);
                return;
            }
            if (mEtTell.length() > 0 && mEtCode.length() > 0 && mEtPwd.length() > 0) {
                mBtnSubmit.setBackgroundResource(R.drawable.bg_login_btn_a);
                mBtnSubmit.setTextColor(Color.WHITE);
                mBtnSubmit.setClickable(true);
            }
        }

    }

}
