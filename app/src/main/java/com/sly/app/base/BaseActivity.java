package com.sly.app.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sly.app.R;
import com.sly.app.listener.LayoutManagerListener;
import com.sly.app.push.PushService;
import com.sly.app.styles.bar.ImmersionBar;
import com.sly.app.styles.bar.OSUtils;
import com.sly.app.styles.fonts.FontsManager;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vip.devkit.library.Logcat;
import vip.devkit.library.NetUtils;
import vip.devkit.library.ScreenUtils;
import vip.devkit.library.StringUtil;
import vip.devkit.library.ViewUtils;
import vip.devkit.view.common.baselayout.BaseLayoutManager;

/**
 * 作者 by K
 * 时间：on 2017/8/28 15:58
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public abstract class BaseActivity extends AppCompatActivity {
    private long lastBackKeyDownTick = 0;
    public static final long MAX_DOUBLE_BACK_DURATION = 2000;
    protected LoadingDialog loadingDialog;//加载中
    protected Context mContext;
    protected Unbinder unbinder;
    protected ImmersionBar mImmersionBar;
    protected BaseLayoutManager mBaseLayoutManager;
    private WindowManager.LayoutParams layoutParams;
    private PushService.MyBinder myBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (PushService.MyBinder) service;
            myBinder.startPushService();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏应用的标题栏
        setContentView(setLayoutId());
        unbinder = ButterKnife.bind(this);
        mContext = this;
        Logcat.i("导航栏高度" + ViewUtils.getStatusBarHeight(this) + "/" + ScreenUtils.getStatusBarHeight(this) + " 手机系统：" + OSUtils.isEMUI() + "/" + OSUtils.isFlymeOS());
        initBase();
        initData();
        initView();
        setListener();
    }

    private void initBase() {
        /**检测app版本**/
//        Beta.checkUpgrade(false, true);
        /**多状态布局*/
        initBaseLayout();
        if (isCheckNetState())
            checkNetState(this, R.layout.layout_net_error);
        if (isImmersionBarEnabled())
            initImmersionBar();
        Intent bindIntent = new Intent(this, PushService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    protected abstract void initData();

    protected abstract int setLayoutId();

    protected abstract void initView();

    protected abstract void setListener();

    /***
     * 用于在初始化View之前做一些事
     */
    protected final void init() {
        FontsManager.initFormAssets(mContext, "fonts/test.ttf");
        FontsManager.changeFonts(this);
    }

    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }


    /**
     * startActivity
     *
     * @param clazz
     */
    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity  putExtras（Bundle）
     *
     * @param clazz
     * @param extras
     */
    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * 重启当前Activity
     */
    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
            ToastUtils.showToast("再按一次退出");
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * @param mContext
     */
    protected void checkNetState(Context mContext, int layoutId) {
        Logcat.i("NetStatus:" + NetUtils.isNetworkAvailable(mContext));
        if (!NetUtils.isNetworkAvailable(mContext)) {
        }
    }
    /**
     * 初始化多状态布局View
     */
    protected void initBaseLayout() {
      //  mBaseLayoutManager = BaseLayoutManager.wrap(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
        unbindService(connection);// 解除绑定，断开连接

    }

    //设置android app 的字体大小不受系统字体大小改变的影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 初始化  加载
     *
     * @param str       提示内容
     * @param isOnTouch 点击外部是否关闭
     */
    public void initProgressDialog(String str, boolean isOnTouch) {
        if (!isFinishing()) {
            loadingDialog = new LoadingDialog(mContext, R.style.loading_dialog);
            loadingDialog.setCanceledOnTouchOutside(isOnTouch);
            if (StringUtil.isEmpty(str)) {
                loadingDialog.setText("加载中，请稍等...");
            } else {
                loadingDialog.setText(str);
            }
            loadingDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void dismissProgressDialog(boolean isUiThread) {
        if (isUiThread == true) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        } else {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();

    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    /**
     * Is CheckNetState  enabled boolean.
     *
     * @return the boolean
     */
    public boolean isCheckNetState() {
        return true;
    }

    private LayoutManagerListener mLayoutManagerListener;

    public void setLayoutManagerListener(LayoutManagerListener mLayoutManagerListener) {
        this.mLayoutManagerListener = mLayoutManagerListener;
    }

    /**
     * 初始化字体备用
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}