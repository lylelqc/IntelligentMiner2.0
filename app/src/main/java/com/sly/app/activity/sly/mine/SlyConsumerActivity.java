package com.sly.app.activity.sly.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.wallet.CurrencyInfo;
import com.sly.app.activity.mine.wallet.FundActivity;
import com.sly.app.activity.mine.wallet.RAccountActivity;
import com.sly.app.activity.mine.wallet.RechargeActivity;
import com.sly.app.activity.mine.wallet.WRecordActivity;
import com.sly.app.activity.mine.wallet.WithdrawActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.UserInfoBean;
import com.sly.app.model.balance.BalanceInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.Request;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;


/**
 * 钱包
 */
public class SlyConsumerActivity extends BaseActivity {
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
    @BindView(R.id.tv_ok_balance)
    TextView mTvOkBalance;
    @BindView(R.id.tv_income)
    TextView mTvIncome;
    @BindView(R.id.tv_expenditure)
    TextView mTvExpenditure;
    @BindView(R.id.rl_business)
    RelativeLayout mRlBusiness;
    @BindView(R.id.rl_recharge)
    RelativeLayout mRlRecharge;
    @BindView(R.id.rl_withdraw)
    RelativeLayout mRlWithdraw;
    @BindView(R.id.rl_zz)
    RelativeLayout mRlZz;
    @BindView(R.id.textView8)
    TextView mTextView8;
    @BindView(R.id.rl_wallet_a)
    RelativeLayout mRlWalletA;
    @BindView(R.id.view1)
    View mView1;
    @BindView(R.id.rl_wallet_b)
    RelativeLayout mRlWalletB;
    @BindView(R.id.rl_wallet_c)
    RelativeLayout mRlWalletC;
    @BindView(R.id.rl_wallet_d)
    RelativeLayout mRlWalletD;
    @BindView(R.id.rl_wallet_e)
    RelativeLayout mRlWalletE;
    private String MemberCode,Token;
    private Realm realm;
    private UserInfoBean persons;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    protected void initView() {
        mTvMainTitle.setText("我的钱包");
//       if (persons.getMemberLeveName().equals("合伙人")){
//           mRlWalletD.setVisibility(View.GONE);
//       }
        mRlWalletD.setVisibility(View.GONE);
    }

    protected void initData() {
        realm = Realm.getDefaultInstance();
        MemberCode = SharedPreferencesUtil.getString(this, "User","None");
        Token = SharedPreferencesUtil.getString(this, "Token","None");
        persons = realm.where(UserInfoBean.class)
                .equalTo("MemberCode", MemberCode)
                .findFirst();
        MemberCode = SharedPreferencesUtil.getString(this, "","None");
        Token = SharedPreferencesUtil.getString(this, "Token","None");
        getBalance(this, MemberCode, Token);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_sly_consumer;
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getBalance(mContext, MemberCode, Token);
    }

    private void getBalance(Context mContext, String memberCode, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        map.put("CurrencyCode", "YB");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSER_DATE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logcat.e("返回码:" + statusCode);
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                String backlogJsonStr = content;
                String backlogJsonStrTmp = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStrTmp.substring(1, backlogJsonStrTmp.length() - 1);
                LogCatW(NetWorkConstant.PURSER_DATE, json, statusCode, backlogJsonStr);
                final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (returnBean.getStatus().equals("1")) {
                    List<BalanceInfo> balanceInfo = JSON.parseArray(returnBean.getData(), BalanceInfo.class);
                    mTvBalance.setText(String.valueOf(balanceInfo.get(0).getBalance()));
                    mTvIncome.setText(String.valueOf(balanceInfo.get(0).getIncome()));
                    mTvExpenditure.setText(String.valueOf(balanceInfo.get(0).getExpenditure()));
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }


    @OnClick({R.id.btn_main_back, R.id.rl_business, R.id.rl_recharge, R.id.rl_withdraw, R.id.rl_zz, R.id.rl_wallet_a, R.id.rl_wallet_b, R.id.rl_wallet_c, R.id.rl_wallet_d, R.id.rl_wallet_e})
    public void onViewClicked(View view) {
        Bundle   mBundle =null;
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_business:
                mBundle = new Bundle();
                mBundle.putString("marks", "YB");
                startActivityWithExtras(WRecordActivity.class, mBundle);
                break;
            case R.id.rl_recharge:
                startActivityWithoutExtras(RechargeActivity.class);
                break;
            case R.id.rl_withdraw:
                startActivityWithoutExtras(WithdrawActivity.class);
                break;
            case R.id.rl_zz:
                startActivityWithoutExtras(RAccountActivity.class);
                break;
            case R.id.rl_wallet_a:
                mBundle = new Bundle();
                mBundle.putString("marks", "进货款");
                mBundle.putString("currencyType", "Payment");
                startActivityWithExtras(CurrencyInfo.class, mBundle);
                break;
            case R.id.rl_wallet_b:
                mBundle = new Bundle();
                mBundle.putString("marks", "库存额度");
                mBundle.putString("currencyType", "DistributionShop");
                startActivityWithExtras(CurrencyInfo.class, mBundle);
                break;
            case R.id.rl_wallet_c:
                mBundle = new Bundle();
                mBundle.putString("marks", "产品券");
                mBundle.putString("currencyType", "Product");
                startActivityWithExtras(CurrencyInfo.class, mBundle);
                break;
            case R.id.rl_wallet_d:
                mBundle = new Bundle();
                mBundle.putString("marks", "授信额度");
                mBundle.putString("currencyType", "DistributionMember");
                startActivityWithExtras(CurrencyInfo.class, mBundle);
                break;
            case R.id.rl_wallet_e:
                startActivityWithoutExtras(FundActivity.class);
                break;
        }
    }
}


