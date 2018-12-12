package com.sly.app.model.master;

public class MasterAllDataBean {

    /**
     *{\"MachineCount\":\"0\",
     \"MonthExpenses\":\"285.12\",
     \"MonthRunRate\":\"0.0000\",
     \"MonthPower\":\"0.0000\"}
     */

    private String MachineCount;
    private String MonthExpenses;
    private String MonthRunRate;
    private String MonthPower;

    public String getMachineCount() {
        return MachineCount;
    }

    public void setMachineCount(String machineCount) {
        MachineCount = machineCount;
    }

    public String getMonthExpenses() {
        return MonthExpenses;
    }

    public void setMonthExpenses(String monthExpenses) {
        MonthExpenses = monthExpenses;
    }

    public String getMonthRunRate() {
        return MonthRunRate;
    }

    public void setMonthRunRate(String monthRunRate) {
        MonthRunRate = monthRunRate;
    }

    public String getMonthPower() {
        return MonthPower;
    }

    public void setMonthPower(String monthPower) {
        MonthPower = monthPower;
    }
}
