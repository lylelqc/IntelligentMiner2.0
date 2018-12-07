package com.sly.app.activity.mine.wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
import com.sly.app.model.balance.BalanceInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 作者 by K
 * 时间：on 2017/7/17 10:56
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途： 微商分销款
 * 最后修改：
 */
public class CurrencyInfo extends BaseActivity {
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
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_income)
    TextView mTvIncome;
    @BindView(R.id.tv_expenditure)
    TextView mTvExpenditure;
    @BindView(R.id.rl_Record)
    RelativeLayout mRlRecord;
    @BindView(R.id.rl_recharge)
    RelativeLayout mRlRecharge;

    private String barTitle;
    private String currencyType;//货币类型
    private String MemberCode, Token;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        if (!CommonUtils.isBlank(barTitle)){
            mTvMainTitle.setText(barTitle);
        }else {
            mTvMainTitle.setText("钱包");
        }
    }

    @Override
    protected void initData() {
        Bundle mBundle = getIntent().getExtras();
        barTitle = mBundle.getString("marks");
        currencyType = mBundle.getString("currencyType");
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        getBalance(this,MemberCode,Token,currencyType);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_currency05;
    }

    @Override
    protected void setListener() {

    }

    private void getBalance(Context mContext, String memberCode, String token,String currencyCode) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        map.put("CurrencyCode", currencyCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSER_DATE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logcat.e("返回码:" + statusCode);
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.PURSER_DATE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<BalanceInfo> balanceInfo = JSON.parseArray(mReturnBean.getData(), BalanceInfo.class);
                    mTvBalance.setText(balanceInfo.get(0).getBalance()+"");
                    mTvExpenditure.setText(balanceInfo.get(0).getExpenditure()+"");
                    mTvIncome.setText(balanceInfo.get(0).getIncome()+"");
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }


    @OnClick({R.id.btn_main_back, R.id.rl_Record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_Record:
                Bundle bundle = new Bundle();
                bundle.putString("marks", currencyType);
                startActivityWithExtras(WRecordActivity.class, bundle);
                break;
        }
    }
}
