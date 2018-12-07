package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class AllDMinerTableBean implements Parcelable {

    /**
     MachineCount	int	设备总数量
     RunMachineCount	int	运行设备数量
     RunRate	decimal	实时运行率
     usepower	decimal	当日耗电量
     usecalc	decimal	实时算力
     */

    private int MachineCount;
    private int RunMachineCount;
    private double RunRate;
    private double usepower;
    private double usecalc;

    @Override
    public String toString() {
        return "AllDMinerTableBean{" +
                "MachineCount=" + MachineCount +
                ", RunMachineCount=" + RunMachineCount +
                ", RunRate=" + RunRate +
                ", usepower=" + usepower +
                ", usecalc=" + usecalc +
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

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double runRate) {
        RunRate = runRate;
    }

    public double getUsepower() {
        return usepower;
    }

    public void setUsepower(double usepower) {
        this.usepower = usepower;
    }

    public double getUsecalc() {
        return usecalc;
    }

    public void setUsecalc(double usecalc) {
        this.usecalc = usecalc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.MachineCount);
        dest.writeInt(this.RunMachineCount);
        dest.writeDouble(this.RunRate);
        dest.writeDouble(this.usepower);
        dest.writeDouble(this.usecalc);
    }

    public AllDMinerTableBean() {
    }

    protected AllDMinerTableBean(Parcel in) {
        this.MachineCount = in.readInt();
        this.RunMachineCount = in.readInt();
        this.RunRate = in.readDouble();
        this.usepower = in.readDouble();
        this.usecalc = in.readDouble();
    }

    public static final Creator<AllDMinerTableBean> CREATOR = new Creator<AllDMinerTableBean>() {
        @Override
        public AllDMinerTableBean createFromParcel(Parcel source) {
            return new AllDMinerTableBean(source);
        }

        @Override
        public AllDMinerTableBean[] newArray(int size) {
            return new AllDMinerTableBean[size];
        }
    };
}
