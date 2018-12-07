package com.sly.app.activity.mine.wallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.WXPayBean;
import com.sly.app.model.pay.AliPayAuthResult;
import com.sly.app.model.pay.AliPayResult;
import com.sly.app.model.pay.WxPayAction;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;


/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.recharge_money)
    EditText mRechargeMoney;
    @BindView(R.id.cb_pay_aliPay)
    CheckBox mCbPayAliPay;
    @BindView(R.id.rl_pay_aliPay)
    RelativeLayout mRlPayAliPay;
    @BindView(R.id.cb_pay_weChat)
    CheckBox mCbPayWeChat;
    @BindView(R.id.rl_pay_weChat)
    RelativeLayout mRlPayWeChat;
    @BindView(R.id.cb_pay_wallet)
    CheckBox mCbPayWallet;
    @BindView(R.id.rl_pay_wallet)
    RelativeLayout mRlPayWallet;
    @BindView(R.id.bt_recharge)
    TextView mBtRecharge;
    @BindView(R.id.sp_currency_type)
    Spinner mSpCurrencyType;
    private String PayType, mCurrencyType;
    private String MemberCode, Token;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;//暂时不用
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Logcat.i("resultStatus:------------------------------------------------- " + resultStatus + "/" + resultInfo);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showToast("充值成功");
                        startActivityWithoutExtras(ConsumerActivity.class);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showToast("充值失败");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayAuthResult authResult = new AliPayAuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ToastUtils.showToast("授权成功" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        ToastUtils.showToast("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("充值");
        mCbPayWeChat.setClickable(false);
        mCbPayAliPay.setClickable(false);
        mCbPayWallet.setClickable(false);

    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void setListener() {

        mSpCurrencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] join = getResources().getStringArray(R.array.currency_type);
                Logcat.i("你点击的是:" + join[position]);
                mCurrencyType = join[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick({R.id.btn_main_back, R.id.rl_pay_aliPay, R.id.rl_pay_weChat, R.id.rl_pay_wallet, R.id.bt_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_pay_wallet:
                PayType = "wallet";
                mCbPayWallet.setChecked(true);
                mCbPayWeChat.setChecked(false);
                mCbPayAliPay.setChecked(false);
                break;
            case R.id.rl_pay_weChat:
                PayType = "weChat";
                mCbPayWallet.setChecked(false);
                mCbPayWeChat.setChecked(true);
                mCbPayAliPay.setChecked(false);
                break;
            case R.id.rl_pay_aliPay:
                PayType = "aliPay";
                mCbPayAliPay.setChecked(true);
                mCbPayWallet.setChecked(false);
                mCbPayWeChat.setChecked(false);
                break;
            case R.id.bt_recharge:
                String Number = mRechargeMoney.getText().toString().trim();
                if (CommonUtils.isBlank(Number)) {
                    ToastUtils.showToast("请输入需要充值的数额");
                } else {
                    if (mCurrencyType.equals("充值类型")) {
                        ToastUtils.showToast("请选择充值类型");
                        return;
                    }
                    if (CommonUtils.isBlank(PayType)) {
                        ToastUtils.showToast("请选择支付方式");
                    } else {
                        if (PayType.equals("aliPay")) {
                            //     ToastUtils.showToast("即将开放");
                            ProOrderAliPay(MemberCode, Token, Number, mCurrencyType);
                        } else if (PayType.equals("weChat")) {
                            if (mCurrencyType.equals("钱包余额")) {
                                ProOrderWXPay(MemberCode, Token, Number);
                            } else if (mCurrencyType.equals("产品券")) {
                                ProOrderWXPay2(MemberCode, Token, Number, "Ticket");
                            } else if (mCurrencyType.equals("库存额度")) {
                                ProOrderWXPay2(MemberCode, Token, Number, "Stock");
                            }
                        } else if (PayType.equals("wallet")) {
                            ToastUtils.showToast("即将开放");
                        }
                    }
                }
                break;
        }

    }

    private void ProOrderWXPay(String memberCode, String token, String price) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("OrderFormCode", "cz");
        map.put("ProductName", "微信充值");
        map.put("Price", price);
        map.put("PaymentTypeCode", "02");
        map.put("OrderStatus", "1");
        map.put("DoctorID", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.WALLET_PAY_WX, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.WALLET_PAY_WX, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    WxPayAction wxPayAction = new WxPayAction();
                    wxPayAction.setPayUseing("cz");
                    WXPayBean payInfo = JSON.parseObject(mReturnBean.getData(), WXPayBean.class);
                    api = WXAPIFactory.createWXAPI(RechargeActivity.this, null);
                    api.registerApp(payInfo.getAppid());
                    PayReq request = new PayReq();
                    request.appId = payInfo.getAppid();
                    request.partnerId = payInfo.getMch_id();
                    request.prepayId = payInfo.getPrepayid();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = payInfo.getNoncestr();
                    request.timeStamp = payInfo.getTimestamp();
                    request.sign = payInfo.getSign();
                    api.sendReq(request);
                }
                return false;
            }
        });
    }

    private void ProOrderWXPay2(String memberCode, String token, String price, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("Sum", price);
        map.put("Type", type);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.WALLET_TYPE_PAY_WX, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.WALLET_TYPE_PAY_WX, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    WxPayAction wxPayAction = new WxPayAction();
                    wxPayAction.setPayUseing("cz");
                    WXPayBean payInfo = JSON.parseObject(mReturnBean.getData(), WXPayBean.class);
                    api = WXAPIFactory.createWXAPI(RechargeActivity.this, null);
                    api.registerApp(payInfo.getAppid());
                    PayReq request = new PayReq();
                    request.appId = payInfo.getAppid();
                    request.partnerId = payInfo.getMch_id();
                    request.prepayId = payInfo.getPrepayid();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = payInfo.getNoncestr();
                    request.timeStamp = payInfo.getTimestamp();
                    request.sign = payInfo.getSign();
                    api.sendReq(request);
                }
                return false;
            }
        });
    }

    private void ProOrderAliPay(String memberCode, String token, String price, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("Price", price);
        if (type.equals("钱包余额")) {
            map.put("type", "CZ");
        } else if (type.equals("产品券")) {
            map.put("type", "Ticket");
        } else if (type.equals("库存额度")) {
            map.put("type", "Stock");
        }
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.WALLET_PAY_ALI, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.WALLET_PAY_ALI, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    final String payInfo = mReturnBean.getData();
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask PayInfo = new PayTask(RechargeActivity.this);
                            Map<String, String> result = PayInfo.payV2(payInfo, true);
                            Logcat.i(result.toString());
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
