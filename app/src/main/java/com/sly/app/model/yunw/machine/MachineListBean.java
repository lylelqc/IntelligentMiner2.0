package com.sly.app.model.yunw.machine;

public class MachineListBean {

    /**
     * \"rowid\":1,
     \"MachineSysCode\":\"ffdccd4d-776c-421a-9f9d-6edcb6108aee\",
     \"MachineCode\":\"J00003549\",
     \"IP\":\"10.18.1.125\",
     \"NowCal\":13657.2600,
     \"StatusCode\":\"01\",
     \"AreaCode\":\"dd4d8618-32e5-4c24-b6cf-3c4a8690be93\",
     \"MinerSysCode\":\"M0000059\",
     \"Model\":\"蚂蚁S9\",
     \"StatusName\":\"离线（断电\/断网）\",
     \"AreaName\":\"test-3\",
     \"Worker\":\"f1000s9shz1801.10x18x1x125\"
     */
    private int rowid;
    private String MachineSysCode;
    private String MachineCode;
    private String IP;
    private double NowCal;
    private String StatusCode;
    private String AreaCode;
    private String MinerSysCode;
    private String Model;
    private String StatusName;
    private String AreaName;
    private String Worker;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getMachineSysCode() {
        return MachineSysCode;
    }

    public void setMachineSysCode(String machineSysCode) {
        MachineSysCode = machineSysCode;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public double getNowCal() {
        return NowCal;
    }

    public void setNowCal(double nowCal) {
        NowCal = nowCal;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    public String getMinerSysCode() {
        return MinerSysCode;
    }

    public void setMinerSysCode(String minerSysCode) {
        MinerSysCode = minerSysCode;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getWorker() {
        return Worker;
    }

    public void setWorker(String worker) {
        Worker = worker;
    }
}
