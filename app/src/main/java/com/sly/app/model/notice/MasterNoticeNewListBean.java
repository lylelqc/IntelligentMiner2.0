package com.sly.app.model.notice;

public class MasterNoticeNewListBean {

    /**
     * \"rowid\":1,
     \"Mine48_MessageID\":\"AAEB4E8D-E448-4EB1-976E-300A791B7AAF\",
     \"Mine48_MinePersonSysCode\":\"23a0f149-78e4-4b37-8d76-412435f582d9\",
     \"Mine48_Title\":\"矿场区域(A)可能断电或者服务器宕机\",
     \"Mine48_Time\":\"2018-12-02 17:15:51\",
     \"Mine48_Message\":\"矿场区域(A)可能断电或者服务器宕机\",
     \"Mine48_MessageClass\":\"01\",
     \"Mine48_IsPush\":false,
     \"Mine48_IsRead\":false,
     \"Mine48_ReadTime\":null,
     \"Mine48_BillNo\":\"Z000017\"
     */

    private int rowid;
    private String Mine48_MessageID;
    private String Mine48_MinePersonSysCode;
    private String Mine48_Title;
    private String Mine48_Time;
    private String Mine48_Message;
    private String Mine48_MessageClass;
    private boolean Mine48_IsPush;
    private boolean Mine48_IsRead;
    private String Mine48_ReadTime;
    private String Mine48_BillNo;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getMine48_MessageID() {
        return Mine48_MessageID;
    }

    public void setMine48_MessageID(String mine48_MessageID) {
        Mine48_MessageID = mine48_MessageID;
    }

    public String getMine48_MinePersonSysCode() {
        return Mine48_MinePersonSysCode;
    }

    public void setMine48_MinePersonSysCode(String mine48_MinePersonSysCode) {
        Mine48_MinePersonSysCode = mine48_MinePersonSysCode;
    }

    public String getMine48_Title() {
        return Mine48_Title;
    }

    public void setMine48_Title(String mine48_Title) {
        Mine48_Title = mine48_Title;
    }

    public String getMine48_Time() {
        return Mine48_Time;
    }

    public void setMine48_Time(String mine48_Time) {
        Mine48_Time = mine48_Time;
    }

    public String getMine48_Message() {
        return Mine48_Message;
    }

    public void setMine48_Message(String mine48_Message) {
        Mine48_Message = mine48_Message;
    }

    public String getMine48_MessageClass() {
        return Mine48_MessageClass;
    }

    public void setMine48_MessageClass(String mine48_MessageClass) {
        Mine48_MessageClass = mine48_MessageClass;
    }

    public boolean isMine48_IsPush() {
        return Mine48_IsPush;
    }

    public void setMine48_IsPush(boolean mine48_IsPush) {
        Mine48_IsPush = mine48_IsPush;
    }

    public boolean isMine48_IsRead() {
        return Mine48_IsRead;
    }

    public void setMine48_IsRead(boolean mine48_IsRead) {
        Mine48_IsRead = mine48_IsRead;
    }

    public String getMine48_ReadTime() {
        return Mine48_ReadTime;
    }

    public void setMine48_ReadTime(String mine48_ReadTime) {
        Mine48_ReadTime = mine48_ReadTime;
    }

    public String getMine48_BillNo() {
        return Mine48_BillNo;
    }

    public void setMine48_BillNo(String mine48_BillNo) {
        Mine48_BillNo = mine48_BillNo;
    }
}
