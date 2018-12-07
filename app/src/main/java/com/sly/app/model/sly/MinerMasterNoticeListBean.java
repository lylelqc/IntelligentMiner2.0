package com.sly.app.model.sly;

public class MinerMasterNoticeListBean {
    /**
     * rowid : 1
     * Mine48_MessageID : be3637be-4fdd-47d7-a193-22542f7c8a99  消息编号
     * Mine48_MinePersonSysCode : 3416389f-de65-4b1a-aedd-9492182b5bc0  人员系统编号
     * Mine48_Title : 有维修单需您尽快处理   标题
     * Mine48_Time : 2018-09-11 17:41:50  发送时间
     * Mine48_Message : 客户于2018/9/11 17:41:50催单,请您尽快处理该笔维修单   消息内容
     * Mine48_MessageClass : 催单  消息分类
     * Mine48_IsRead : false 是否已读
     * Mine48_ReadTime : null  	阅读时间
     * Mine48_BillNo : 2018091100003251  	单据号
     */

    private int rowid;
    private String Mine48_MessageID;
    private String Mine48_MinePersonSysCode;
    private String Mine48_Title;
    private String Mine48_Time;
    private String Mine48_Message;
    private String Mine48_MessageClass;
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

    public String getMine48_ReadTime() {
        return Mine48_ReadTime;
    }

    public void setMine48_ReadTime(String Mine48_ReadTime) {
        this.Mine48_ReadTime = Mine48_ReadTime;
    }

    public String getMine48_BillNo() {
        return Mine48_BillNo;
    }

    public void setMine48_BillNo(String Mine48_BillNo) {
        this.Mine48_BillNo = Mine48_BillNo;
    }
}
