package com.sly.app.listener;

/**
 * Created by on 15/12/7.
 */
public interface IRequestListener {

    /**
     * when data call back success
     *
     * @param data
     */
    void onSuccess(int eventTag, String data);

    /**
     * when data call back error
     *
     * @param msg
     */
    void onError(int eventTag, String msg);

    /**
     * 是否正在刷新
     * @param status
     * @return
     */
    void isRequesting(int eventTag, boolean status);

}
