package com.sly.app.activity.loading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vip.devkit.library.Logcat;

/**
 * 程序入口，启动app欢迎界面，延时功能和判断功能
 */
public class WelcomeActivity extends BaseActivity {
    private SharedPreferences sharepreferences;//实例化 SharedPreferences
    private SharedPreferences.Editor editor;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.init();
    }

    @Override
    public boolean isCheckNetState() {
        return  false;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        sharepreferences = this.getSharedPreferences("check", MODE_PRIVATE);// 初始化 SharedPreferences 储存
        editor = sharepreferences.edit();
        proInspect(this);
        isFirstStart(this);
        initProData(this);
    }

    @Override
    protected void setListener() {

    }

    /**
     * 重新安装/或更新之后删除数据库的时候
     *
     * @param welcomeActivity
     */
    private void initProData(WelcomeActivity welcomeActivity) {
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            //应用装时间
            long firstInstallTime = packageInfo.firstInstallTime;
            //应用最后一次更新时间
            long lastUpdateTime = packageInfo.lastUpdateTime;
            Logcat.i("first install time : " + firstInstallTime + " last update time :" + lastUpdateTime);
            if (lastUpdateTime > firstInstallTime) {
                if (lastUpdateTime == System.currentTimeMillis()) {
                    RealmConfiguration config = new RealmConfiguration.Builder().build(); // 默认的 RealmConfiguration
                    Realm.deleteRealm(config); // 清除数据
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // TODO: 2017/8/29  程序安检

    /**
     * 用于初始化/检查程序
     *
     * @param mContext
     */
    private void proInspect(Context mContext) {

    }

    /**
     * @param mContext
     */
    public void isFirstStart(Context mContext) {
        boolean isFirstS = sharepreferences.getBoolean("isFirstStart", true);
        if (isFirstS) {
            proStartActivity(mContext, true);
            editor.putBoolean("isFirstStart", false);//第一次启动后
            editor.commit();
        } else {
            proStartActivity(mContext, false);
        }
    }

    /**
     * @param mContext
     * @param isFirst
     */
    private void proStartActivity(final Context mContext, final boolean isFirst) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isFirst) {
                    startActivity(new Intent(mContext, LoadingActivity.class));
                } else {
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                finish();
            }
        };
        // 设置不同的延迟启动时间
        if (isFirst) {
            timer.schedule(task, 1000 * 1);
        } else {
            timer.schedule(task, 1000 * 2);
        }
    }


}

