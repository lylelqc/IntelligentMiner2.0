package com.sly.app.model.sly;

public class SlyRecordBean {

    /**
     * RowNo : 1
     * Time : 2018-08-28 13:48:09
     * Sum : 100
     * Status : 等待审核
     * PIC : #
     */

    private int RowNo;
    private String Time;
    private int Sum;
    private String Status;
    private String PIC;

    public int getRowNo() {
        return RowNo;
    }

    public void setRowNo(int RowNo) {
        this.RowNo = RowNo;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getSum() {
        return Sum;
    }

    public void setSum(int Sum) {
        this.Sum = Sum;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getPIC() {
        return PIC;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }
}
