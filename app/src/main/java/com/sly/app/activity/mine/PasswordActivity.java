package com.sly.app.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;

import butterknife.BindView;


/**
 * Created by 01 on 2017/6/13.
 */
public class PasswordActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.rl_loginpassword)
    RelativeLayout mRlLoginpassword;
    @BindView(R.id.rl_paypassword)
    RelativeLayout mRlPaypassword;
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("设置密码");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected void setListener() {

        mBtnMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        mRlLoginpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("pay", "login");
                intent.setClass(PasswordActivity.this, UpDataPwActivity.class);
                startActivity(intent);
            }
        });
        mRlPaypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("pay", "pay");
                intent.setClass(PasswordActivity.this, UpDataPwActivity.class);
                startActivity(intent);
            }
        });
    }

}
