package com.sly.app.model.miner;

public class MinerRepairFreeListBean {

    /**
     * rowid":1,
     * "Mine24_Time":"2018-11-15 15:19:00",
     * "Mine25_RepairSum":0.0000
     */

    private int rowid;
    private String Mine24_Time;
    private String Mine24_BillNo;
    private double Mine25_RepairSum;

    public String getMine24_BillNo() {
        return Mine24_BillNo;
    }

    public void setMine24_BillNo(String mine24_BillNo) {
        Mine24_BillNo = mine24_BillNo;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getMine24_Time() {
        return Mine24_Time;
    }

    public void setMine24_Time(String mine24_Time) {
        Mine24_Time = mine24_Time;
    }

    public double getMine25_RepairSum() {
        return Mine25_RepairSum;
    }

    public void setMine25_RepairSum(double mine25_RepairSum) {
        Mine25_RepairSum = mine25_RepairSum;
    }
}
