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
package com.sly.app.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.notice.AllNoticeBean;
import com.sly.app.utils.AppLog;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;


/**
 * 文 件 名: PushService
 * 创 建 人: By k
 * 创建日期: 2017/11/8 11:36
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class PushService extends Service {


    private String Token, MemberCode;
    private MyBinder mBinder = new MyBinder();
    private Context mContext;
    private String User;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logcat.i("onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logcat.i("onDestroy() executed");
    }

    public class MyBinder extends Binder {

        public void startPushService() {
            Logcat.i("TAG", "startDownload() executed");
            // 执行具体的任务
            Token = SharedPreferencesUtil.getString(mContext, "Token");
             User = SharedPreferencesUtil.getString(mContext, "User");
            if (CommonUtils.isBlank(User) || CommonUtils.isBlank(Token)) {
                Logcat.i("用户未登录");
            } else {
               // getMessageWarn(MemberCode, Token);
            }
        }
    }
    private void getMessageWarn(String memberCode,String token) {
        final Map<String, String> map = new HashMap();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.GetMessage, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW("Service \n"+NetWorkConstant.GetMessage, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                 List<AllNoticeBean> noticeBean = JSON.parseArray(mReturnBean.getData(),AllNoticeBean.class);
                    Intent intent = new Intent();
                    intent.setAction("AppReceiver");
                    Bundle mBundle = new Bundle();
                    if (noticeBean.get(0).getMemberMessageClassCode().equals("01")){
                        mBundle.putString("title", "提现审核通知");
                    }else   if (noticeBean.get(0).getMemberMessageClassCode().equals("02")){
                        mBundle.putString("title", "个人订单物流信息");
                    }else   if (noticeBean.get(0).getMemberMessageClassCode().equals("03")){
                        mBundle.putString("title", "加盟订单审核");
                    }else   if (noticeBean.get(0).getMemberMessageClassCode().equals("04")){
                        mBundle.putString("title", "积分赠送");
                    }
                    mBundle.putString("info",noticeBean.get(0).getTitle());
                    mBundle.putString("json", CommonUtils.GsonToJson(noticeBean));
                    intent.putExtras(mBundle);
                    sendBroadcast(intent);
                } else {
                    AppLog.i("是否有新的通知："+mReturnBean.getMsg());
                }
            }
        });
    }

}
