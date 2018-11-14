package com.sly.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * 作者 by K
 * 时间：on 2017/8/22 13:56
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ReturnBean {


    /**
     * status : 1
     * msg : 成功
     * data : b2ff4a6c-ef25-4f24-af2d-d3c25048750e
     */

    private String status;
    private String msg;
    private String data;
    @SerializedName("data")


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
