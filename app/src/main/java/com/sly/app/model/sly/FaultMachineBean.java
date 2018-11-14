package com.sly.app.model.sly;

public class FaultMachineBean {
    /*
    *
    * code	string	矿机编号
    syscode	string	矿机系统编号
    model	string	型号
    info	string	维修信息
    runrate	string	运行率
    ManagTime	string	维修时长**/

    private String code;
    private String syscode;
    private String model;
    private String info;
    private String runrate;
    private String ManagTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSyscode() {
        return syscode;
    }

    public void setSyscode(String syscode) {
        this.syscode = syscode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRunrate() {
        return runrate;
    }

    public void setRunrate(String runrate) {
        this.runrate = runrate;
    }

    public String getManagTime() {
        return ManagTime;
    }

    public void setManagTime(String managTime) {
        ManagTime = managTime;
    }
}
