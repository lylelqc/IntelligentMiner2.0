package com.sly.app.utils;

import android.content.Context;

import com.sly.app.model.sly.LoginInfoBean;
import com.sly.app.utils.http.OkHttpClientManager;
import com.sly.app.utils.http.OkHttpResponseHandler;

import java.util.Map;

/**
 * Http请求
 * Created by liucanwen on 15/12/7.
 */
public class HttpHelper {

    private static volatile HttpHelper instance = null;

    private HttpHelper() {
    }

    public static HttpHelper getInstance() {
        if (null == instance) {
            synchronized (HttpHelper.class) {
                if (null == instance) {
                    instance = new HttpHelper();
                }
            }
        }
        return instance;
    }

    /**
     * post 请求
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     */
    public void post(Context context, String url, Map<String, String> params,
                     OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;


        LoginInfoBean loginMsg = LoginMsgHelper.getResult(context);

//        if(loginMsg != null) {
//            params.put("token", loginMsg.getToken());
//        }
//        params.put("plat","1");

        System.out.println(url);
        System.out.println(params.toString());

//        OkHttpClientManager.postAsyn(url, params, responseHandler);
        OkHttpClientManager.postJson(url, params, responseHandler);

    }


    /**
     * get 请求
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     */
    public void get(Context context, String url, Map<String, String> params,
                     OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;

        if (url == null)
            return;

//        LoginInfoBean loginMsg = LoginMsgHelper.getResult(context);
//
//        if(loginMsg != null) {
//            params.put("token", loginMsg.getToken());
//            params.put("account",loginMsg.getAccount()+"");
//        }
//        params.put("plat","1");

        System.out.println(url);
        System.out.println(params.toString());
        url +="?";
        for (String key : params.keySet()) {
            String value = params.get(key);
            url += key + "=" + value +"&";
         }
        url = url.substring(0,url.length()-1);
        System.out.println("get请求》" + url);
        OkHttpClientManager.getAsyn(url, responseHandler);

    }

}
