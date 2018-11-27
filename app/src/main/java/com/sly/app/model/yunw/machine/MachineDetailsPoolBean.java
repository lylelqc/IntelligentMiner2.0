package com.sly.app.model.yunw.machine;

public class MachineDetailsPoolBean {

    /**
     * \"ID\":\"581914E4-AA85-46DB-BE5E-DC25A6884C01\",
     \"URL\":\"vip-hhht.f2pool.com:3333\",
     \"Worker\":\"f1000s9shz1801.10x18x1x125\",
     \"Passwords\":\"123\",
     \"PoolNo\":1
     */

    private String ID;
    private String URL;
    private String Worker;
    private String Passwords;
    private String PoolNo;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getWorker() {
        return Worker;
    }

    public void setWorker(String worker) {
        Worker = worker;
    }

    public String getPasswords() {
        return Passwords;
    }

    public void setPasswords(String passwords) {
        Passwords = passwords;
    }

    public String getPoolNo() {
        return PoolNo;
    }

    public void setPoolNo(String poolNo) {
        PoolNo = poolNo;
    }
}
