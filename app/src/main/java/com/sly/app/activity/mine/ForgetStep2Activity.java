package com.sly.app.activity.mine;

import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class ForgetStep2Activity extends BaseActivity {
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
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String userNumber;
    private String mCode;
    private String Key;
    private String pw,pwtwo;

    @Override
    protected void initData() {
        userNumber = getIntent().getStringExtra("User");
        mCode = getIntent().getStringExtra("code");
        Key = SharedPreferencesUtil.getString(this, "Key","None");
        setFocus();
        ForgetStep2Activity.TextChange textChange = new ForgetStep2Activity.TextChange();
        ForgetStep2Activity.TextChange textChange1 = new ForgetStep2Activity.TextChange();
        etPwd.addTextChangedListener(textChange);
        etPwd2.addTextChangedListener(textChange);
        btnSubmit.addTextChangedListener(textChange1);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forget_step2;
    }

    @Override
    protected void initView() {


    }
    private void setFocus() {
        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    etPwd.setFocusable(true);
                    etPwd.setFocusableInTouchMode(true);
                } else {
                    etPwd.clearFocus();
                }
            }
        });
        etPwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    etPwd2.setFocusable(true);
                    etPwd2.setFocusableInTouchMode(true);
                } else {
                    etPwd2.clearFocus();
                }
            }
        });
    }
    @Override
    protected void setListener() {

    }
    @OnClick({R.id.btn_main_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_submit:
                pw = etPwd.getText().toString().trim();
                pwtwo = etPwd2.getText().toString().trim();
                if (!pw.equals(pwtwo)) {
                    ToastUtils.showToast("两次输入的密码不一致！");
                    return;
                }
                upDataLoginPassword(userNumber,etPwd.getText().toString().trim(),mCode);
                break;
        }
    }
    private void upDataLoginPassword(String Number, String pwd, String code) {
        Map map = new HashMap();

        map.put("Token", "None");
        map.put("LoginType", "None");
        map.put("Rounter", "Member.003");
        map.put("User", Number);
        map.put("target", Number);
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
                NetLogCat.W(NetWorkCons.BASE_URL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")&& mReturnBean.getMsg().equals("成功")) {
                    ToastUtils.showToast("修改密码成功");
                    startActivityWithoutExtras(LoginActivity.class);
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                ToastUtils.showToast("修改登录密码失败，请检查网络");
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
            if (etPwd.getText().toString().isEmpty() || etPwd2.getText().toString().isEmpty()) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_b);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(false);
                return;
            }
            if (etPwd.length() > 0 && etPwd2.length() > 0) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_a);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(true);
            }
        }

    }
}
