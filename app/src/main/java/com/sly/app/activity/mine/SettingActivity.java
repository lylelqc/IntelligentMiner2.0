package com.sly.app.activity.mine;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.GetFileSizeUtil;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * Created by 01 on 2016/10/31.
 */
public class SettingActivity extends BaseActivity {
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
    @BindView(R.id.rl_defend)
    RelativeLayout mRlDefend;
    @BindView(R.id.tv_cache)
    TextView mTvCache;
    @BindView(R.id.cache)
    RelativeLayout mCache;
    @BindView(R.id.tv_about)
    TextView mTvAbout;
    @BindView(R.id.rl_about)
    RelativeLayout mRlAbout;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.rl_version)
    RelativeLayout mRlVersion;
    @BindView(R.id.back_login)
    Button mBackLogin;
    double size;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        File getCache = SettingActivity.this.getApplication().getCacheDir();
//        size = GetFileSizeUtil.getFileOrFilesSize(getCache.getPath(), GetFileSizeUtil.SIZETYPE_KB);
        mTvMainTitle.setText("设置");
        mTvCache.setText(size + "KB");
        mTvVersionName.setText("当前版本:" + getVersionName(this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setListener() {
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (SharedPreferencesUtil.getString(SettingActivity.this, "MemberCode", "").equals("")) {
                intent.setClass(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backLogin() {
        final Dialog dialog = new Dialog(SettingActivity.this);
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
                String name = SharedPreferencesUtil.getString(SettingActivity.this, "MemberCode");
                BackBena backBena = new BackBena();
                backBena.setMemberCode(name);
                Gson gson = new Gson();
                String json = gson.toJson(backBena);
                HttpClient.postJson(NetWorkConstant.USER_LOGIN_OUT, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.e("-----------"+statusCode);
                        LogCatW(NetWorkConstant.USER_GET_IMG, "", statusCode, content);
                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if (content.indexOf("注销成功") != -1) {
                            SharedPreferencesUtil.putString(SettingActivity.this, "Token", null);
                            SharedPreferencesUtil.putString(SettingActivity.this, "MemberCode", null);
                            startActivityWithoutExtras(MainActivity.class);
                            finish();
                        } else {
                            startActivityWithoutExtras(MainActivity.class);
                            finish();
                        }
                    }
                });
                SharedPreferencesUtil.putString(SettingActivity.this, "Token", null);
                SharedPreferencesUtil.putString(SettingActivity.this, "MemberCode", null);
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


    @OnClick({R.id.btn_main_back, R.id.cache, R.id.rl_about, R.id.rl_version, R.id.back_login, R.id.rl_defend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                if (SharedPreferencesUtil.getString(SettingActivity.this, "MemberCode", "").equals("")) {
                    startActivityWithoutExtras(MainActivity.class);
                }
                finish();
                break;
            case R.id.rl_defend:
                startActivityWithoutExtras(DefendActivity.class);
                break;
            case R.id.cache:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.clear_history_alert);
                TextView tv_tips = (TextView) dialog.findViewById(R.id.textView5);
                tv_tips.setText("确认要清除缓存吗？");
                dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.GONE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                        File cache = SettingActivity.this.getApplication().getCacheDir();
                        GetFileSizeUtil.deleteCache(cache);
                        double size = GetFileSizeUtil.getFileOrFilesSize(cache.getPath(), GetFileSizeUtil.SIZETYPE_KB);
                        mTvCache.setText(size + "KB");
                        ToastUtils.showToast("缓存清理完成");
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.rl_about:
                startActivityWithoutExtras(AboutActivity.class);
                break;
            case R.id.rl_version:
              //  Beta.checkUpgrade();
                Beta.checkUpgrade(true,false);
                /***** 获取升级信息 *****/
                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
                StringBuilder info = new StringBuilder();
                if (upgradeInfo == null) {
                    return;
                }
                info.append("id: ").append(upgradeInfo.id).append("\n");
                info.append("标题: ").append(upgradeInfo.title).append("\n");
                info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
                info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
                info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
                info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
                info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
                info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
                info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
                info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
                info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
                info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
                info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType).append("\n");
                info.append("图片地址：").append(upgradeInfo.imageUrl);

                Logcat.i("更新Info:"+info);
                break;
            case R.id.back_login:
                SharedPreferencesUtil.clearString(this, "Discount");
                backLogin();
                break;
        }
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
