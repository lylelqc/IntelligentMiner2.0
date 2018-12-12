package com.sly.app.model.master;

public class MasterAllFreeBean {

    /**
     * \"Day\":1,
     * \"Date\":\"2018-10-01 00:00:00\",
     \"DateRepairSum\":0.00,
     \"DatePowerSum\":0.00,
     \"RepairSum\":0.00,
     \"PowerSum\":0.00
     */

    private int Day;
    private String Date;
    private double DateRepairSum;
    private double DatePowerSum;
    private double RepairSum;
    private double PowerSum;

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getDateRepairSum() {
        return DateRepairSum;
    }

    public void setDateRepairSum(double dateRepairSum) {
        DateRepairSum = dateRepairSum;
    }

    public double getDatePowerSum() {
        return DatePowerSum;
    }

    public void setDatePowerSum(double datePowerSum) {
        DatePowerSum = datePowerSum;
    }

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public double getPowerSum() {
        return PowerSum;
    }

    public void setPowerSum(double powerSum) {
        PowerSum = powerSum;
    }
}
