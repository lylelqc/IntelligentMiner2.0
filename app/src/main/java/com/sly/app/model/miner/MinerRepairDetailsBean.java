package com.sly.app.model.miner;

public class MinerRepairDetailsBean {

    /**
     * \"BillNo\":\"2018121000000020\",
     \"MachineSysCode\":\"65ab7cb8-7614-4be8-a2c7-982af906596e\",
     \"MachineCode\":\"65ab7cb8-7614-4be8-a2c7-982af906596e\",
     \"Info\":\"设备异常:网络异常。IP:10.0.0.211,型号为:蚂蚁S9,所在区域为:1-01,当前算力为:0.00T\",
     \"Ptime\":\"2018-12-10 16:39:00\",
     \"repairInfo\":\"因设备恢复正常,默认已处理\",
     \"RepairSum\":0.0000,
     \"EndTime\":\"2018-12-10 17:29:00\",
     \"Model\":\"蚂蚁S9\",
     \"ResultName\":\"已处理\",
     \"RepairHours\":\"0小时50分\"
     ResultCode
     */
    private String BillNo;
    private String MachineSysCode;
    private String MachineCode;
    private String Info;
    private String Ptime;
    private String repairInfo;
    private double RepairSum;
    private String EndTime;
    private String Model;
    private String ResultName;
    private String RepairHours;
    private String ResultCode;

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getMachineSysCode() {
        return MachineSysCode;
    }

    public void setMachineSysCode(String machineSysCode) {
        MachineSysCode = machineSysCode;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getPtime() {
        return Ptime;
    }

    public void setPtime(String ptime) {
        Ptime = ptime;
    }

    public String getRepairInfo() {
        return repairInfo;
    }

    public void setRepairInfo(String repairInfo) {
        this.repairInfo = repairInfo;
    }

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getResultName() {
        return ResultName;
    }

    public void setResultName(String resultName) {
        ResultName = resultName;
    }

    public String getRepairHours() {
        return RepairHours;
    }

    public void setRepairHours(String repairHours) {
        RepairHours = repairHours;
    }
}
