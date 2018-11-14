package com.sly.app.model;

public class MinerMasterNoticeDetail {

    /**
     * Mine48_MessageID : 000332e8-1026-4d5f-9a2c-003e73b506c6
     * Mine48_MinePersonSysCode : 3416389f-de65-4b1a-aedd-9492182b5bc0
     * Mine48_Title : 有维修单需您尽快处理
     * Mine48_Time : 2018-09-10 11:08:30
     * Mine48_Message : 客户于2018/9/10 11:08:30催单,请您尽快处理该笔维修单
     * Mine48_MessageClass : 催单
     * Mine48_IsRead : false
     * Mine48_ReadTime : null
     * Mine48_BillNo : 5890ED36-FDF7-4F6D-BDFC-A2D7C3CA30C7
     */

    private String Mine48_MessageID;
    private String Mine48_MinePersonSysCode;
    private String Mine48_Title;
    private String Mine48_Time;
    private String Mine48_Message;
    private String Mine48_MessageClass;
    private boolean Mine48_IsRead;
    private Object Mine48_ReadTime;
    private String Mine48_BillNo;

    public String getMine48_MessageID() {
        return Mine48_MessageID;
    }

    public void setMine48_MessageID(String Mine48_MessageID) {
        this.Mine48_MessageID = Mine48_MessageID;
    }

    public String getMine48_MinePersonSysCode() {
        return Mine48_MinePersonSysCode;
    }

    public void setMine48_MinePersonSysCode(String Mine48_MinePersonSysCode) {
        this.Mine48_MinePersonSysCode = Mine48_MinePersonSysCode;
    }

    public String getMine48_Title() {
        return Mine48_Title;
    }

    public void setMine48_Title(String Mine48_Title) {
        this.Mine48_Title = Mine48_Title;
    }

    public String getMine48_Time() {
        return Mine48_Time;
    }

    public void setMine48_Time(String Mine48_Time) {
        this.Mine48_Time = Mine48_Time;
    }

    public String getMine48_Message() {
        return Mine48_Message;
    }

    public void setMine48_Message(String Mine48_Message) {
        this.Mine48_Message = Mine48_Message;
    }

    public String getMine48_MessageClass() {
        return Mine48_MessageClass;
    }

    public void setMine48_MessageClass(String Mine48_MessageClass) {
        this.Mine48_MessageClass = Mine48_MessageClass;
    }

    public boolean isMine48_IsRead() {
        return Mine48_IsRead;
    }

    public void setMine48_IsRead(boolean Mine48_IsRead) {
        this.Mine48_IsRead = Mine48_IsRead;
    }

    public Object getMine48_ReadTime() {
        return Mine48_ReadTime;
    }

    public void setMine48_ReadTime(Object Mine48_ReadTime) {
        this.Mine48_ReadTime = Mine48_ReadTime;
    }

    public String getMine48_BillNo() {
        return Mine48_BillNo;
    }

    public void setMine48_BillNo(String Mine48_BillNo) {
        this.Mine48_BillNo = Mine48_BillNo;
    }
}
