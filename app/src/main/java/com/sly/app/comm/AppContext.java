package com.sly.app.comm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.sly.app.utils.SharedPreferencesUtil;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者 by K
 * 时间：on 2017/8/21 10:58
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AppContext extends Application {

    public static Application application;
    public static Handler mHandler;
    private static AppContext app;
    public static Context mContext;
    public static int countImg = 1;
    protected AppConfig mAppConfig;
    private int refCount = 0;

    /**
     * 打开的activity
     **/
    private List<Activity> activities = new ArrayList<Activity>();
    private boolean isForeground;

    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        application = this;
        mContext = this;
        mAppConfig = AppConfig.getInstance();
        initApp(mContext);
        registerActivityCallBacks();
        //推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * @param mContext
     */
    private void initApp(Context mContext) {
        /**
         *  初始化公共配置
         */
        mAppConfig.initCommonConfig();
        /**
         *  Realm 数据库
         */
        mAppConfig.initRealm();
        /**
         *   默认图片选择器
         */
        mAppConfig.initImgPicker(countImg);
        /**
         * 更新
         */
        mAppConfig.initBugly();
        /**
         * LeakCanary   打包改false
         */
        mAppConfig.initLeakCanary(false);
        /**
         * 初始化分享
         */
        mAppConfig.initShare();

    }

    // 通过这个Callback拿到App所有Activity的生命周期回调
    private void registerActivityCallBacks() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                refCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                isForeground = true;
                Intent intent = new Intent();
                intent.setAction("com.sly.app.REMOVE_BADGE");
                Bundle bundle = new Bundle();
                bundle.putString("badgeCount", "0");
                intent.putExtras(bundle);
                SharedPreferencesUtil.putInt(mContext, "badgeCount", 0);
                sendBroadcast(intent);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                isForeground = false;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                refCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    public boolean isForeground() {
        return isForeground;
    }
}
