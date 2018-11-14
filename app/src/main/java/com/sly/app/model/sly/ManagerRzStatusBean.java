package com.sly.app.model.sly;

public class ManagerRzStatusBean {
    /**
     * Mine58_Name : ?
     * Mine60_AuditStatusName : 审核通过
     */

    private String Mine58_Name;
    private String Mine60_AuditStatusName;
    private String Mine72_Name;
    private String Mine72_AuditStatusCode;

    public String getMine72_Name() {
        return Mine72_Name;
    }

    public void setMine72_Name(String mine72_Name) {
        Mine72_Name = mine72_Name;
    }

    public String getMine72_AuditStatusCode() {
        return Mine72_AuditStatusCode;
    }

    public void setMine72_AuditStatusCode(String mine72_AuditStatusCode) {
        Mine72_AuditStatusCode = mine72_AuditStatusCode;
    }

    public String getMine58_Name() {
        return Mine58_Name;
    }

    public void setMine58_Name(String Mine58_Name) {
        this.Mine58_Name = Mine58_Name;
    }

    public String getMine60_AuditStatusName() {
        return Mine60_AuditStatusName;
    }

    public void setMine60_AuditStatusName(String Mine60_AuditStatusName) {
        this.Mine60_AuditStatusName = Mine60_AuditStatusName;
    }
}
