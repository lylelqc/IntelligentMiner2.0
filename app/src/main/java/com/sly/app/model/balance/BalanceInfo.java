package com.sly.app.model.balance;

/**
 * Created by 01 on 2017/9/22.
 */

public class BalanceInfo  {

    /**
     * Balance : 4530.0
     * Income : 6030.0
     * expenditure : 0.0
     * PurseType : Gift
     */

    private double Balance;
    private double Income;
    private double expenditure;
    private String PurseType;

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getIncome() {
        return Income;
    }

    public void setIncome(double Income) {
        this.Income = Income;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }

    public String getPurseType() {
        return PurseType;
    }

    public void setPurseType(String PurseType) {
        this.PurseType = PurseType;
    }
}
