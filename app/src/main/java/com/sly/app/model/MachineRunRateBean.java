package com.sly.app.model;

public class MachineRunRateBean {

    /**
     *
     * "StatusCode":"00",
     * "Status":"在线",
     * "MachineCount":983,
     * "Rate":0.9840}**/

    private String StatusCode;
    private String Status;
    private int MachineCount;
    private double Rate;

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getMachineCount() {
        return MachineCount;
    }

    public void setMachineCount(int machineCount) {
        MachineCount = machineCount;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }
}
