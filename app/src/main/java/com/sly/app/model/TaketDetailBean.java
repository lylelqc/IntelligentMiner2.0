package com.sly.app.model;

public class TaketDetailBean {


    /**
     * BillNo : 5AFCE4C1-00E7-4F1F-AE57-E7317AEFAC33
     * MachineCode : 743ce55c-ad9b-4565-94d9-6fbb0f46ae65
     * MachineName : 蚂蚁T9Plus
     * BeginTime : 2018-09-05 08:45:26
     * Analyse : 连接成功,但获取数据失败
     * RepairHours : 26
     * RunStatus : 离线（断电/断网）
     * MineCode : MineSunnyCloud
     * MineName : 算力云测试矿场
     * RepairPerson : 矿场主
     * RepairSum : 0
     * BillStatus : 未处理
     */
//    BillNo	string	维修单据号
//    MachineName	string	故障设别名称
//    BeginTime	datetime	发生故障时间
//    Analyse	string	故障分析
//    RepairHours	decimal	维修时间
//    RunStatus	string	维修后设备运行情况
    private String BillNo;
    private String MachineCode;
    private String MachineName;
    private String BeginTime;
    private String Analyse;
    private int RepairHours;
    private String RunStatus;
    private String MineCode;
    private String MineName;
    private String RepairPerson;
    private String RepairSum;
    private String BillStatus;


    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String BillNo) {
        this.BillNo = BillNo;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String MachineName) {
        this.MachineName = MachineName;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getAnalyse() {
        return Analyse;
    }

    public void setAnalyse(String Analyse) {
        this.Analyse = Analyse;
    }

    public int getRepairHours() {
        return RepairHours;
    }

    public void setRepairHours(int RepairHours) {
        this.RepairHours = RepairHours;
    }

    public String getRunStatus() {
        return RunStatus;
    }

    public void setRunStatus(String RunStatus) {
        this.RunStatus = RunStatus;
    }

    public String getMineCode() {
        return MineCode;
    }

    public void setMineCode(String MineCode) {
        this.MineCode = MineCode;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String MineName) {
        this.MineName = MineName;
    }

    public String getRepairPerson() {
        return RepairPerson;
    }

    public void setRepairPerson(String RepairPerson) {
        this.RepairPerson = RepairPerson;
    }

    public String getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(String RepairSum) {
        this.RepairSum = RepairSum;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String BillStatus) {
        this.BillStatus = BillStatus;
    }
}
