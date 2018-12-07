package com.sly.app.activity.mine;

import android.graphics.Bitmap;
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
public class

ForgetActivity extends BaseActivity {
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout mLlTitleRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.tv_mobilephone)
    TextView mTvMobilephone;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.rl_username)
    LinearLayout mRlUsername;
    @BindView(R.id.iv_lock)
    TextView mIvLock;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    private Bitmap bmp;
    private CodeUtils codeUtils;
    private String mobile, code, pwd;
    private TimeUtils timeUtils;

    @Override
    protected void initView() {
        mBtnTitleText.setText("修改登录密码");
        mTvTitleRight.setText("修改");
        codeUtils = new CodeUtils();
        code = codeUtils.createCode();
        bmp = codeUtils.createBitmap(code);
        setFocus();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void setListener() {
    }

    private void setFocus() {
        mEtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    mEtPhone.setFocusable(true);
                    mEtPhone.setFocusableInTouchMode(true);
                } else {
                    mEtPhone.clearFocus();
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
    }

    private void getYanzhenma() {
        mobile = mEtPhone.getText().toString().trim();
        if (mobile.isEmpty()) {
            ToastUtils.showToast("请输入电话号码");
            return;
        } else {
            timeUtils.RunTimer();
            sendMsm(mobile);
        }
    }


    @OnClick({R.id.btn_title_back, R.id.btn_code, R.id.ll_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.btn_code:
                timeUtils = new TimeUtils(mBtnCode, "发送验证码");
                getYanzhenma();
                break;
            case R.id.ll_title_right:
                mobile = mEtPhone.getText().toString().trim();
                code = mEtCode.getText().toString().trim();
                pwd = mEtPwd.getText().toString().trim();
                if (CommonUtils.isBlank(mobile)|| CommonUtils.isBlank(code)|| CommonUtils.isBlank(pwd)){
                    ToastUtils.showToast("请填写修改信息");
                }else {
                    if (!CommonUtils.isMobile(mobile)) {
                        ToastUtils.showToast("手机号码不合法！");
                    }else {
                        if (pwd.length() < 6) {
                            ToastUtils.showToast("密码必须超过6位");
                        }else {
                            upDataLoginPassword(mobile,mobile,code,pwd);
                        }
                    }
                }
                break;
        }
    }
    private void sendMsm(String MemberCode){
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
                if (mReturnBean.getStatus().equals("1")){
                    ToastUtils.showToast(mReturnBean.getMsg());
                }else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }
    private  void upDataLoginPassword(String MemberCode,String Token,String code,String pwd){
        Map map = new HashMap();
        map.put("MemberCode",MemberCode);
        map.put("Mobile",MemberCode);
        map.put("Token",Token);
        map.put("MobileCode",code);
        map.put("NewPassword", EncryptUtil.MD5(pwd));
        map.put("AppInfo","");
        final String json= CommonUtils.GsonToJson(map);
        Logcat.i("修改密码" + json);
        HttpClient.postJson(NetWorkConstant.GetChaneLoginWD,json,new HttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.W(NetWorkConstant.GetChaneLoginWD, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")&&mReturnBean.getData().equals("修改密码成功")){
                    ToastUtils.showToast("修改登陆密码成功");
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

}
