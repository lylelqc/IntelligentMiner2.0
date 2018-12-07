package com.sly.app.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.CodeUtils;
import vip.devkit.library.Logcat;

/**
 */
public class ForgetStep1Activity extends BaseActivity {


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
    @BindView(R.id.et_tell)
    EditText etTell;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Button mBtnCode;

    boolean auth = true;
    private Bitmap bmp;
    private CodeUtils codeUtils;
    private String number, code, phone, email;
    private TimeUtils timeUtils;
    private String Key;
    private String token = "None", loginType = "None";

    @Override
    protected void initView() {
        tvMainTitle.setText("重置登录密码");
        codeUtils = new CodeUtils();
        code = codeUtils.createCode();
        bmp = codeUtils.createBitmap(code);
        setFocus();
        TextChange textChange = new TextChange();
        TextChange textChange1 = new TextChange();
        etTell.addTextChangedListener(textChange);
        etCode.addTextChangedListener(textChange);
        btnSubmit.addTextChangedListener(textChange1);
    }

    @Override
    protected void initData() {
        Key = SharedPreferencesUtil.getString(this, "Key","None");
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(llCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.acctivity_forget_step1;
    }

    @Override
    protected void setListener() {
    }

    private void setFocus() {
        etTell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    etTell.setFocusable(true);
                    etTell.setFocusableInTouchMode(true);
                } else {
                    etTell.clearFocus();
                }
            }
        });
        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    etCode.setFocusable(true);
                    etCode.setFocusableInTouchMode(true);
                } else {
                    etCode.clearFocus();
                }
            }
        });
    }

    private void getYanzhenma() {
        number = etTell.getText().toString().trim();
        if (!CommonUtils.isMobile(number) && !CommonUtils.isEmail(number)) {
            ToastUtils.showToast("请输入正确格式的手机号或邮箱号");
            auth = true;
            return;
        } else {
            timeUtils = new TimeUtils(btnSendCode, "获取验证码");//确保每次都改变
            timeUtils.RunTimer();
            requestAuth(number);
        }
    }


    @OnClick({R.id.btn_main_back, R.id.btn_send_code, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_send_code:
                timeUtils = new TimeUtils(mBtnCode, "发送验证码");
                getYanzhenma();
                break;
            case R.id.btn_submit:
                number = etTell.getText().toString().trim();
                code = etCode.getText().toString().trim();
                if (CommonUtils.isBlank(number) || CommonUtils.isBlank(code)) {
                    ToastUtils.showToast("请填写修改信息");
                } else {
                    if (!CommonUtils.isMobile(number) && !CommonUtils.isEmail(number)) {
                        ToastUtils.showToast("请输入正确格式的手机号或邮箱号");
                        return;
                    } else {
//                        if (CommonUtils.isMobile(number)) {
//                            phone = number;
//                            email = "None";
//                        } else {
//                            phone = "None";
//                            email = number;
//                        }
                        checkoutCode(number, code);
                    }
                }
                break;
        }
    }

    private void checkoutCode(final String number, final String code) {
        Map map = new HashMap();

        map.put("Token", token);
        map.put("LoginType", loginType);
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
                                SharedPreferencesUtil.putString(ForgetStep1Activity.this, "User", number);
                                Intent intent = new Intent();
                                intent.putExtra("User", number + "");
                                intent.putExtra("code", code + "");
                                intent.setClass(ForgetStep1Activity.this, ForgetStep2Activity.class);
                                startActivity(intent);
                                finish();
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

    /**
     * 获取验证码
     */
    private void requestAuth(String number) {
        Logcat.e("-----key----" + Key);
        Map map = new HashMap();

        map.put("Token", "None");
        map.put("LoginType", "None");
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
                    auth = true;
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
            if (etTell.getText().toString().isEmpty() || etCode.getText().toString().isEmpty()) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_b);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(false);
                return;
            }
            if (etTell.length() > 0 && etCode.length() > 0) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_a);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(true);
            }
        }

    }
}
