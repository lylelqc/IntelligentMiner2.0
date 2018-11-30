package com.sly.app.model.yunw.goline;

public class PlanMachineListBean {

    /**
     * \"Worker\":\"sunyang001\",
     \"IsOnline\":true,
     \"Model\":\"蚂蚁S9\",
     \"ExeTime\":\"2018-11-16 14:37:02\",
     \"Owner\":\"M0000065\",
     \"ScanCount\":380}
     */

    private String Worker;
    private boolean IsOnline;
    private String Model;
    private String ExeTime;
    private String Owner;
    private int ScanCount;

    public String getWorker() {
        return Worker;
    }

    public void setWorker(String worker) {
        Worker = worker;
    }

    public boolean isOnline() {
        return IsOnline;
    }

    public void setOnline(boolean online) {
        IsOnline = online;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getExeTime() {
        return ExeTime;
    }

    public void setExeTime(String exeTime) {
        ExeTime = exeTime;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public int getScanCount() {
        return ScanCount;
    }

    public void setScanCount(int scanCount) {
        ScanCount = scanCount;
    }
}
