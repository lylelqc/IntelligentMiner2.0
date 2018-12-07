package com.sly.app.activity.mine;

import android.content.Intent;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/6/13.
 */
public  class UpDataPwActivity extends BaseActivity {
    private TextView tv_tetle, tv_main_right, tv_phone;
    private LinearLayout bt_back, ll_right;
    private EditText et_code, et_newpassword, et_twopassword;
    private Button bt_ryanzheng;
    private TimeUtils timeUtils;
    String phone;
    String Token;
    String  Mobile ;
    String pay = "";
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    private String MemberCode;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        tv_tetle = (TextView) findViewById(R.id.tv_main_title);
        tv_main_right = (TextView) findViewById(R.id.tv_main_right);
        bt_back = (LinearLayout) findViewById(R.id.btn_main_back);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        bt_ryanzheng = (Button) findViewById(R.id.bt_ryanzheng);
        et_code = (EditText) findViewById(R.id.et_code);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_twopassword = (EditText) findViewById(R.id.et_twopassword);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_main_right.setText("保存");
        tv_tetle.setText("重置密码");
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Mobile = SharedPreferencesUtil.getString(this, "Mobile");
        Intent intent = new Intent();
        intent = getIntent();
        pay = intent.getStringExtra("pay");
        phone = SharedPreferencesUtil.getString(this, "MemberCode", "");
        tv_phone.setText(phone);
        if ("login".equals(pay)) {
            tv_tetle.setText("修改登录密码");
        } else if ("pay".equals(pay)) {
            tv_tetle.setText("修改支付密码");
        }
        timeUtils = new TimeUtils(bt_ryanzheng, "获取验证码");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_updatapassword;
    }

    @Override
    protected void setListener() {
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_main_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("login".equals(pay)) {
                    updataLoginPassword();
                } else if ("pay".equals(pay)) {
                    UpPayPassword();
                }

            }
        });
        bt_ryanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeUtils.RunTimer();
                sendMsm();
            }
        });
    }

    private void UpPayPassword() {
        String code = et_code.getText().toString().trim();
        String pw = et_newpassword.getText().toString().trim();
        String pwtwo = et_twopassword.getText().toString().trim();
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
        Map map = new HashMap();
        map.put("PruseCode", MemberCode);
        map.put("MemberCode", MemberCode);
        map.put("Mobile", Mobile);
        map.put("PayPassword", EncryptUtil.MD5(pwtwo));
        map.put("Code", code);
        map.put("Token", Token);
        map.put("AppInfo", "");
        final String json = CommonUtils.GsonToJson(map);
        Logcat.i("修改密码" + json);
        HttpClient.postJson(NetWorkConstant.GetChanePayWD, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("statusCode " + statusCode);
                NetLogCat.W(NetWorkConstant.GetChanePayWD, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().equals("设置密码成功")) {
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

    private void updataLoginPassword() {
        String code = et_code.getText().toString().trim();
        String pw = et_newpassword.getText().toString().trim();
        String pwtwo = et_twopassword.getText().toString().trim();
        if (!pw.equals(pwtwo)) {
            ToastUtils.showToast("两次输入的密码不一致！");
            return;
        }
        if (code.isEmpty() || pw.isEmpty() || pwtwo.isEmpty()) {
            ToastUtils.showToast("信息填写不完整！");
            return;
        }
        Map map = new HashMap();
        map.put("MemberCode", MemberCode);
        map.put("Mobile", Mobile);
        map.put("Token", Token);
        map.put("MobileCode", code);
        map.put("NewPassword", EncryptUtil.MD5(pwtwo));
        map.put("AppInfo", "");
        final String json = CommonUtils.GsonToJson(map);
        Logcat.i("修改密码" + json);
        HttpClient.postJson(NetWorkConstant.GetChaneLoginWD, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.W(NetWorkConstant.GetChaneLoginWD, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().equals("修改密码成功")) {
                    ToastUtils.showToast("修改密码成功");
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

    private void sendMsm() {
        String MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        String Token = SharedPreferencesUtil.getString(this, "Token");
        initProgressDialog("发送验证码中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }
}
