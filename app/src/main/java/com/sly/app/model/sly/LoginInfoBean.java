package com.sly.app.model.sly;

public class LoginInfoBean {

    //SysCode : 3D3825EC-43C0-464F-9E7F-0E06CA458743
    //CusCode :
    /**
     * User : 13719291926
     * FrSysCode : M0000058
     * FMasterCode : M0000058
     * PersonSysCode : ""
     * Key : B33A831B-C780-426B-BAD4-DCCE6A1FB714
     * Token : 3FFB8AF7-028A-4CF9-8480-5DD1EB2B73E9
     * Role: MinerMaster
     */

    private String User;
//    private String SysCode;
//    private String CusCode;
    private String FrSysCode;
    private String FMasterCode;
    private String PersonSysCode;
    private String ChildAccount;
    private String Key;
    private String Token;
    private String Role;

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

//    public String getSysCode() {
//        return SysCode;
//    }
//
//    public void setSysCode(String SysCode) {
//        this.SysCode = SysCode;
//    }
//
//    public String getCusCode() {
//        return CusCode;
//    }
//
//    public void setCusCode(String CusCode) {
//        this.CusCode = CusCode;
//    }


    public String getFrSysCode() {
        return FrSysCode;
    }

    public void setFrSysCode(String frSysCode) {
        FrSysCode = frSysCode;
    }

    public String getFMasterCode() {
        return FMasterCode;
    }

    public void setFMasterCode(String FMasterCode) {
        this.FMasterCode = FMasterCode;
    }

    public String getPersonSysCode() {
        return PersonSysCode;
    }

    public void setPersonSysCode(String personSysCode) {
        PersonSysCode = personSysCode;
    }

    public String getChildAccount() {
        return ChildAccount;
    }

    public void setChildAccount(String childAccount) {
        ChildAccount = childAccount;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
