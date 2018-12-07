package com.sly.app.model;

public class SlyRecordListBean {
    /**
     * Pay11_DataID : 6FE63A01-DF0D-4C23-89A1-ABC2E4278CF4
     * Pay11_PurseCode : 0D2FEFB4-C368-4AAD-B854-21F28D527D40_C01
     * Pay11_UserCode : 5356A9DF-4C4C-40FF-854C-004306297645
     * Pay11_CurrencyCode : C01
     * Pay11_SerialNumber : 2018080800000004
     * Pay11_Time : 2018-08-08 16:10:43
     * Pay11_BeforeBalance : 0
     * Pay11_Sum : 2.1
     * Pay11_AfterBalance : 2.1
     * Pay11_Note : ThisToNotes
     * Pay11_Remark : ThisNotes
     * Pay11_BusinessCode : 2018080800000004
     * Pay11_Oper : Sys
     * Pay11_Optime : 2018-08-08 16:10:43
     * Status : 1
     * PrimaryKey : 6FE63A01-DF0D-4C23-89A1-ABC2E4278CF4
     */

    /**
     * Pay11_DataID	string	数据ID
     Pay11_PurseCode	string	电子钱包编号
     Pay11_UserCode	string	用户编号
     Pay11_CurrencyCode	string	货币编号(分区)
     Pay11_SerialNumber	string	流水号
     Pay11_Time	datetime	发生时间
     Pay11_BeforeBalance	decimal	发生前结余
     Pay11_Sum	decimal	发生数额
     Pay11_AfterBalance	decimal	发生后结余
     Pay11_Note	string	说明
     Pay11_Remark	string	详情
     Pay11_BusinessCode	string	业务编号
     Pay11_Oper	string	操作员
     Pay11_Optime	datetime	操作时间**/

    private String Pay11_DataID;
    private String Pay11_PurseCode;
    private String Pay11_UserCode;
    private String Pay11_CurrencyCode;
    private String Pay11_SerialNumber;
    private String Pay11_Time;
    private String Pay11_BeforeBalance;
    private String Pay11_Sum;
    private String Pay11_AfterBalance;
    private String Pay11_Note;
    private String Pay11_Remark;
    private String Pay11_BusinessCode;
    private String Pay11_Oper;
    private String Pay11_Optime;
    private String Status;
    private String PrimaryKey;

    public String getPay11_DataID() {
        return Pay11_DataID;
    }

    public void setPay11_DataID(String Pay11_DataID) {
        this.Pay11_DataID = Pay11_DataID;
    }

    public String getPay11_PurseCode() {
        return Pay11_PurseCode;
    }

    public void setPay11_PurseCode(String Pay11_PurseCode) {
        this.Pay11_PurseCode = Pay11_PurseCode;
    }

    public String getPay11_UserCode() {
        return Pay11_UserCode;
    }

    public void setPay11_UserCode(String Pay11_UserCode) {
        this.Pay11_UserCode = Pay11_UserCode;
    }

    public String getPay11_CurrencyCode() {
        return Pay11_CurrencyCode;
    }

    public void setPay11_CurrencyCode(String Pay11_CurrencyCode) {
        this.Pay11_CurrencyCode = Pay11_CurrencyCode;
    }

    public String getPay11_SerialNumber() {
        return Pay11_SerialNumber;
    }

    public void setPay11_SerialNumber(String Pay11_SerialNumber) {
        this.Pay11_SerialNumber = Pay11_SerialNumber;
    }

    public String getPay11_Time() {
        return Pay11_Time;
    }

    public void setPay11_Time(String Pay11_Time) {
        this.Pay11_Time = Pay11_Time;
    }

    public String getPay11_BeforeBalance() {
        return Pay11_BeforeBalance;
    }

    public void setPay11_BeforeBalance(String Pay11_BeforeBalance) {
        this.Pay11_BeforeBalance = Pay11_BeforeBalance;
    }

    public String getPay11_Sum() {
        return Pay11_Sum;
    }

    public void setPay11_Sum(String Pay11_Sum) {
        this.Pay11_Sum = Pay11_Sum;
    }

    public String getPay11_AfterBalance() {
        return Pay11_AfterBalance;
    }

    public void setPay11_AfterBalance(String Pay11_AfterBalance) {
        this.Pay11_AfterBalance = Pay11_AfterBalance;
    }

    public String getPay11_Note() {
        return Pay11_Note;
    }

    public void setPay11_Note(String Pay11_Note) {
        this.Pay11_Note = Pay11_Note;
    }

    public String getPay11_Remark() {
        return Pay11_Remark;
    }

    public void setPay11_Remark(String Pay11_Remark) {
        this.Pay11_Remark = Pay11_Remark;
    }

    public String getPay11_BusinessCode() {
        return Pay11_BusinessCode;
    }

    public void setPay11_BusinessCode(String Pay11_BusinessCode) {
        this.Pay11_BusinessCode = Pay11_BusinessCode;
    }

    public String getPay11_Oper() {
        return Pay11_Oper;
    }

    public void setPay11_Oper(String Pay11_Oper) {
        this.Pay11_Oper = Pay11_Oper;
    }

    public String getPay11_Optime() {
        return Pay11_Optime;
    }

    public void setPay11_Optime(String Pay11_Optime) {
        this.Pay11_Optime = Pay11_Optime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getPrimaryKey() {
        return PrimaryKey;
    }

    public void setPrimaryKey(String PrimaryKey) {
        this.PrimaryKey = PrimaryKey;
    }
}
