package com.sly.app.model.sly;

public class MyMachineDetail {

    /**
     * Model,Price,NHRate,TimeShow
     * MachineCode : J00000313
     * MineName : 矿场测试8号
     * RunHours : 193.796388888
     * RunRate : 0.227253116458138
     * StatusName : 离线（断电/断网）
     * PowerSum : 33.030625
     * Pools : 192.168.136.106:3335,bencong2070xj.121X106,123|192.168.136.106:3333,bencong2070xj.121X106,123|stratum.btcc.com:1800,bencong2070xj.121X106,123|
     */

    private String MachineCode;
    private String MineName;
    private String RunHours;
    private String RunRate;
    private String StatusName;
    private String PowerSum;
    private String Pools;
    private String TimeShow;
    private String Model;
    private String Price;
    private String NHRate;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String MineName) {
        this.MineName = MineName;
    }

    public String getRunHours() {
        return RunHours;
    }

    public void setRunHours(String RunHours) {
        this.RunHours = RunHours;
    }

    public String getRunRate() {
        return RunRate;
    }

    public void setRunRate(String RunRate) {
        this.RunRate = RunRate;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    public String getPowerSum() {
        return PowerSum;
    }

    public void setPowerSum(String PowerSum) {
        this.PowerSum = PowerSum;
    }

    public String getPools() {
        return Pools;
    }

    public void setPools(String Pools) {
        this.Pools = Pools;
    }

    public String getTimeShow() {
        return TimeShow;
    }

    public void setTimeShow(String timeShow) {
        TimeShow = timeShow;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNHRate() {
        return NHRate;
    }

    public void setNHRate(String NHRate) {
        this.NHRate = NHRate;
    }
}
