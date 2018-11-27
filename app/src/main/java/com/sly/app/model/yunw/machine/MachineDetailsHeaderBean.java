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
     */

    private String Coin;
    private String IP;
    private String Model;
    private String MinerSysCode;
    private String StatusName;
    private String AreaName;
    private String RunRate;

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
