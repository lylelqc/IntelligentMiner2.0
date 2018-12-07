package com.sly.app.model.master;

public class MasterMachineListBean {

    /**
     * {\"rowid\":1,
     \"MachineSysCode\":\"4f4bc23d-7788-4e8b-b94d-36beb70826d8\",
     \"MachineCode\":\"J00003563\",
     \"IP\":\"10.18.1.9\",
     \"NowCal\":0.00,
     \"Model\":\"蚂蚁S9\",
     \"StatusName\":\"离线（断电\/断网）\",
     \"AreaName\":\"test-3\",
     \"Mobile\":\"18128757662\",
     \"Email\":\"\",
     \"Worker\":\"f1000s9shz1801.10x18x1x9\"}
     */

    private int rowid;
    private String MachineSysCode;
    private String MachineCode;
    private String IP;
    private double NowCal;
    private String Model;
    private String StatusName;
    private String AreaName;
    private String Mobile;
    private String Email;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWorker() {
        return Worker;
    }

    public void setWorker(String worker) {
        Worker = worker;
    }
}
