package com.sly.app.presenter;

import android.content.Context;

import java.util.Map;

/**
 * 公用RecyclerView Present 接口
 * 适用于RecyclerView 列表实现
 */
public interface IRecyclerViewPresenter {

    /**
     * 请求数据
     * @param eventTag
     * @param context
     * @param url
     * @param map
     */
    void loadData(int eventTag, Context context, String url, Map<String, String> map);
}
