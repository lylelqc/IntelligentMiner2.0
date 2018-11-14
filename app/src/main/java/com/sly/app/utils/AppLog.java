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

import vip.devkit.library.Logcat;

/**
 * 文 件 名: AppLog
 * 创 建 人: By k
 * 创建日期: 2017/11/9 11:04
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class AppLog extends Logcat {
    public static void LogCatIs(String url, String json, int statusCode, String string) {
        Logcat.i("\n\t API 地址：" + url + "\n\t 提交的参数：" + json + "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }

    public static void LogCatI(String url, String json, int statusCode, String string) {
        Logcat.i("\n\t API 地址：" + url + "\n\t 提交的参数：" + json + "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }

    public static void LogCatE(String url, String json, int statusCode, String string) {
        AppLog.e("\n\t API 地址：" + url + "\n\t 提交的参数：" + json + "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }

    public static void LogCatW(String url, String json, int statusCode, String string) {
        Logcat.w("\n\t API 地址：" + url + "\n\t 提交的参数：" + json + "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));

    }
}
