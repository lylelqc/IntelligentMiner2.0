package com.sly.app.model.sly;

public class RepairDetailsBean {
    /**
     *
     * "isAccept\":\"已确认\",
     * \"ptime\":\"2018\/09\/06\",
     \"AcceptInfo\":\"已经支付成功\",
     \"UseInfo\":\"维修1\",
     \"RepairSum\":10.0000,
     \"Remark\":\"\",
     \"RepairInfo\":\"处理完成\",
     \"detailid\":\"414ADD7B-6CD1-4985-82AF-111329E87C3C\",
     \"billno\":\"A5C5962F-496D-4253-A356-20EB3EB769A7\",
     \"resultname\":\"已处理\",
     \"minename\":\"矿场主\"**/

    private String isAccept;
    private String ptime;
    private String AcceptInfo;
    private String UseInfo;
    private double RepairSum;
    private String Remark;
    private String RepairInfo;
    private String detailid;
    private String billno;
    private String resultname;
    private String minename;

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getAcceptInfo() {
        return AcceptInfo;
    }

    public void setAcceptInfo(String acceptInfo) {
        AcceptInfo = acceptInfo;
    }

    public String getUseInfo() {
        return UseInfo;
    }

    public void setUseInfo(String useInfo) {
        UseInfo = useInfo;
    }

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getRepairInfo() {
        return RepairInfo;
    }

    public void setRepairInfo(String repairInfo) {
        RepairInfo = repairInfo;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getResultname() {
        return resultname;
    }

    public void setResultname(String resultname) {
        this.resultname = resultname;
    }

    public String getMinename() {
        return minename;
    }

    public void setMinename(String minename) {
        this.minename = minename;
    }
}
