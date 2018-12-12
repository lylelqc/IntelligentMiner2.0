package com.sly.app.model.master;

public class MasterSpareListBean {

    /**
     * "rowid\":1,
     \"UseCount\":1,
     \"PartID\":\"9621f703-81ea-4287-91d0-8de3f430f62f\",
     \"PartName\":\"算力板\",
     \"Times\":\"2018-11-29\",
     \"Worker\":null,
     \"Model\":null
     */
    private int rowid;
    private int UseCount;
    private String PartID;
    private String PartName;
    private String Times;
    private String Worker;
    private String Model;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public int getUseCount() {
        return UseCount;
    }

    public void setUseCount(int useCount) {
        UseCount = useCount;
    }

    public String getPartID() {
        return PartID;
    }

    public void setPartID(String partID) {
        PartID = partID;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public String getWorker() {
        return Worker;
    }

    public void setWorker(String worker) {
        Worker = worker;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }
}
