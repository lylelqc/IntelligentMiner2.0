package com.sly.app.model.sly;

public class RepairDocBean {
    /**
     *  "rowid\":1,
     \"billno\":\"2018101700002414\",
     \"areaname\":\"test-5\",
     \"ip\":\"10.18.3.56\",
     \"ptime\":\"2018\/10\/17\",
     \"endtime\":null,
     \"info\":\"温度异常,T1:50.0000,T2:132.0000,T3:47.0000\",
     \"resultCode\":\"00\",
     \"resultname\":\"未处理\"
     * **/

    private int rowid;
    private String billno;
    private String areaname;
    private String ip;
    private String ptime;
    private String endtime;
    private String info;
    private String resultCode;
    private String resultname;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultname() {
        return resultname;
    }

    public void setResultname(String resultname) {
        this.resultname = resultname;
    }
}
