package com.sly.app.model.master;

public class MasterAccountPermissionBean {

    private boolean IsCancel;
    private boolean UsingFee;
    private boolean UsingPower;
    private boolean UsingRate;
    private boolean UsingPart;

    public boolean isCancel() {
        return IsCancel;
    }

    public void setCancel(boolean cancel) {
        IsCancel = cancel;
    }

    public boolean isUsingFee() {
        return UsingFee;
    }

    public void setUsingFee(boolean usingFee) {
        UsingFee = usingFee;
    }

    public boolean isUsingPower() {
        return UsingPower;
    }

    public void setUsingPower(boolean usingPower) {
        UsingPower = usingPower;
    }

    public boolean isUsingRate() {
        return UsingRate;
    }

    public void setUsingRate(boolean usingRate) {
        UsingRate = usingRate;
    }

    public boolean isUsingPart() {
        return UsingPart;
    }

    public void setUsingPart(boolean usingPart) {
        UsingPart = usingPart;
    }
}
