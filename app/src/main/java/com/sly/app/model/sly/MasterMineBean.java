package com.sly.app.model.sly;

public class MasterMineBean {
    /**
     *\"MineCode\":\"Z000013\",
     * \"mineName\":\"二号测试矿场\",
     * \"modelname\":\"蚂蚁S9系列\",
     \"Model\":\"蚂蚁S9\",
     \"calcFore\":62710.68,
     \"acount\":10,
     \"faultcount\":0,
     \"runrate\":1.0000
     * ***/

    private String MineCode;
    private String mineName;
    private String modelname;
    private String Model;
    private double calcFore;
    private int acount;
    private int faultcount;
    private double runrate;

    public String getMineCode() {
        return MineCode;
    }

    public void setMineCode(String mineCode) {
        MineCode = mineCode;
    }

    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public double getCalcFore() {
        return calcFore;
    }

    public void setCalcFore(double calcFore) {
        this.calcFore = calcFore;
    }

    public int getAcount() {
        return acount;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public int getFaultcount() {
        return faultcount;
    }

    public void setFaultcount(int faultcount) {
        this.faultcount = faultcount;
    }

    public double getRunrate() {
        return runrate;
    }

    public void setRunrate(double runrate) {
        this.runrate = runrate;
    }
}
