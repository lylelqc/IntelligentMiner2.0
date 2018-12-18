package com.sly.app.model.yunw.machine;

public class MachineDetailsHeaderBean {

    /**
     * \"Coin\":\"btc\",
     \"IP\":\"10.18.1.125\",
     \"Model\":\"蚂蚁S9\",
     \"MinerSysCode\":\"M0000059\",
     \"StatusName\":\"离线（断电\/断网）\",
     \"AreaName\":\"test-3\",
     \"RunRate\":\"0.0007\"


     \"Coin\":\"BTC\",
     \"IP\":\"10.0.0.211\",
     \"Model\":\"蚂蚁S9\",
     \"MinerSysCode\":\"M0000077\",
     \"MachineSysCode\":\"65ab7cb8-7614-4be8-a2c7-982af906596e\",
     \"MachineCode\":\"J00024944\",
     \"MineName\":\"石河子-148\",
     \"StatusName\":\"在线\",
     \"AreaName\":\"1-01\",
     \"RunRate\":\"0.1465\"
     */


    private String Coin;
    private String IP;
    private String Model;
    private String MinerSysCode;
    private String MachineSysCode;
    private String MachineCode;
    private String MineName;
    private String StatusName;
    private String AreaName;
    private String RunRate;

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

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String mineName) {
        MineName = mineName;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getMinerSysCode() {
        return MinerSysCode;
    }

    public void setMinerSysCode(String minerSysCode) {
        MinerSysCode = minerSysCode;
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

    public String getRunRate() {
        return RunRate;
    }

    public void setRunRate(String runRate) {
        RunRate = runRate;
    }
}
