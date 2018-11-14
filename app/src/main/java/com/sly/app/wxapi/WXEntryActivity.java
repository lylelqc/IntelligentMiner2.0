/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.wxapi;

import android.app.Activity;

import com.sly.app.comm.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import vip.devkit.library.Logcat;


/**
 * 文 件 名: WXEntryActivity
 * 创 建 人: By k
 * 创建日期: 2018/1/18 10:54
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 说    明：
 * 修改时间：
 * 修改备注：
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mApi;
    @Override
    public void onReq(BaseReq baseReq) {
        mApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        mApi.handleIntent(this.getIntent(), this);

    }

    @Override
    public void onResp(BaseResp resp) {
        Logcat.i("-------------:"+resp.errStr+"/"+resp.openId);

        try{
            SendAuth.Resp sResp = (SendAuth.Resp) resp;
            if(sResp.state.equals("wechat_sdk_userinfo_login")){
                Logcat.i("=====================:"+sResp);
            }
        }catch (Exception e){
        }
        finish();
    }
}
