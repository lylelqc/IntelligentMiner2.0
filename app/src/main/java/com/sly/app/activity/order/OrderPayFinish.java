package com.sly.app.activity.order;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 by K
 * 时间：on 2017/9/23 16:44
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class OrderPayFinish extends BaseActivity {


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
    @BindView(R.id.tv_receivesuccessful)
    TextView mTvReceivesuccessful;
    @BindView(R.id.tv_order_detail)
    TextView mTvOrderDetail;
    @BindView(R.id.tv_store)
    TextView mTvStore;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("支付完成");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pay_after;
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btn_main_back, R.id.tv_order_detail, R.id.tv_store})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                startActivityWithoutExtras(MainActivity.class);
                finish();
                break;
            case R.id.tv_order_detail:
                if (getIntent().getExtras().getString("OrderNo").contains("J")){

                }
                Bundle bundle = new Bundle();
                bundle.putString("OrderNo",getIntent().getExtras().getString("OrderNo"));
                startActivityWithExtras(OrderDetails.class,bundle);
//                startActivityWithoutExtras(MainActivity.class);
                finish();
                break;
            case R.id.tv_store:
                startActivityWithoutExtras(MainActivity.class);
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivityWithoutExtras(MainActivity.class);
            finish();
        }
        return false;
    }
}
