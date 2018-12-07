package com.sly.app.model.yunw.machine;

public class MachineStatusBean {

    /**
     * "StatusCode\":\"01\",
     * \"StatusName\":\"离线（断电\/断网）\"
     */
    private String StatusCode;
    private String StatusName;

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
}
