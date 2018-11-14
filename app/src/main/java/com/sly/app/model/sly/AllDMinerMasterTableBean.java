package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class AllDMinerMasterTableBean implements Parcelable {

    /**
     MachineCount	int	设备总数量
     RunMachineCount	int	运行设备数量
     IncomeSum	decimal	累计收益
     usepower	decimal	今日耗电量
     RunRate	decimal	运行率
     */

    private int MachineCount;
    private int RunMachineCount;
    private double usepower;
    private double IncomeSum;
    private double RunRate;

    @Override
    public String toString() {
        return "AllDMinerMasterTableBean{" +
                "MachineCount=" + MachineCount +
                ", RunMachineCount=" + RunMachineCount +
                ", usepower=" + usepower +
                ", IncomeSum=" + IncomeSum +
                ", RunRate=" + RunRate +
                '}';
    }

    public int getMachineCount() {
        return MachineCount;
    }

    public void setMachineCount(int machineCount) {
        MachineCount = machineCount;
    }

    public int getRunMachineCount() {
        return RunMachineCount;
    }

    public void setRunMachineCount(int runMachineCount) {
        RunMachineCount = runMachineCount;
    }

    public double getUsepower() {
        return usepower;
    }

    public void setUsepower(double usepower) {
        this.usepower = usepower;
    }

    public double getIncomeSum() {
        return IncomeSum;
    }

    public void setIncomeSum(double incomeSum) {
        IncomeSum = incomeSum;
    }

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double runRate) {
        RunRate = runRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.MachineCount);
        dest.writeInt(this.RunMachineCount);
        dest.writeDouble(this.usepower);
        dest.writeDouble(this.IncomeSum);
        dest.writeDouble(this.RunRate);
    }

    public AllDMinerMasterTableBean() {
    }

    protected AllDMinerMasterTableBean(Parcel in) {
        this.MachineCount = in.readInt();
        this.RunMachineCount = in.readInt();
        this.usepower = in.readDouble();
        this.IncomeSum = in.readDouble();
        this.RunRate = in.readDouble();
    }

    public static final Parcelable.Creator<AllDMinerMasterTableBean> CREATOR = new Parcelable.Creator<AllDMinerMasterTableBean>() {
        @Override
        public AllDMinerMasterTableBean createFromParcel(Parcel source) {
            return new AllDMinerMasterTableBean(source);
        }

        @Override
        public AllDMinerMasterTableBean[] newArray(int size) {
            return new AllDMinerMasterTableBean[size];
        }
    };
}
