package com.sly.app.model.miner;

public class MinerMachineNumBean {

    /**
     * "count\":800,
     * \"onLineCount\":108,
     * \"offLineCount\":668,
     * \"abnormalCount\":0,
     * \"downtimeCount\":0
     */
    private int count;
    private int onLineCount;
    private int offLineCount;
    private int abnormalCount;
    private int downtimeCount;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOnLineCount() {
        return onLineCount;
    }

    public void setOnLineCount(int onLineCount) {
        this.onLineCount = onLineCount;
    }

    public int getOffLineCount() {
        return offLineCount;
    }

    public void setOffLineCount(int offLineCount) {
        this.offLineCount = offLineCount;
    }

    public int getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public int getDowntimeCount() {
        return downtimeCount;
    }

    public void setDowntimeCount(int downtimeCount) {
        this.downtimeCount = downtimeCount;
    }
}
