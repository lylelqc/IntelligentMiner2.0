package com.sly.app.activity.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.fragment.orderState.AllOrder;
import com.sly.app.fragment.orderState.StayDelivery;
import com.sly.app.fragment.orderState.StayEvaluation;
import com.sly.app.fragment.orderState.StayPay;
import com.sly.app.fragment.orderState.StayReceive;
import com.sly.app.view.MainBottomBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 01 on 2016/10/14.
 * <p/>
 */
public class ShopOrder extends BaseActivity {
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
    @BindView(R.id.btn_allorder)
    TextView mBtnAllorder;
    @BindView(R.id.btn_staypay)
    TextView mBtnStaypay;
    @BindView(R.id.btn_staydelivery)
    TextView mBtnStaydelivery;
    @BindView(R.id.btn_stayreceive)
    TextView mBtnStayreceive;
    @BindView(R.id.btn_evaluation)
    TextView mBtnEvaluation;
    @BindView(R.id.bottom_odetail_bar)
    MainBottomBar mBottomOdetailBar;
    @BindView(R.id.fl_details)
    FrameLayout mFlDetails;
    private FragmentManager mFragmentManager;
    private String wwwType;
    private int index;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).keyboardEnable(true).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("我的订单");
        initMainButtom();
    }

    @Override
    protected void initData() {
        index = getIntent().getIntExtra("index", 0);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_state;
    }

    @Override
    protected void setListener() {

    }

    private void initMainButtom() {
        mFragmentManager = getSupportFragmentManager();
        mBottomOdetailBar.setCallBack(new MainBottomBar.CallBack() {

            @Override
            public void call(int prevIndex, int currentIndex) {
                // TODO Auto-generated method stub
                // 获取当前fragment和切换到的fragment
                Fragment perFragment = mFragmentManager.findFragmentByTag("tag" + prevIndex);
                Fragment currentFragment = mFragmentManager.findFragmentByTag("tag" + currentIndex);
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (perFragment != null) {
                    ft.hide(perFragment);
                }
                if (currentFragment == null) {
                    currentFragment = getBottomTabFragment(currentIndex);
                    ft.add(R.id.fl_details, currentFragment, "tag" + currentIndex);
                } else {
                    ft.show(currentFragment);
                }
                ft.commitAllowingStateLoss();
            }
        });
        mBottomOdetailBar.setSelected(index);
    }

    /**
     * 返回bottomTabFragment
     *
     * @param index
     * @return
     */

    private Fragment getBottomTabFragment(int index) {
        Fragment mFragment = null;
        switch (index) {
            case 0:
                mFragment = new AllOrder();
                break;
            case 1:
                mFragment = new StayPay();
                break;
            case 2:
                mFragment = new StayDelivery();
                break;
            case 3:
                mFragment = new StayReceive();
                break;
            case 4:
                mFragment = new StayEvaluation();
                break;
        }
        return mFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
}



