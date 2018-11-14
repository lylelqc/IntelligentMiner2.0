package com.sly.app.model.sly;

import io.realm.RealmObject;

public class YwFullInfoBean extends RealmObject {

    /**
     *  F23_MinePersonSysCode	string	运维人员系统编号
     F23_MinePersonCode	string	工号(显示)
     F23_MineName	string	人员姓名
     F23_Notes	string	说明
     F23_PersonSysClassCode	string	人员分类系统编号
     F23_PersonStatusCode	string	人员状态编号
     F23_PersonClassCode	string	人员分类编号
     F23_BeginHour	int	工作时间(起)
     F23_EndHour	int	工作时间(止)
     F23_IsSetPermission	bool	是否已分配权限
     F23_PermissionUserSysCode	string	分配权限用户
     F23_MineCode	string	矿场编号
     F23_Oper	string	操作员
     F23_Optime	datetime	操作时间
     **/

    private String F23_MinePersonSysCode;
    private String F23_MinePersonCode;
    private String F23_MineName;
    private String F23_Notes;
    private String F23_PersonSysClassCode;
    private String F23_PersonStatusCode;
    private String F23_PersonClassCode;
    private String F23_BeginHour;
    private String F23_EndHour;
    private String F23_IsSetPermission;
    private String F23_PermissionUserSysCode;
    private String F23_MineCode;
    private String F23_Oper;
    private String F23_Optime;

    public String getF23_MinePersonSysCode() {
        return F23_MinePersonSysCode;
    }

    public void setF23_MinePersonSysCode(String f23_MinePersonSysCode) {
        F23_MinePersonSysCode = f23_MinePersonSysCode;
    }

    public String getF23_MinePersonCode() {
        return F23_MinePersonCode;
    }

    public void setF23_MinePersonCode(String f23_MinePersonCode) {
        F23_MinePersonCode = f23_MinePersonCode;
    }

    public String getF23_MineName() {
        return F23_MineName;
    }

    public void setF23_MineName(String f23_MineName) {
        F23_MineName = f23_MineName;
    }

    public String getF23_Notes() {
        return F23_Notes;
    }

    public void setF23_Notes(String f23_Notes) {
        F23_Notes = f23_Notes;
    }

    public String getF23_PersonSysClassCode() {
        return F23_PersonSysClassCode;
    }

    public void setF23_PersonSysClassCode(String f23_PersonSysClassCode) {
        F23_PersonSysClassCode = f23_PersonSysClassCode;
    }

    public String getF23_PersonStatusCode() {
        return F23_PersonStatusCode;
    }

    public void setF23_PersonStatusCode(String f23_PersonStatusCode) {
        F23_PersonStatusCode = f23_PersonStatusCode;
    }

    public String getF23_PersonClassCode() {
        return F23_PersonClassCode;
    }

    public void setF23_PersonClassCode(String f23_PersonClassCode) {
        F23_PersonClassCode = f23_PersonClassCode;
    }

    public String getF23_BeginHour() {
        return F23_BeginHour;
    }

    public void setF23_BeginHour(String f23_BeginHour) {
        F23_BeginHour = f23_BeginHour;
    }

    public String getF23_EndHour() {
        return F23_EndHour;
    }

    public void setF23_EndHour(String f23_EndHour) {
        F23_EndHour = f23_EndHour;
    }

    public String getF23_IsSetPermission() {
        return F23_IsSetPermission;
    }

    public void setF23_IsSetPermission(String f23_IsSetPermission) {
        F23_IsSetPermission = f23_IsSetPermission;
    }

    public String getF23_PermissionUserSysCode() {
        return F23_PermissionUserSysCode;
    }

    public void setF23_PermissionUserSysCode(String f23_PermissionUserSysCode) {
        F23_PermissionUserSysCode = f23_PermissionUserSysCode;
    }

    public String getF23_MineCode() {
        return F23_MineCode;
    }

    public void setF23_MineCode(String f23_MineCode) {
        F23_MineCode = f23_MineCode;
    }

    public String getF23_Oper() {
        return F23_Oper;
    }

    public void setF23_Oper(String f23_Oper) {
        F23_Oper = f23_Oper;
    }

    public String getF23_Optime() {
        return F23_Optime;
    }

    public void setF23_Optime(String f23_Optime) {
        F23_Optime = f23_Optime;
    }
}
