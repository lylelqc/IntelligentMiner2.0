package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class MinerMasterChartBean implements Parcelable {
    private int ID;
    private String MineCode;
    private String MineName;
    private int AllMachineCount;
    private int AllNormalCount;
    private double AllRunRate;
    private int MachineCount;
    private int NormalCount;
    private double RunRate;

    public double getAllRunRate() {
        return AllRunRate;
    }

    public void setAllRunRate(double allRunRate) {
        AllRunRate = allRunRate;
    }

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double runRate) {
        RunRate = runRate;
    }

    @Override
    public String toString() {
        return "MinerMasterChartBean{" +
                "ID=" + ID +
                ", MineCode='" + MineCode + '\'' +
                ", MineName='" + MineName + '\'' +
                ", AllMachineCount=" + AllMachineCount +
                ", AllNormalCount=" + AllNormalCount +
                ", AllRunRate=" + AllRunRate +
                ", MachineCount=" + MachineCount +
                ", NormalCount=" + NormalCount +
                ", RunRate=" + RunRate +
                '}';
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public void setMineCode(String MineCode) {
        this.MineCode = MineCode;
    }
    public String getMineCode() {
        return MineCode;
    }

    public void setMineName(String MineName) {
        this.MineName = MineName;
    }
    public String getMineName() {
        return MineName;
    }

    public void setAllMachineCount(int AllMachineCount) {
        this.AllMachineCount = AllMachineCount;
    }
    public int getAllMachineCount() {
        return AllMachineCount;
    }

    public void setAllNormalCount(int AllNormalCount) {
        this.AllNormalCount = AllNormalCount;
    }
    public int getAllNormalCount() {
        return AllNormalCount;
    }


    public void setMachineCount(int MachineCount) {
        this.MachineCount = MachineCount;
    }
    public int getMachineCount() {
        return MachineCount;
    }

    public void setNormalCount(int NormalCount) {
        this.NormalCount = NormalCount;
    }
    public int getNormalCount() {
        return NormalCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.MineCode);
        dest.writeString(this.MineName);
        dest.writeInt(this.AllMachineCount);
        dest.writeInt(this.AllNormalCount);
        dest.writeDouble(this.AllRunRate);
        dest.writeInt(this.MachineCount);
        dest.writeInt(this.NormalCount);
        dest.writeDouble(this.RunRate);
    }

    public MinerMasterChartBean() {
    }

    protected MinerMasterChartBean(Parcel in) {
        this.ID = in.readInt();
        this.MineCode = in.readString();
        this.MineName = in.readString();
        this.AllMachineCount = in.readInt();
        this.AllNormalCount = in.readInt();
        this.AllRunRate = in.readDouble();
        this.MachineCount = in.readInt();
        this.NormalCount = in.readInt();
        this.RunRate = in.readDouble();
    }

    public static final Creator<MinerMasterChartBean> CREATOR = new Creator<MinerMasterChartBean>() {
        @Override
        public MinerMasterChartBean createFromParcel(Parcel source) {
            return new MinerMasterChartBean(source);
        }

        @Override
        public MinerMasterChartBean[] newArray(int size) {
            return new MinerMasterChartBean[size];
        }
    };
}
