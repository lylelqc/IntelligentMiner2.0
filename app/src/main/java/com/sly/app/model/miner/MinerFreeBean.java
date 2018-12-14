package com.sly.app.model.miner;

public class MinerFreeBean {

    /**
     * "DataID":"1",
     * "RepairSum":265.00,
     * "PowerFeeSum":12500.00
     */
    private String DataID;
    private double RepairSum;
    private double PowerFeeSum;

    public String getDataID() {
        return DataID;
    }

    public void setDataID(String dataID) {
        DataID = dataID;
    }

    public double getRepairSum() {
        return RepairSum;
    }

    public void setRepairSum(double repairSum) {
        RepairSum = repairSum;
    }

    public double getPowerFeeSum() {
        return PowerFeeSum;
    }

    public void setPowerFeeSum(double powerFeeSum) {
        PowerFeeSum = powerFeeSum;
    }
}
