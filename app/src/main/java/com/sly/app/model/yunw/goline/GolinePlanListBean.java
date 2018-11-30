package com.sly.app.model.yunw.goline;

public class GolinePlanListBean {
    /**
     * \"Mine68_PlanID\":\"2018111614345612482604\",
     \"Mine68_MineCode\":\"Z000017\",
     \"Mine68_Owner\":\"\",
     \"Mine68_PlanCount\":0,
     \"Mine68_IsExecuted\":true,
     \"Mine68_Executor\":\"Sys\",
     \"Mine68_IsExecutedTime\":\"2018-11-16 14:37:02\",
     \"Mine68_Oper\":\"5b1b48cb-aa54-4f4f-81c1-b8c005f65882\",
     \"Mine68_Optime\":\"2018-11-16 14:34:56\",
     \"ScanCount\":380,
     \"OnlineStatus\":\"全部上线\"
     */

    private String Mine68_PlanID;
    private String Mine68_MineCode;
    private String Mine68_Owner;
    private int Mine68_PlanCount;
    private boolean Mine68_IsExecuted;
    private String Mine68_Executor;
    private String Mine68_IsExecutedTime;
    private String Mine68_Oper;
    private String Mine68_Optime;
    private int ScanCount;
    private String OnlineStatus;

    public String getMine68_PlanID() {
        return Mine68_PlanID;
    }

    public void setMine68_PlanID(String mine68_PlanID) {
        Mine68_PlanID = mine68_PlanID;
    }

    public String getMine68_MineCode() {
        return Mine68_MineCode;
    }

    public void setMine68_MineCode(String mine68_MineCode) {
        Mine68_MineCode = mine68_MineCode;
    }

    public String getMine68_Owner() {
        return Mine68_Owner;
    }

    public void setMine68_Owner(String mine68_Owner) {
        Mine68_Owner = mine68_Owner;
    }

    public int getMine68_PlanCount() {
        return Mine68_PlanCount;
    }

    public void setMine68_PlanCount(int mine68_PlanCount) {
        Mine68_PlanCount = mine68_PlanCount;
    }

    public boolean isMine68_IsExecuted() {
        return Mine68_IsExecuted;
    }

    public void setMine68_IsExecuted(boolean mine68_IsExecuted) {
        Mine68_IsExecuted = mine68_IsExecuted;
    }

    public String getMine68_Executor() {
        return Mine68_Executor;
    }

    public void setMine68_Executor(String mine68_Executor) {
        Mine68_Executor = mine68_Executor;
    }

    public String getMine68_IsExecutedTime() {
        return Mine68_IsExecutedTime;
    }

    public void setMine68_IsExecutedTime(String mine68_IsExecutedTime) {
        Mine68_IsExecutedTime = mine68_IsExecutedTime;
    }

    public String getMine68_Oper() {
        return Mine68_Oper;
    }

    public void setMine68_Oper(String mine68_Oper) {
        Mine68_Oper = mine68_Oper;
    }

    public String getMine68_Optime() {
        return Mine68_Optime;
    }

    public void setMine68_Optime(String mine68_Optime) {
        Mine68_Optime = mine68_Optime;
    }

    public int getScanCount() {
        return ScanCount;
    }

    public void setScanCount(int scanCount) {
        ScanCount = scanCount;
    }

    public String getOnlineStatus() {
        return OnlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        OnlineStatus = onlineStatus;
    }
}
