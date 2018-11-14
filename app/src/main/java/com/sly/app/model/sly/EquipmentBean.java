package com.sly.app.model.sly;

public class EquipmentBean {
    /**
     * RowNo : 1
     * MineName : 矿场测试8号
     * ModelName : 翼比特E9.1
     * MachineCount : 1
     * RunHours : 178.86638888
     * RunRate : 0.33072948616207937
     */

    private int RowNo;
    private String MineName;
    private String ModelName;
    private int MachineCount;
    private double RunHours;
    private double RunRate;
    private String TimeShow;

    public int getRowNo() {
        return RowNo;
    }

    public void setRowNo(int RowNo) {
        this.RowNo = RowNo;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String MineName) {
        this.MineName = MineName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public int getMachineCount() {
        return MachineCount;
    }

    public void setMachineCount(int MachineCount) {
        this.MachineCount = MachineCount;
    }

    public double getRunHours() {
        return RunHours;
    }

    public void setRunHours(double RunHours) {
        this.RunHours = RunHours;
    }

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double RunRate) {
        this.RunRate = RunRate;
    }

    public String getTimeShow() {
        return TimeShow;
    }

    public void setTimeShow(String timeShow) {
        TimeShow = timeShow;
    }
}
