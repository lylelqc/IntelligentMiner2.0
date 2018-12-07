package com.sly.app.activity.sly.mine;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.activity.mine.AboutActivity;
import com.sly.app.activity.mine.CustomerServiceActivity;
import com.sly.app.activity.mine.ForgetStep1Activity;
import com.sly.app.base.BaseActivity;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 01 on 2016/10/31.
 */
public class SlySettingActivity extends BaseActivity {

    double size;
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
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.rl_update_login_pwd)
    RelativeLayout rlUpdateLoginPwd;
    @BindView(R.id.rl_set_pay_pw)
    RelativeLayout rlSetPayPw;
    @BindView(R.id.back_login)
    Button backLogin;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(llCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        File getCache = SlySettingActivity.this.getApplication().getCacheDir();
//        size = GetFileSizeUtil.getFileOrFilesSize(getCache.getPath(), GetFileSizeUtil.SIZETYPE_KB);
        tvMainTitle.setText("设置");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_sly_setting;
    }

    @Override
    protected void setListener() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (SharedPreferencesUtil.getString(SlySettingActivity.this, "MemberCode", "").equals("")) {
                intent.setClass(SlySettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backLogin() {
        final Dialog dialog = new Dialog(SlySettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.textView5)).setText("确定退出吗？");
        dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.GONE);
        dialog.show();

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_action);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                String name = SharedPreferencesUtil.getString(SlySettingActivity.this, "Name");
//                BackBena backBena = new BackBena();
//                backBena.setMemberCode(name);
//                Gson gson = new Gson();
//                String json = gson.toJson(backBena);
//                HttpClient.postJson(NetWorkConstant.USER_LOGIN_OUT, json, new HttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, String content) {
//                        super.onSuccess(statusCode, content);
//                        Logcat.e("-----------" + statusCode);
//                        LogCatW(NetWorkConstant.USER_GET_IMG, "", statusCode, content);
//                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
//                        if (content.indexOf("注销成功") != -1) {
//                            SharedPreferencesUtil.putString(SlySettingActivity.this, "Token", null);
//                            SharedPreferencesUtil.putString(SlySettingActivity.this, "User", null);
//                            startActivityWithoutExtras(MainActivity.class);
//                            finish();
//                        } else {
//                            startActivityWithoutExtras(MainActivity.class);
//                            finish();
//                        }
//                    }
//                });

                SharedPreferencesUtil.clearString(mContext, "Discount");
//                SharedPreferencesUtil.putString(mContext, "User", "None");
                SharedPreferencesUtil.putString(mContext, "Token", "None");
                SharedPreferencesUtil.putString(mContext, "FrSysCode", "None");
                SharedPreferencesUtil.putString(mContext, "FMasterCode", "None");
                /**角色Role**/
                SharedPreferencesUtil.putString(mContext,"LoginType","None");
                SharedPreferencesUtil.putString(mContext, "Key", "None");
                SharedPreferencesUtil.putString(SlySettingActivity.this, "Token", null);
//                SharedPreferencesUtil.putString(SlySettingActivity.this, "User", null);
                SharedPreferencesUtil.putString(mContext,"mineType","None");
                SharedPreferencesUtil.putString(mContext,"RegistrationID", "None");
                startActivityWithoutExtras(MainActivity.class);
                finish();
            }
        });
    }

    /**
     * 字体
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @OnClick({R.id.btn_main_back, R.id.back_login, R.id.rl_user_info, R.id.rl_update_login_pwd, R.id.rl_set_pay_pw, R.id.rl_about_us, R.id.rl_kefu})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_main_back:
                if (SharedPreferencesUtil.getString(SlySettingActivity.this, "MemberCode", "").equals("")) {
                    startActivityWithoutExtras(MainActivity.class);
                }
                finish();
                break;
            case R.id.back_login:
//                SharedPreferencesUtil.clearString(this, "Discount");
//                SharedPreferencesUtil.putString(mContext, "User", "None");
//                SharedPreferencesUtil.putString(mContext, "Token", "None");
//                SharedPreferencesUtil.putString(mContext, "FrSysCode", "None");
//                SharedPreferencesUtil.putString(mContext, "FMasterCode", "None");
                /**角色Role**/
//                SharedPreferencesUtil.putString(mContext,"LoginType","None");
//                SharedPreferencesUtil.putString(mContext, "Key", "None");
                backLogin();
                break;
            case R.id.rl_user_info:
                intent.setClass(this, UserInfoEditActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_update_login_pwd:
                intent.setClass(this, ForgetStep1Activity.class);
                startActivity(intent);
                break;
            case R.id.rl_set_pay_pw:
                intent.setClass(this, SetPayPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_us:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_kefu:
                intent.setClass(this, CustomerServiceActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class BackBena {
        String MemberCode;
        String Token;

        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String memberCode) {
            MemberCode = memberCode;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            Token = token;
        }
    }

    /**
     * get App versionName
     *
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
