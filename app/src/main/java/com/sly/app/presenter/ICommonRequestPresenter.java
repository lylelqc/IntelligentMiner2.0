package com.sly.app.presenter;

import android.content.Context;

import java.util.Map;

/**
 * Created by  on 16/1/23.
 * 做页面请求控制
 *
 */
public interface ICommonRequestPresenter {

    void request(int eventTag, Context context, String url, Map<String, String> params);

    void requestGet(int eventTag, Context context, String url, Map<String, String> params);


}
