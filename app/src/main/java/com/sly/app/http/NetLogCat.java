package com.sly.app.http;

import com.sly.app.utils.CommonUtils;

import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/20 18:34
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class NetLogCat{

    public static void StatuCode(int statusCode) {
        Logcat.i(" HTTP 状态码：" + statusCode);
        Logcat.e(" HTTP 状态码：" + statusCode);
    }

    public static void I(String url, int statusCode, String string, Headers headers) {
        Logcat.i("\n\t API 地址：" + url + "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" +string+ "\n\t Headers:" + headers);
    }

    public static void I(String url,String json, int statusCode, String string) {
        Logcat.i("\n\t API 地址：" + url + "\n\t 提交的参数：" +json+"\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }

    public static void E(String url,String json, int statusCode, String string, Headers headers) {
        Logcat.e("\n\t API 地址：" + url + "\n\t 提交的参数：" +json+ "\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + string + "\n\t Headers:" + headers);
    }

    public static void E(String url, String json,int statusCode, String string) {
        Logcat.i("\n\t API 地址：" + url + "\n\t 提交的参数：" +json+"\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }
    public static void W(String url, String json,int statusCode, String string) {
        Logcat.w("\n\t API 地址：" + url + "\n\t 提交的参数：" +json+"\n\t HTTP 状态码：" + statusCode + "\n\t 返回内容：" + CommonUtils.proStr(string));
    }
}
