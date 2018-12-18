package com.sly.app.model.yunw.repair;

public class RepairBillBean {

    /**
     * \"rowid\":1809,
     \"BillNo\":\"2018111500000032\",
     \"Ptime\":\"2018-11-15 21:16:00\",
     \"Info\":\"设备异常:设备算力异常,当前算力为:0.00T。IP:2.1.11.89,型号为:蚂蚁S9,所在区域为:B,当前算力为:0.0000000000T\",
     \"Model\":\"蚂蚁S9\",
     \"AreaName\":\"B\",
     \"IP\":\"2.1.11.89\"
     RepairSum
     * */

    private int rowid;
    private String BillNo;
    private String Ptime;
    private String Info;
    private String Model;
    private String AreaName;
    private String IP;
    private double RepairSum;

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getPtime() {
        return Ptime;
    }

    public void setPtime(String ptime) {
        Ptime = ptime;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
