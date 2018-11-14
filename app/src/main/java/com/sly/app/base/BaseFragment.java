package com.sly.app.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sly.app.R;
import com.sly.app.styles.bar.ImmersionBar;
import com.sly.app.view.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vip.devkit.library.Logcat;
import vip.devkit.library.NetUtils;
import vip.devkit.view.common.baselayout.BaseLayoutManager;

/**
 * 作者 by K
 * 时间：on 2017/8/31 16:31
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected View mRootView;

    private Unbinder unbinder;
    private LoadingDialog loadingDialog;//加载中
    private ProgressDialog progressDialog;
    protected ImmersionBar mImmersionBar;
    protected BaseLayoutManager mBaseLayoutManager;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initBaseLayout();
        if (isImmersionBarEnabled())
            initImmersionBar();
        initView(view);
        initData();
        initRxBus();
        setListener();
        checkNetState(mActivity);
    }

    /**
     * 初始化RxBus
     */
    protected void initRxBus() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    /**
     * 初始化多状态布局View
     */
    protected void initBaseLayout() {
        mBaseLayoutManager = BaseLayoutManager.wrap(this);
      //  mBaseLayoutManager.showContent();
    }
    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView(View view) {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }


    /**
     * startActivity
     * @param clazz
     */
    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);
        startActivity(intent);
    }

    /**
     *  startActivity  putExtras（Bundle）
     * @param clazz
     * @param extras
     */
    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(mActivity, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * 重启当前Activity
     */
    private  void reStartActivity() {
        Intent intent = mActivity.getIntent();
        mActivity.finish();
        startActivity(intent);
    }
    /**
     * @param mContext
     */
    protected void checkNetState(Context mContext) {
        Logcat.i("NetStatus:" + NetUtils.isNetworkAvailable(mContext));
        if (!NetUtils.isNetworkAvailable(mContext)) {
        }
    }

    /**
     * 初始化  加载
     *
     * @param str       提示内容
     * @param isOnTouch 点击外部是否关闭
     */
    public void initProgressDialog(String str, boolean isOnTouch) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mActivity, R.style.loading_dialog);
            loadingDialog.setText("加载中，请稍等...");
        }
        loadingDialog.setCanceledOnTouchOutside(isOnTouch);
    }

    public void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void showLoading() {
        progressDialog.show();
    }

    public void hiddenLoading() {

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}