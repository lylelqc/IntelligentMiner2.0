package com.sly.app.view.iviews;

/**
 * Created by liucanwen on 16/1/23.
 */
public interface ICommonViewUi {

    /**
     *去请求数据
     */
    void toRequest(int eventTag);

    /**
     * 得到请求的结果
     * @param result
     */
    void getRequestData(int eventTag, String result);

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
