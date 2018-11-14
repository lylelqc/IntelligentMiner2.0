package com.sly.app.model.sly;

public class MinerNoticeDetail {

    /**
     "Mine34_MessageID\":\"1b6824de-8e9c-43b9-889e-7bbfeeeffada\",
     \"Mine34_MinerSysCode\":\"M0000077\",
     \"Mine34_Title\":\"审核\",
     \"Mine34_Time\":\"2018-10-29 16:55:48\",
     \"Mine34_Message\":\"矿工认证审核失败！\",
     \"Mine34_MessageClass\":\"04\",
     \"Mine34_IsPush\":true,
     \"Mine34_IsRead\":false,
     \"Mine34_ReadTime\":null,
     \"Mine34_BillNo\":null
     */

    private String Mine34_MessageID;
    private String Mine34_MinerSysCode;
    private String Mine34_Title;
    private String Mine34_Time;
    private String Mine34_Message;
    private String Mine34_MessageClass;
    private boolean Mine34_IsPush;
    private boolean Mine34_IsRead;
    private String Mine34_ReadTime;
    private String Mine34_BillNo;

    public String getMine34_MessageID() {
        return Mine34_MessageID;
    }

    public void setMine34_MessageID(String mine34_MessageID) {
        Mine34_MessageID = mine34_MessageID;
    }

    public String getMine34_MinerSysCode() {
        return Mine34_MinerSysCode;
    }

    public void setMine34_MinerSysCode(String mine34_MinerSysCode) {
        Mine34_MinerSysCode = mine34_MinerSysCode;
    }

    public String getMine34_Title() {
        return Mine34_Title;
    }

    public void setMine34_Title(String mine34_Title) {
        Mine34_Title = mine34_Title;
    }

    public String getMine34_Time() {
        return Mine34_Time;
    }

    public void setMine34_Time(String mine34_Time) {
        Mine34_Time = mine34_Time;
    }

    public String getMine34_Message() {
        return Mine34_Message;
    }

    public void setMine34_Message(String mine34_Message) {
        Mine34_Message = mine34_Message;
    }

    public String getMine34_MessageClass() {
        return Mine34_MessageClass;
    }

    public void setMine34_MessageClass(String mine34_MessageClass) {
        Mine34_MessageClass = mine34_MessageClass;
    }

    public boolean isMine34_IsPush() {
        return Mine34_IsPush;
    }

    public void setMine34_IsPush(boolean mine34_IsPush) {
        Mine34_IsPush = mine34_IsPush;
    }

    public boolean isMine34_IsRead() {
        return Mine34_IsRead;
    }

    public void setMine34_IsRead(boolean mine34_IsRead) {
        Mine34_IsRead = mine34_IsRead;
    }

    public String getMine34_ReadTime() {
        return Mine34_ReadTime;
    }

    public void setMine34_ReadTime(String mine34_ReadTime) {
        Mine34_ReadTime = mine34_ReadTime;
    }

    public String getMine34_BillNo() {
        return Mine34_BillNo;
    }

    public void setMine34_BillNo(String mine34_BillNo) {
        Mine34_BillNo = mine34_BillNo;
    }
}
