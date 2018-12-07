package com.sly.app.model.sly;

public class MyMachineBean {
    /**
     * RowNo : 2
     * MachineCode : J00000261
     * MineName : 矿场测试8号
     * RunHours : 191.426666666
     * RunRate : 0.047627754173341
     * StatusName : 算力异常
     * PowerSum : 6.837917
     */
//    MachineCode:设备编号
//    MineName:矿场名称
//    RunHours:运行时长
//    RunRate:运行率
//    StatusName:状态
//    PowerSum：电费
    private String RowNo;
    private String MachineCode;
    private String MineName;
    private String RunHours;
    private String MineCode;

    public String getMineCode() {
        return MineCode;
    }

    public void setMineCode(String mineCode) {
        MineCode = mineCode;
    }

    public String getMachineSysCode() {
        return MachineSysCode;
    }

    public void setMachineSysCode(String machineSysCode) {
        MachineSysCode = machineSysCode;
    }

    private String MachineSysCode;
    private double RunRate;
    private String StatusName;
    private double PowerSum;
    private String TimeShow;

    public String getTimeShow() {
        return TimeShow;
    }

    public void setTimeShow(String timeShow) {
        TimeShow = timeShow;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String RowNo) {
        this.RowNo = RowNo;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String MineName) {
        this.MineName = MineName;
    }

    public String getRunHours() {
        return RunHours;
    }

    public void setRunHours(String RunHours) {
        this.RunHours = RunHours;
    }

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double RunRate) {
        this.RunRate = RunRate;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    public double getPowerSum() {
        return PowerSum;
    }

    public void setPowerSum(double PowerSum) {
        this.PowerSum = PowerSum;
    }
}
