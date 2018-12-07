package com.sly.app.activity.mine.league;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.activity.mine.UpDataPwActivity;
import com.sly.app.activity.order.OrderPayFinish;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.Constants;
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
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.pwd.SercurityDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/19 14:29
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class JoinOrderPayActivity extends BaseActivity {
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout mLlTitleRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.gmfw_icon_qb)
    ImageView mGmfwIconQb;
    @BindView(R.id.ll_user_wallet)
    LinearLayout mLlUseWallet;
    @BindView(R.id.cb_pay_wallet)
    CheckBox mCbPayWallet;
    @BindView(R.id.rl_pay_wallet)
    RelativeLayout mRlPayWallet;
    @BindView(R.id.wx)
    ImageView mWx;
    @BindView(R.id.ll_use_wx)
    LinearLayout mLlUseWx;
    @BindView(R.id.cb_pay_weChat)
    CheckBox mCbPayWeChat;
    @BindView(R.id.rl_pay_weChat)
    RelativeLayout mRlPayWeChat;
    @BindView(R.id.alipay)
    ImageView mAlipay;
    @BindView(R.id.cb_pay_aliPay)
    CheckBox mCbPayAliPay;
    @BindView(R.id.rl_pay_aliPay)
    RelativeLayout mRlPayAliPay;
    @BindView(R.id.rl_pay_wallet_ticket)
    RelativeLayout mRlPayTicket;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_jifen)
    TextView mTvJifen;
    @BindView(R.id.tv_wallet_name)
    TextView mTvWalletName;
    @BindView(R.id.tv_wallet_name_tag)
    TextView mTvWalletNameTag;
    @BindView(R.id.tv_recharge)
    TextView mTvRecharge;
    private String PayType, OrderNo, OrderPrice, MallType;
    private String MemberCode, Token;
    private IWXAPI api;
    private boolean isPwd = false;
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
                        ToastUtils.showToast("支付成功");
                        Bundle mBundle = new Bundle();
                        mBundle.putString("OrderNo", OrderNo);
                        startActivityWithExtras(MainActivity.class, mBundle);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showToast("支付失败");
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
    protected void initData() {
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.WX_APP_ID);
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Bundle bundle = getIntent().getExtras();
        MallType = bundle.getString("MallType");
        OrderNo = bundle.getString("OrderNo");
        OrderPrice = bundle.getString("PayPrice");
        if (CommonUtils.isBlank(MallType) && CommonUtils.isBlank(OrderNo) && CommonUtils.isBlank(OrderPrice)) {
            ToastUtils.showToast("支付参数为空，请返回");
        }
        //是否有支付密码
        RegPwd(MemberCode, Token);
    }

    @Override
    protected void initView() {
        mBtnTitleText.setText("美好地球村收银台");
        mTvOrderPrice.setText("￥" + OrderPrice);
        mCbPayWeChat.setClickable(false);
        mCbPayAliPay.setClickable(false);
        mCbPayWallet.setClickable(false);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    protected void setListener() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BackDialog();
        }
        return false;
    }

    @OnClick({R.id.btn_title_back, R.id.ll_title_right, R.id.rl_pay_wallet_ticket, R.id.rl_pay_wallet, R.id.rl_pay_weChat, R.id.rl_pay_aliPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                BackDialog();
                break;
            case R.id.ll_title_right:
/*                if (CommonUtils.isBlank(PayType)) {
                    ToastUtils.showToast("请选择支付方式");
                } else {
                    if (isPwd == true) {
                        if (PayType.equals("wallet")) {
                            SercurityDialog mDialog = new SercurityDialog(this, OrderPrice);
                            mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                                @Override
                                public void inputComplete(String passWord) {
                                    ProOrderPay(MemberCode, Token, OrderNo, OrderPrice, MallType, passWord);
                                }

                            });
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        } else if (PayType.equals("weChat")) {
                            ProOrderWXPay(MemberCode, Token, OrderNo, OrderPrice, MallType);
                        } else if (PayType.equals("aliPay")) {
                            SendCode(MemberCode, Token);
                            ToastUtils.showToast("请选择钱包支付，支付宝支付请等待更新");
                        } else {
                            ToastUtils.showToast("请确认支付方式");
                        }
                    } else {
                        ToastUtils.showToast("您还未设置支付密码");
                        Intent intent = new Intent();
                        intent.putExtra("pay", "pay");
                        intent.setClass(JoinOrderPayActivity.this, UpDataPwActivity.class);
                        startActivity(intent);
                    }
                }*/
                break;
            case R.id.rl_pay_wallet_ticket:
                if (isPwd == true) {
                    SercurityDialog mDialog = new SercurityDialog(this, OrderPrice);
                    mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                        @Override
                        public void inputComplete(String passWord) {
                         //   ProWSOrderPay(MemberCode, Token, OrderNo, OrderPrice, MallType, passWord);
                        }

                    });
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                } else {
                    ToastUtils.showToast("您还未设置支付密码");
                    Intent intent = new Intent();
                    intent.putExtra("pay", "pay");
                    intent.setClass(JoinOrderPayActivity.this, UpDataPwActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_pay_wallet:
                PayType = "wallet";
                mCbPayWallet.setChecked(true);
                mCbPayWeChat.setChecked(false);
                mCbPayAliPay.setChecked(false);
                if (isPwd == true) {
                    SercurityDialog mDialog = new SercurityDialog(this, OrderPrice);
                    mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                        @Override
                        public void inputComplete(String passWord) {
                            ProOrderPay(MemberCode, Token, OrderNo, OrderPrice, MallType, passWord);
                        }
                    });
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                } else {
                    ToastUtils.showToast("您还未设置支付密码");
                    Intent intent = new Intent();
                    intent.putExtra("pay", "pay");
                    intent.setClass(JoinOrderPayActivity.this, UpDataPwActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_pay_weChat:
//                ToastUtils.showToast("请选择钱包支付，微信支付即将开放");
                 ProOrderWXPay(MemberCode, Token, OrderNo, OrderPrice, MallType);
                break;
            case R.id.rl_pay_aliPay:
//                ToastUtils.showToast("请选择钱包支付，支付宝支付即将开放");
                  ProOrderAliPay(MemberCode, Token, OrderNo, OrderPrice, MallType);
                break;
        }
    }

    /**
     * @param memberCode
     * @param token
     * @param orderNo
     * @param orderPrice
     * @param mallType
     */
    private void ProOrderAliPay(String memberCode, String token, String orderNo, String orderPrice, String mallType) {
        initProgressDialog("订单支付中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("OrderNo", orderNo);
        map.put("OrderCode", orderNo);
        map.put("Sum",orderPrice);
        map.put("NickName", "美好地球村商品支付宝支付");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_PAY_ALI, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.W(NetWorkConstant.ORDER_PAY_ALI, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    final String payInfo = mReturnBean.getData();
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask PayInfo = new PayTask(JoinOrderPayActivity.this);
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

    /**
     * @param memberCode
     * @param token
     * @param orderNo
     * @param orderPrice
     * @param mallType
     */
    private void ProOrderWXPay(String memberCode, String token, final String orderNo, String orderPrice, String mallType) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("OrderCode", orderNo);
        map.put("Price", orderPrice);
        map.put("ProductName", "微信支付");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_PAY_WX, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.ORDER_PAY_WX, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    WxPayAction wxPayAction = new WxPayAction();
                    wxPayAction.setPayUseing("Pay");
                    wxPayAction.setOrderNo(orderNo);
                    WXPayBean payInfo = JSON.parseObject(mReturnBean.getData(), WXPayBean.class);
                    api = WXAPIFactory.createWXAPI(JoinOrderPayActivity.this, null);
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

    private void SendCode(String memberCode, String token) {
        initProgressDialog("订单支付中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                return false;
            }
        });
    }

    protected void BackDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.textView5)).setText("确定要离开收银台？");
        dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.VISIBLE);
        dialog.show();

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_action);
        cancelButton.setText("继续支付");
        cancelButton.setTextColor(Color.parseColor("#333333"));
        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_action);
        confirmButton.setText("确定离开");
        confirmButton.setTextColor(Color.parseColor("#ffffff"));
