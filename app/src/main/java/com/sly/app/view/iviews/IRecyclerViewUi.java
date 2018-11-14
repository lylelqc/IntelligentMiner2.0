package com.sly.app.view.iviews;

/**
 * RecyclerView 公用
 * Created by liucanwen on 16/1/22.
 */
public interface IRecyclerViewUi{
    /**
     *去刷新请求数据
     */
    void toRefreshRequest();

    /**
     * 去加载更多请求数据
     */
    void toLoadMoreRequest();

    /**
     * 得到刷新的结果
     * @param result
     */
    void getRefreshData(int eventTag, String result);

    /**
     * 得到加载更多的结果
     * @param result
     */
    void getLoadMoreData(int eventTag, String result);

    /**
     * 请求成功，但是服务器不能正确返回数据
     * @param eventTag
     * @param msg
     */
    void onRequestSuccessException(int eventTag, String msg);

    /**
     * 请求失败
     * @param eventTag
     * @param msg
     */
    void onRequestFailureException(int eventTag, String msg);

    /**
     * 是否正在刷新
     * @param status
     */
    void isRequesting(int eventTag, boolean status);
}
