package com.sly.app.model.sly.balance;

import java.util.List;

public class SlyReturnListBean {


    /**
     * status : 0
     * msg : 成功
     * data : {"Rows":[{"Pay26_ID":11,"Pay26_UserCode":"4D92084E-12EA-43E5-8797-4E204C8ABD45","Pay26_AccountName":"京京","Pay26_AccountNo":"6456464564654","Pay26_Branch":"农业","Pay26_BankCode":"ABC","Pay25_BankName":"中国农业银行","Pay25_Logo":"/img/banklogo/abc.png"}]}
     */

    private String status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<?> Rows;

        public List<?> getRows() {
            return Rows;
        }

        public void setRows(List<?> Rows) {
            this.Rows = Rows;
        }
    }
}
