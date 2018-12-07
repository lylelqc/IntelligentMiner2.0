package com.sly.app.model.sly;

public class MDetailsHeaderBean {

    /**
     * Mine07_MineMachineStatusName\":\"在线\",
     \"runRate\":1.0000000000000,
     \"Mine08_Model\":\"蚂蚁T9Plus\",
     \"Mine08_IP\":\"192.168.108.36\",
     \"Mine06_MineAreaName\":\"b3_6\",
       Mine01_MineName //矿工的区域
     \"Mine08_CoinType\":\"btc\"**/

    private String Mine07_MineMachineStatusName;
    private double runRate;
    private String Mine08_Model;
    private String Mine08_IP;
    private String Mine06_MineAreaName;
    private String Mine01_MineName;
    private String Mine08_CoinType;

    public String getMine01_MineName() {
        return Mine01_MineName;
    }

    public void setMine01_MineName(String mine01_MineName) {
        Mine01_MineName = mine01_MineName;
    }

    public String getMine07_MineMachineStatusName() {
        return Mine07_MineMachineStatusName;
    }

    public void setMine07_MineMachineStatusName(String mine07_MineMachineStatusName) {
        Mine07_MineMachineStatusName = mine07_MineMachineStatusName;
    }

    public double getRunRate() {
        return runRate;
    }

    public void setRunRate(double runRate) {
        this.runRate = runRate;
    }

    public String getMine08_Model() {
        return Mine08_Model;
    }

    public void setMine08_Model(String mine08_Model) {
        Mine08_Model = mine08_Model;
    }

    public String getMine08_IP() {
        return Mine08_IP;
    }

    public void setMine08_IP(String mine08_IP) {
        Mine08_IP = mine08_IP;
    }

    public String getMine06_MineAreaName() {
        return Mine06_MineAreaName;
    }

    public void setMine06_MineAreaName(String mine06_MineAreaName) {
        Mine06_MineAreaName = mine06_MineAreaName;
    }

    public String getMine08_CoinType() {
        return Mine08_CoinType;
    }

    public void setMine08_CoinType(String mine08_CoinType) {
        Mine08_CoinType = mine08_CoinType;
    }
}