//        confirmButton.setBackgroundColor(Color.parseColor("#ffffff"));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ToastUtils.showToast("离开支付页面");
                dialog.dismiss();
                finish();
            }
        });
    }

    /**
     * @param memberCode
     * @param token
     * @param orderNo
     * @param orderPrice
     * @param mallType
     * @param passWord
     */
    private void ProOrderPay(String memberCode, String token, final String orderNo, String orderPrice, String mallType, String passWord) {
        initProgressDialog("订单支付中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("OrderNo", orderNo);
        map.put("PayPassword", passWord);
            map.put("CurrencyCode", "YB");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_PAY, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
                NetLogCat.W(NetWorkConstant.ORDER_PAY, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", orderNo);
//                    startActivityWithExtras(OrderPayFinish.class, mBundle);
                    startActivityWithoutExtras(MainActivity.class);
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });

    }

    /**
     * @param memberCode
     * @param token
     * @param orderNo
     * @param orderPrice
     * @param mallType
     * @param passWord
     */
    private void ProWSOrderPay(String memberCode, String token, final String orderNo, String orderPrice, String mallType, String passWord) {
        initProgressDialog("订单支付中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("OrderNo", orderNo);
        map.put("OrderCode", orderNo);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_PAY_ALI, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
                NetLogCat.W(NetWorkConstant.ORDER_PAY_ALI, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", orderNo);
                    startActivityWithExtras(OrderPayFinish.class, mBundle);
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });

    }

    /**
     * @param memberCode
     * @param token
     */
    private void RegPwd(String memberCode, final String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSE_IS_NULL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e("-----------" + statusCode);
                NetLogCat.W(NetWorkConstant.PURSE_IS_NULL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().equals("已有支付密码")) {
                    isPwd = true;
                    return true;
                }
                return false;
            }
        });
    }

}
