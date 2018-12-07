package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class RepairBean implements Parcelable {
    /**
     * RepairDate	datetime	处理时间
     * ErrorName	string	故障描述
     * Cost	decimal	维修费用
     * ProcessHours	string	处理时长
     * ProcessResult	string	处理状态
     */
    private String RepairDate;
    private String ErrorName;
    private double Cost;
    private double ProcessHours;
    private String ProcessResult;
    private String TimeShow;

    public RepairBean(String timeShow) {

        TimeShow = timeShow;
    }

    @Override
    public String toString() {
        return "RepairBean{" +
                "RepairDate='" + RepairDate + '\'' +
                ", ErrorName='" + ErrorName + '\'' +
                ", Cost=" + Cost +
                ", ProcessHours='" + ProcessHours + '\'' +
                ", ProcessResult='" + ProcessResult + '\'' +
                '}';
    }

    public String getRepairDate() {
        return RepairDate;
    }

    public void setRepairDate(String repairDate) {
        RepairDate = repairDate;
    }

    public String getErrorName() {
        return ErrorName;
    }

    public void setErrorName(String errorName) {
        ErrorName = errorName;
    }

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    public double getProcessHours() {
        return ProcessHours;
    }

    public void setProcessHours(double processHours) {
        ProcessHours = processHours;
    }

    public String getProcessResult() {
        return ProcessResult;
    }

    public void setProcessResult(String processResult) {
        ProcessResult = processResult;
    }

    public String getTimeShow() {
        return TimeShow;
    }

    public void setTimeShow(String timeShow) {
        TimeShow = timeShow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.RepairDate);
        dest.writeString(this.ErrorName);
        dest.writeValue(this.Cost);
        dest.writeValue(this.ProcessHours);
        dest.writeString(this.ProcessResult);
        dest.writeString(this.TimeShow);
    }

    public RepairBean() {
    }

    protected RepairBean(Parcel in) {
        this.RepairDate = in.readString();
        this.ErrorName = in.readString();
        this.Cost = (Double) in.readValue(Double.class.getClassLoader());
        this.ProcessHours = (Double) in.readValue(Double.class.getClassLoader());
        this.ProcessResult = in.readString();
        this.TimeShow = in.readString();
    }

    public static final Creator<RepairBean> CREATOR = new Creator<RepairBean>() {
        @Override
        public RepairBean createFromParcel(Parcel source) {
            return new RepairBean(source);
        }

        @Override
        public RepairBean[] newArray(int size) {
            return new RepairBean[size];
        }
    };
}
