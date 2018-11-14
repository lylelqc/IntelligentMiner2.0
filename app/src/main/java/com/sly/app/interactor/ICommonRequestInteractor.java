package com.sly.app.interactor;

import android.content.Context;

import java.util.Map;

public interface ICommonRequestInteractor {

    void request(int eventTag, Context context, String url, Map<String, String> map);

    void requestGet(int eventTag, Context context, String url, Map<String, String> map);


}
