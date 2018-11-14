package com.sly.app.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.sly.app.R;
import com.sly.app.base.Contants;
import com.sly.app.comm.AppContext;
import com.sly.app.model.PostResult;
import com.sly.app.utils.StringHelper;
import com.sly.app.view.iviews.BaseUi;
import com.sly.app.view.iviews.IPageStatusUi;
import com.sly.app.view.iviews.IRefreshRetryUi;
import com.sly.app.view.iviews.IToolbar;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseUi, IPageStatusUi, IRefreshRetryUi, IToolbar {

    /**
     * context
     */
    protected Context mContext = null;

    protected SwipeRefreshLayout swipeRefreshLayout;

    private ImageView pageStatusIconIv;
    private TextView pageStatusTextTv;

    private CardView refreshAgainBtn;
    private TextView refreshAgainTv;

    private View returnBack;
    private TextView toolbarTv;
    private TextView toolbarRightTv;
    private View toolbarRightBtn;
    private ImageView toolbarRightImg;


    private boolean isCanBack = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        ((AppContext) getApplication()).addActivity(this);
        if (isShowBar()) {
            ImmersionBar.with(this).init();
        }
        mContext = this;
        /**检测app版本**/
//        Beta.checkUpgrade(false, true);
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        initViewsAndEvents();
    }

    protected void setToolbarTitle(String title) {
        setTitle(" ");
        if (toolbarTv != null) {
            toolbarTv.setText(title);
        }

    }

    protected void setToolRightTitle(String title) {
        toolbarRightBtn.setVisibility(View.GONE);
        if (toolbarRightTv != null) {
            toolbarRightTv.setText(title);
        }

    }

    protected void setToolRightImg(int rId) {
        toolbarRightTv.setVisibility(View.GONE);
        toolbarRightBtn.setVisibility(View.VISIBLE);
        toolbarRightImg.setImageResource(rId);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

        toolbarTv = ButterKnife.findById(this, R.id.toolbar_title);
        returnBack = ButterKnife.findById(this, R.id.return_back);
        toolbarRightTv = ButterKnife.findById(this, R.id.toolbar_right_tv);
        toolbarRightImg = ButterKnife.findById(this, R.id.toolbar_right_img);
        toolbarRightBtn = ButterKnife.findById(this, R.id.toolbar_right_btn);
//
        if (null != returnBack) {
            returnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isCanBack) {
                        finish();
                    } else {
                        setOnClickBack();
                    }
                }
            });
        }


        swipeRefreshLayout = ButterKnife.findById(this, R.id.swipe_refresh_layout);
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        }

        pageStatusIconIv = ButterKnife.findById(this, R.id.page_status_icon_iv);
        pageStatusTextTv = ButterKnife.findById(this, R.id.page_status_text_tv);

        refreshAgainBtn = ButterKnife.findById(this, R.id.refresh_again_btn);
        refreshAgainTv = ButterKnife.findById(this, R.id.refresh_again_tv);


    }

    public void setBackStatus(boolean status) {
        isCanBack = status;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShowBar()) {
            try {
                ImmersionBar.with(this).destroy();
            } catch (Exception e) {
            }
        }
//        ButterKnife.reset(this);

        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

        dimissProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 是否绑定eventBus
     */
    protected abstract boolean isBindEventBusHere();

    /**
     * 绑定布局xml文件
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 初始化布局和事件，在onFirstUserVisible之前执行
     */
    protected abstract void initViewsAndEvents();

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

    private void toast(String msg, int duration) {
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
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }
    }

    @Override
    public void showPageStatusView(int iconRes, String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.VISIBLE);
            pageStatusIconIv.setImageResource(iconRes);
        }
    }

    @Override
    public void hidePageStatusView() {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.GONE);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRefreshRetry(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if (null != refreshAgainBtn) {
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

        if (null != refreshAgainBtn) {
            refreshAgainBtn.setVisibility(View.VISIBLE);
            refreshAgainTv.setText("立即登录");
            refreshAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dimissRefreshRetry();
//                    CommonUtil2.goActivity(mContext, TestActivity.class,null,false);

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
        if (mContext == null) {
            return;
        }
        if (builder != null && builder.isShowing()) {
            return;
        }
        builder = new MaterialDialog.Builder(this)
                .content(label)
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void showProgress(String label, boolean isCancelable) {

        builder = new MaterialDialog.Builder(this)
                .content(label)
                .cancelable(isCancelable)
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void dimissProgress() {
        if (builder != null && builder.isShowing()) {
            builder.dismiss();
        }
    }

    public void hideToolbackBtn() {
        if (null != returnBack) {
            returnBack.setVisibility(View.INVISIBLE);
        }
    }

    public void showToolbackBtn() {
        if (null != returnBack) {
            returnBack.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接受EventBus 广播
     */
    public void onEvent(PostResult postResult) {

    }


    /**
     * Bar
     */
    public boolean isShowBar() {
        return false;
    }

    public void setHideBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    public void setOnClickBack() {

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


}
