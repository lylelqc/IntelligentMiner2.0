package com.sly.app.model.yunw.repair;

public class RepairBillDetailsBean {

    /**
     * \"BillNo\":\"2018111900000034\",
     \"Ptime\":\"2018-11-19 13:40:00 \",
     \"Info\":\"设备异常:设备算力异常,当前算力为:0.00T。IP:192.168.20.96,型号为:蚂蚁S9,所在区域为:C,当前算力为:0.0000000000T\",
     \"ResultCode\":\"04\",
     \"MachineSysCode\":\"81a9f9cf-0fbf-4fde-a899-807917a0e5c1\",
     \"RepairSum\":0.0000,
     \"RepairInfo\":\"因设备恢复正常,默认已处理\",
     \"EndTime\":\"2018-11-19 14:30:00 \",
     \"ResultName\":\"已处理\",
     \"Model\":\"蚂蚁S9\",
     \"IP\":\"192.168.20.96\",
     \"AreaName\":\"C\",
     \"RepairHours\":\"0小时50分\"
     \AcceptTime
     \"DetailID\":\"0C150155-972B-4CB6-B5B3-5BCD5DC25BC9\",
     */

    private String BillNo;
    private String DetailID;
    private String Ptime;
    private String Info;
    private String ResultCode;
    private String MachineSysCode;
    private double RepairSum;
    private String RepairInfo;
    private String EndTime;
    private String ResultName;
    private String Model;
    private String IP;
    private String AreaName;
    private String RepairHours;
    private String AcceptTime;

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getDetailID() {
        return DetailID;
    }

    public void setDetailID(String detailID) {
        DetailID = detailID;
    }

    public String getPtime() {
        return Ptime;
    }

    public void setPtime(String ptime) {
        Ptime = ptime;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getMachineSysCode() {
        return MachineSysCode;
    }

    public void setMachineSysCode(String machineSysCode) {
        MachineSysCode = machineSysCode;
    }

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public String getRepairInfo() {
        return RepairInfo;
    }

    public void setRepairInfo(String repairInfo) {
        RepairInfo = repairInfo;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getResultName() {
        return ResultName;
    }

    public void setResultName(String resultName) {
        ResultName = resultName;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getRepairHours() {
        return RepairHours;
    }

    public void setRepairHours(String repairHours) {
        RepairHours = repairHours;
    }
}
