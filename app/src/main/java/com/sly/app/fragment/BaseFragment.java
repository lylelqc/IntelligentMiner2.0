package com.sly.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.sly.app.R;
import com.sly.app.base.Contants;
import com.sly.app.model.PostResult;
import com.sly.app.utils.StringHelper;
import com.sly.app.view.iviews.BaseUi;
import com.sly.app.view.iviews.IPageStatusUi;
import com.sly.app.view.iviews.IRefreshRetryUi;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 */
public abstract class BaseFragment extends Fragment implements BaseUi, IPageStatusUi, IRefreshRetryUi {

    /**
     * Log tag
     */
    protected static String TAG_LOG = null;

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    /**
     * context
     */
    protected Context mContext = null;

    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    private ImageView pageStatusIconIv;
    private TextView pageStatusTextTv;

    private CardView refreshAgainBtn;
    private TextView refreshAgainTv;

    protected SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        if(isShowBar()){
            try {
                ImmersionBar.with(this).init();
            }catch (Exception e){

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        pageStatusIconIv = ButterKnife.findById(view, R.id.page_status_icon_iv);
        pageStatusTextTv = ButterKnife.findById(view, R.id.page_status_text_tv);


        refreshAgainBtn = ButterKnife.findById(view, R.id.refresh_again_btn);
        refreshAgainTv = ButterKnife.findById(view, R.id.refresh_again_tv);

        swipeRefreshLayout = ButterKnife.findById(view, R.id.swipe_refresh_layout);
        if(null != swipeRefreshLayout){
            swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        }

        initViewsAndEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isShowBar()){
            try {
                ImmersionBar.with(this).destroy();
            }catch (Exception e){}
        }
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

        dimissProgress();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
//        MobclickAgent.onPageStart("BaseFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
//        MobclickAgent.onPageEnd("BaseFragment");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * fragment第一次可见，通常用于请求网络
     */
    protected abstract void onFirstUserVisible();

    /**
     * 相当于onResume
     */
    protected abstract void onUserVisible();

    /**
     * fragment第一次不可见
     */
    private void onFirstUserInvisible() {
        // 不推荐做任何操作
    }

    /**
     * 相当于onPause
     */
    protected abstract void onUserInvisible();

    /**
     * 初始化布局和事件，在onFirstUserVisible之前执行
     */
    protected abstract void initViewsAndEvents();

    /**
     * 绑定布局xml文件
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 是否绑定eventBus
     */
    protected abstract boolean isBindEventBusHere();

    @Override
    public void showToastLong(String msg) {
        if (null != msg && !StringHelper.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void showToastShort(String msg) {
        if (null != msg && !StringHelper.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_SHORT);
        }
    }

    private Toast mToast = null;
    private void toast(String msg, int duration){
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, duration);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    @Override
    public void showPageStatusView(String message) {
        if(null != pageStatusTextTv){
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }
    }

    @Override
    public void showPageStatusView(int iconRes, String message) {
        if(null != pageStatusTextTv){
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if(null != pageStatusIconIv){
            pageStatusIconIv.setVisibility(View.VISIBLE);
            pageStatusIconIv.setImageResource(iconRes);
        }
    }

    @Override
    public void hidePageStatusView() {
        if(null != pageStatusTextTv){
            pageStatusTextTv.setVisibility(View.GONE);
        }

        if(null != pageStatusIconIv){
            pageStatusIconIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRefreshRetry(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if(null != refreshAgainBtn){
            refreshAgainBtn.setVisibility(View.VISIBLE);
            refreshAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRefreshRetryBtn();
                }
            });
        }
    }

    public void showLoginRetry(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if(null != refreshAgainBtn){
            refreshAgainBtn.setVisibility(View.VISIBLE);
            refreshAgainTv.setText("立即登录");
            refreshAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dimissRefreshRetry();
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean(LoginActivity.IS_NEED_GOTO_HOME,false);
//                    CommonUtils.goActivity(mContext, .class,null,false);

                }
            });
        }
    }

    @Override
    public void dimissRefreshRetry() {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.GONE);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.GONE);
        }

        if (null != refreshAgainBtn) {
            refreshAgainBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void clickRefreshRetryBtn() {
        dimissRefreshRetry();
    }

    MaterialDialog builder = null;

    @Override
    public void showProgress(String label) {

        builder = new MaterialDialog.Builder(mContext)
                .content(label)
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void showProgress(String label, boolean isCancelable) {

        builder = new MaterialDialog.Builder(mContext)
                .content(label)
                .cancelable(isCancelable)
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void dimissProgress() {
        if(builder != null && builder.isShowing()){
            builder.dismiss();
        }
    }

    /**
     * 接受EventBus 广播
     * */
    public void onEvent(PostResult postResult){

    }

    /**
     * Bar
     */
    public boolean isShowBar(){
        return true;
    }

    public void setHideBar(){
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }
}
