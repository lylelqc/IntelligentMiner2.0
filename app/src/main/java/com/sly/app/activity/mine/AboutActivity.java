package com.sly.app.activity.mine;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 01 on 2017/8/24.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("关于智能矿工");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void setListener() {
    }



    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }

}
