package com.sly.app.model.yunw.machine;

public class MachineStatusBean {

    /**
     * "StatusCode\":\"01\",
     * \"StatusName\":\"离线（断电\/断网）\"
     * \"num\":3
     */
    private String StatusCode;
    private String StatusName;
    private int num;

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
