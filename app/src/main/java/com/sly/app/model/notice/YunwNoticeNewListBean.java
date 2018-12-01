package com.sly.app.model.notice;

public class YunwNoticeNewListBean {
    /**
     * \"F48_MessageID\":\"B2AF6AA0-9E30-4889-B32C-A0733431CF94\",
     \"F48_MinePersonSysCode\":\"dc54a704-5592-4f41-bb7e-95a7430985bb\",
     \"F48_Title\":\"矿机故障\",
     \"F48_Time\":\"2018-11-20 14:12:47\",
     \"F48_Message\":\"设备异常:网络异常。IP:2.1.8.4,型号为:蚂蚁S9,所在区域为:B,当前算力为:0.0000000000T\",
     \"F48_MessageClass\":\"01\",
     \"F48_IsPush\":false,
     \"F48_IsRead\":false,
     \"F48_ReadTime\":null,
     \"F48_BillNo\":\"2018112000000083\",
     \"Status\":1,
     \"PrimaryKey\":\"B2AF6AA0-9E30-4889-B32C-A0733431CF94\"
     */

    private String F48_MessageID;
    private String F48_MinePersonSysCode;
    private String F48_Title;
    private String F48_Time;
    private String F48_Message;
    private String F48_MessageClass;
    private boolean F48_IsPush;
    private boolean F48_IsRead;
    private String F48_ReadTime;
    private String F48_BillNo;
    private int Status;
    private String PrimaryKey;

    public String getF48_MessageID() {
        return F48_MessageID;
    }

    public void setF48_MessageID(String f48_MessageID) {
        F48_MessageID = f48_MessageID;
    }

    public String getF48_MinePersonSysCode() {
        return F48_MinePersonSysCode;
    }

    public void setF48_MinePersonSysCode(String f48_MinePersonSysCode) {
        F48_MinePersonSysCode = f48_MinePersonSysCode;
    }

    public String getF48_Title() {
        return F48_Title;
    }

    public void setF48_Title(String f48_Title) {
        F48_Title = f48_Title;
    }

    public String getF48_Time() {
        return F48_Time;
    }

    public void setF48_Time(String f48_Time) {
        F48_Time = f48_Time;
    }

    public String getF48_Message() {
        return F48_Message;
    }

    public void setF48_Message(String f48_Message) {
        F48_Message = f48_Message;
    }

    public String getF48_MessageClass() {
        return F48_MessageClass;
    }

    public void setF48_MessageClass(String f48_MessageClass) {
        F48_MessageClass = f48_MessageClass;
    }

    public boolean isF48_IsPush() {
        return F48_IsPush;
    }

    public void setF48_IsPush(boolean f48_IsPush) {
        F48_IsPush = f48_IsPush;
    }

    public boolean isF48_IsRead() {
        return F48_IsRead;
    }

    public void setF48_IsRead(boolean f48_IsRead) {
        F48_IsRead = f48_IsRead;
    }

    public String getF48_ReadTime() {
        return F48_ReadTime;
    }

    public void setF48_ReadTime(String f48_ReadTime) {
        F48_ReadTime = f48_ReadTime;
    }

    public String getF48_BillNo() {
        return F48_BillNo;
    }

    public void setF48_BillNo(String f48_BillNo) {
        F48_BillNo = f48_BillNo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getPrimaryKey() {
        return PrimaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        PrimaryKey = primaryKey;
    }
}
