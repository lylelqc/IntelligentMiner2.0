package com.sly.app.model.miner;

public class MinerBillDateBean {

    /**
     * \"BeginDate\":\"2018-11-25 00:00:00\",
     * \"EndDate\":\"2018-11-29 00:00:00\",
     * \"DataID\":\"1\"
     */
    private String BeginDate;
    private String EndDate;
    private String DataID;

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getDataID() {
        return DataID;
    }

    public void setDataID(String dataID) {
        DataID = dataID;
    }
}
