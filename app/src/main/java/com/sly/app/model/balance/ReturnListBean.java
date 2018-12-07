package com.sly.app.model.balance;

import java.util.List;

/**
 * Created by 01 on 2017/9/26.
 */
public class ReturnListBean {

    /**
     * status : 1
     * msg : 成功
     * data : []
     */

    private String status;
    private String msg;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
