package com.sly.app.activity.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/6/13.
 */
public class DefendActivity extends BaseActivity {

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
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.iv_relation)
    ImageView mIvRelation;
    @BindView(R.id.tv_dqc_account)
    TextView mTvDqcAccount;
    @BindView(R.id.tv_dqc)
    TextView mTvDqc;
    @BindView(R.id.rl_binding)
    RelativeLayout mRlBinding;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    private String DQC_Account;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("账户与安全");
        mTvLogin.setText(SharedPreferencesUtil.getString(this, "MemberCode"));
        mTvPhone.setText(CommonUtils.replaceStr(3, 7, "****", SharedPreferencesUtil.getString(this, "Mobile")));
        isRelation(SharedPreferencesUtil.getString(this, "MemberCode"), SharedPreferencesUtil.getString(this, "Token"), false);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_defend;
    }

    @Override
    protected void setListener() {
    }
    @OnClick({R.id.btn_main_back, R.id.rl_password, R.id.rl_binding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_binding:
                isRelation(SharedPreferencesUtil.getString(this, "MemberCode"), SharedPreferencesUtil.getString(this, "Token"), true);
                break;
            case R.id.rl_password:
                startActivityWithoutExtras(PasswordActivity.class);
                break;
        }
    }

    /**
     * 是否绑定地球村账户
     *
     * @param memberCode
     * @param token
     */
    private void isRelation(String memberCode, final String token, final boolean isGo) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_IS_RELATION, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e(NetWorkConstant.USER_IS_RELATION+"/"+json+"/"+statusCode+"/"+content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && !CommonUtils.isBlank(mReturnBean.getData())) {
                    mTvDqc.setText("绑定的地球村账号");
                    mTvDqcAccount.setVisibility(View.VISIBLE);
                    mTvDqcAccount.setText(mReturnBean.getData());
                    mIvRelation.setVisibility(View.GONE);
                } else {
                    mTvDqcAccount.setVisibility(View.GONE);
                    if (isGo == true) {
                        startActivityWithoutExtras(RelationDQCActivity.class);
                        mIvRelation.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
    }

}
