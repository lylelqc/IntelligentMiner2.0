package com.sly.app.model;

public class SlyRechargeListBean {

    /**
     * rowid : 1  编号
     * times : 2018-09-10 18:18:32  充值时间
     * sums : 100  充值数额
     * truesum : 100  换算实付金额
     * statues : Refuse  审核状态
     * info :审核信息
     *
     */

    private int rowid;
    private String times;
    private int sums;
    private int truesum;
    private String statues;
    private String info;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getSums() {
        return sums;
    }

    public void setSums(int sums) {
        this.sums = sums;
    }

    public int getTruesum() {
        return truesum;
    }

    public void setTruesum(int truesum) {
        this.truesum = truesum;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
