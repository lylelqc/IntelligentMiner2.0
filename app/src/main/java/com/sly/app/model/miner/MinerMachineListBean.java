package com.sly.app.model.miner;

public class MinerMachineListBean {

    /**
     * \"rowid\":1,
     \"StatusName\":\"离线（断电\/断网）\",
     \"MachineSysCode\":\"0040f6b6-35eb-47d5-96b8-e174b5656040\",
     \"MachineCode\":\"J00024743\",
     \"Model\":\"蚂蚁S9\",
     \"MineCode\":\"Z000016\",
     \"NowCal\":0.00
     */
    private int rowid;
    private String StatusName;
    private String MachineSysCode;
    private String MachineCode;
    private String Model;
    private String MineCode;
    private double NowCal;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
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

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getMineCode() {
        return MineCode;
    }

    public void setMineCode(String mineCode) {
        MineCode = mineCode;
    }

    public double getNowCal() {
        return NowCal;
    }

    public void setNowCal(double nowCal) {
        NowCal = nowCal;
    }
}
