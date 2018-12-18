package com.sly.app.model.miner;

public class MinerManageFreeListBean {

    /**
     * "rowid":1,
     * "Mine28_GenerateTime":"2018-12-07 19:00:19",
     * "Mine28_Sum":0.1680
     */

    private int rowid;
    private String Mine28_GenerateTime;
    private double Mine28_Sum;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getMine28_GenerateTime() {
        return Mine28_GenerateTime;
    }

    public void setMine28_GenerateTime(String mine28_GenerateTime) {
        Mine28_GenerateTime = mine28_GenerateTime;
    }

    public double getMine28_Sum() {
        return Mine28_Sum;
    }

    public void setMine28_Sum(double mine28_Sum) {
        Mine28_Sum = mine28_Sum;
    }
}
