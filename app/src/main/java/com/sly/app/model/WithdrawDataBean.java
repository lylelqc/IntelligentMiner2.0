package com.sly.app.model;

/**
 * Created by 01 on 2017/9/28.
 */
public class WithdrawDataBean {
    /**
     * Pay09_ID : 3
     * Pay09_PurseCode : 18390838781
     * Pay09_AccountName : 孙静芳
     * Pay09_AccountNo : 4657948524784513452
     * Pay09_Branch : 中国农业银行
     * Pay09_BankCode : 95599
     * Pay09_BankName : 中国农业银行
     * Pay09_Remark : null
     * Pay09_Oper : 18390838781
     * Pay09_Optime : 2017-09-28 11:00:27
     * Pay09_PurseCodeLen : 11
     * MemberCode : null
     * Token : null
     * Status : 1
     * PrimaryKey : 3
     */

    private int Pay09_ID;
    private String Pay09_PurseCode;
    private String Pay09_AccountName;
    private String Pay09_AccountNo;
    private String Pay09_Branch;
    private String Pay09_BankCode;
    private String Pay09_BankName;
    private Object Pay09_Remark;
    private String Pay09_Oper;
    private String Pay09_Optime;
    private int Pay09_PurseCodeLen;
    private Object MemberCode;
    private Object Token;
    private int Status;
    private int PrimaryKey;

    public int getPay09_ID() {
        return Pay09_ID;
    }

    public void setPay09_ID(int Pay09_ID) {
        this.Pay09_ID = Pay09_ID;
    }

    public String getPay09_PurseCode() {
        return Pay09_PurseCode;
    }

    public void setPay09_PurseCode(String Pay09_PurseCode) {
        this.Pay09_PurseCode = Pay09_PurseCode;
    }

    public String getPay09_AccountName() {
        return Pay09_AccountName;
    }

    public void setPay09_AccountName(String Pay09_AccountName) {
        this.Pay09_AccountName = Pay09_AccountName;
    }

    public String getPay09_AccountNo() {
        return Pay09_AccountNo;
    }

    public void setPay09_AccountNo(String Pay09_AccountNo) {
        this.Pay09_AccountNo = Pay09_AccountNo;
    }

    public String getPay09_Branch() {
        return Pay09_Branch;
    }

    public void setPay09_Branch(String Pay09_Branch) {
        this.Pay09_Branch = Pay09_Branch;
    }

    public String getPay09_BankCode() {
        return Pay09_BankCode;
    }

    public void setPay09_BankCode(String Pay09_BankCode) {
        this.Pay09_BankCode = Pay09_BankCode;
    }

    public String getPay09_BankName() {
        return Pay09_BankName;
    }

    public void setPay09_BankName(String Pay09_BankName) {
        this.Pay09_BankName = Pay09_BankName;
    }

    public Object getPay09_Remark() {
        return Pay09_Remark;
    }

    public void setPay09_Remark(Object Pay09_Remark) {
        this.Pay09_Remark = Pay09_Remark;
    }

    public String getPay09_Oper() {
        return Pay09_Oper;
    }

    public void setPay09_Oper(String Pay09_Oper) {
        this.Pay09_Oper = Pay09_Oper;
    }

    public String getPay09_Optime() {
        return Pay09_Optime;
    }

    public void setPay09_Optime(String Pay09_Optime) {
        this.Pay09_Optime = Pay09_Optime;
    }

    public int getPay09_PurseCodeLen() {
        return Pay09_PurseCodeLen;
    }

    public void setPay09_PurseCodeLen(int Pay09_PurseCodeLen) {
        this.Pay09_PurseCodeLen = Pay09_PurseCodeLen;
    }

    public Object getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(Object MemberCode) {
        this.MemberCode = MemberCode;
    }

    public Object getToken() {
        return Token;
    }

    public void setToken(Object Token) {
        this.Token = Token;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getPrimaryKey() {
        return PrimaryKey;
    }

    public void setPrimaryKey(int PrimaryKey) {
        this.PrimaryKey = PrimaryKey;
    }

    @Override
    public String toString() {
        return "WithdrawDataBean{" +
                "Pay09_ID=" + Pay09_ID +
                ", Pay09_PurseCode='" + Pay09_PurseCode + '\'' +
                ", Pay09_AccountName='" + Pay09_AccountName + '\'' +
                ", Pay09_AccountNo='" + Pay09_AccountNo + '\'' +
                ", Pay09_Branch='" + Pay09_Branch + '\'' +
                ", Pay09_BankCode='" + Pay09_BankCode + '\'' +
                ", Pay09_BankName='" + Pay09_BankName + '\'' +
                ", Pay09_Remark=" + Pay09_Remark +
                ", Pay09_Oper='" + Pay09_Oper + '\'' +
                ", Pay09_Optime='" + Pay09_Optime + '\'' +
                ", Pay09_PurseCodeLen=" + Pay09_PurseCodeLen +
                ", MemberCode=" + MemberCode +
                ", Token=" + Token +
                ", Status=" + Status +
                ", PrimaryKey=" + PrimaryKey +
                '}';
    }
}
