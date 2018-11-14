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
package com.sly.app.utils;

import android.app.Activity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.sly.app.comm.Constants.WX_APP_ID;


/**
 * 文 件 名: LoginUtil
 * 创 建 人: By k
 * 创建日期: 2018/1/18 10:12
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 说    明：
 * 修改时间：
 * 修改备注：
 */
public class LoginUtil {


    public static void WXLogin( final Activity activity){
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity,WX_APP_ID);
        msgApi.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_userinfo_login";
        msgApi.sendReq(req);
    }
    private static final String TAG = "";

    public static String  getLogTagWithMethod() {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
    }
}
