package com.sly.app.model;

/**
 * Created by 01 on 2017/9/27.
 */
public class WithdrawDetailBean {

    private String Name;
    private String Money;
    private String SuccessTime;
    private String Bank;
    private String MemberCode;

    @Override
    public String toString() {
        return "WithdrawDetailBean{" +
                "Name='" + Name + '\'' +
                ", Money='" + Money + '\'' +
                ", SuccessTime='" + SuccessTime + '\'' +
                ", Bank='" + Bank + '\'' +
                ", MemberCode='" + MemberCode + '\'' +
                '}';
    }
    public WithdrawDetailBean(){}
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String memberCode) {
        MemberCode = memberCode;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getSuccessTime() {
        return SuccessTime;
    }

    public void setSuccessTime(String successTime) {
        SuccessTime = successTime;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }
}
