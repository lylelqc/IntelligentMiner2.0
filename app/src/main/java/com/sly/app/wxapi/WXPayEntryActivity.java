package com.sly.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sly.app.R;
import com.sly.app.comm.Constants;
import com.sly.app.model.pay.WxPayAction;
import com.sly.app.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import vip.devkit.library.Logcat;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPay";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Logcat.e(TAG, req.openId + "");
        Logcat.e(TAG, req.transaction + "");
    }

    @Override
    public void onResp(BaseResp resp) {
        Logcat.e(TAG, resp.errCode + "");
        Logcat.e(TAG, resp.errStr + "");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String payUseing = WxPayAction.getPayUseing();
            String wwwType = WxPayAction.getWwwType();
            String OrderNo = WxPayAction.getOrderNo();
            if (resp.errCode == 800) {
                ToastUtils.showToast("请重新操作、谢谢");
                finish();
            } else if (resp.errCode == 0) {
                if (payUseing.equals("Pay")) {
                    ToastUtils.showToast("支付成功");
//                    Intent intent = new Intent(WXPayEntryActivity.this, OrderPayFinish.class);
                    Intent intent = new Intent();
                    intent.putExtra("MallType", wwwType);
                    intent.putExtra("OrderNo", OrderNo);
                    startActivity(intent);
                } else if (payUseing.equals("cz")) {
                    ToastUtils.showToast("充值成功");
//                    Intent intent = new Intent(WXPayEntryActivity.this, ConsumerActivity.class);
                    Intent intent = new Intent();
                    intent.putExtra("MallType", wwwType);
                    intent.putExtra("OrderNo", OrderNo);
                    startActivity(intent);
                }
            } else if (resp.errCode == -1) {
                ToastUtils.showToast("支付失败");
                finish();
            } else if (resp.errCode == -2) {
                ToastUtils.showToast("取消支付");
                finish();
            }
        }
        finish();
    }
}