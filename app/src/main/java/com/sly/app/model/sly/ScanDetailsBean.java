package com.sly.app.model.sly;

public class ScanDetailsBean {

    private String Worker;
    private boolean IsOnline;
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
