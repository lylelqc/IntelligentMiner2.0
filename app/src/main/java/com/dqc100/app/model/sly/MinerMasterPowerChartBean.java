package com.dqc100.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class MinerMasterPowerChartBean implements Parcelable {
    /**
     * ID : 1
     * MineCode : 128EF1F2-846F-4D7B-A401-FAE78D83C285
     * MineName : 矿场1号
     * WeekPower : 0
     * MonthPower : 0
     * Date1 : 2018-08-27 00:00:00
     * Date1Power : 0
     * Date2 : 2018-08-26 00:00:00
     * Date2Power : 0
     * Date3 : 2018-08-25 00:00:00
     * Date3Power : 0
     * Date4 : 2018-08-24 00:00:00
     * Date4Power : 0
     * Date5 : 2018-08-23 00:00:00
     * Date5Power : 0
     * Date6 : 2018-08-22 00:00:00
     * Date6Power : 0
     * Date7 : 2018-08-21 00:00:00
     * Date7Power : 0
     */

    private int ID;
    private String MineCode;
    private String MineName;
    private double WeekPower;
    private double MonthPower;
    private String Date1;
    private double Date1Power;
    private String Date2;
    private double Date2Power;
    private String Date3;
    private double Date3Power;
    private String Date4;
    private double Date4Power;
    private String Date5;
    private double Date5Power;
    private String Date6;
    private double Date6Power;
    private String Date7;
    private double Date7Power;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMineCode() {
        return MineCode;
    }

    public void setMineCode(String mineCode) {
        MineCode = mineCode;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String mineName) {
        MineName = mineName;
    }

    public double getWeekPower() {
        return WeekPower;
    }

    public void setWeekPower(double weekPower) {
        WeekPower = weekPower;
    }

    public double getMonthPower() {
        return MonthPower;
    }

    public void setMonthPower(double monthPower) {
        MonthPower = monthPower;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public double getDate1Power() {
        return Date1Power;
    }

    public void setDate1Power(double date1Power) {
        Date1Power = date1Power;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    public double getDate2Power() {
        return Date2Power;
    }

    public void setDate2Power(double date2Power) {
        Date2Power = date2Power;
    }

    public String getDate3() {
        return Date3;
    }

    public void setDate3(String date3) {
        Date3 = date3;
    }

    public double getDate3Power() {
        return Date3Power;
    }

    public void setDate3Power(double date3Power) {
        Date3Power = date3Power;
    }

    public String getDate4() {
        return Date4;
    }

    public void setDate4(String date4) {
        Date4 = date4;
    }

    public double getDate4Power() {
        return Date4Power;
    }

    public void setDate4Power(double date4Power) {
        Date4Power = date4Power;
    }

    public String getDate5() {
        return Date5;
    }

    public void setDate5(String date5) {
        Date5 = date5;
    }

    public double getDate5Power() {
        return Date5Power;
    }

    public void setDate5Power(double date5Power) {
        Date5Power = date5Power;
    }

    public String getDate6() {
        return Date6;
    }

    public void setDate6(String date6) {
        Date6 = date6;
    }

    public double getDate6Power() {
        return Date6Power;
    }

    public void setDate6Power(double date6Power) {
        Date6Power = date6Power;
    }

    public String getDate7() {
        return Date7;
    }

    public void setDate7(String date7) {
        Date7 = date7;
    }

    public double getDate7Power() {
        return Date7Power;
    }

    public void setDate7Power(double date7Power) {
        Date7Power = date7Power;
    }

    @Override
    public String toString() {
        return "MinerMasterPowerChartBean{" +
                "ID=" + ID +
                ", MineCode='" + MineCode + '\'' +
                ", MineName='" + MineName + '\'' +
                ", WeekPower=" + WeekPower +
                ", MonthPower=" + MonthPower +
                ", Date1='" + Date1 + '\'' +
                ", Date1Power=" + Date1Power +
                ", Date2='" + Date2 + '\'' +
                ", Date2Power=" + Date2Power +
                ", Date3='" + Date3 + '\'' +
                ", Date3Power=" + Date3Power +
                ", Date4='" + Date4 + '\'' +
                ", Date4Power=" + Date4Power +
                ", Date5='" + Date5 + '\'' +
                ", Date5Power=" + Date5Power +
                ", Date6='" + Date6 + '\'' +
                ", Date6Power=" + Date6Power +
                ", Date7='" + Date7 + '\'' +
                ", Date7Power=" + Date7Power +
                '}';
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
        dest.writeDouble(this.WeekPower);
        dest.writeDouble(this.MonthPower);
        dest.writeString(this.Date1);
        dest.writeDouble(this.Date1Power);
        dest.writeString(this.Date2);
        dest.writeDouble(this.Date2Power);
        dest.writeString(this.Date3);
        dest.writeDouble(this.Date3Power);
        dest.writeString(this.Date4);
        dest.writeDouble(this.Date4Power);
        dest.writeString(this.Date5);
        dest.writeDouble(this.Date5Power);
        dest.writeString(this.Date6);
        dest.writeDouble(this.Date6Power);
        dest.writeString(this.Date7);
        dest.writeDouble(this.Date7Power);
    }

    public MinerMasterPowerChartBean() {
    }

    protected MinerMasterPowerChartBean(Parcel in) {
        this.ID = in.readInt();
        this.MineCode = in.readString();
        this.MineName = in.readString();
        this.WeekPower = in.readDouble();
        this.MonthPower = in.readDouble();
        this.Date1 = in.readString();
        this.Date1Power = in.readDouble();
        this.Date2 = in.readString();
        this.Date2Power = in.readDouble();
        this.Date3 = in.readString();
        this.Date3Power = in.readDouble();
        this.Date4 = in.readString();
        this.Date4Power = in.readDouble();
        this.Date5 = in.readString();
        this.Date5Power = in.readDouble();
        this.Date6 = in.readString();
        this.Date6Power = in.readDouble();
        this.Date7 = in.readString();
        this.Date7Power = in.readDouble();
    }

    public static final Parcelable.Creator<MinerMasterPowerChartBean> CREATOR = new Parcelable.Creator<MinerMasterPowerChartBean>() {
        @Override
        public MinerMasterPowerChartBean createFromParcel(Parcel source) {
            return new MinerMasterPowerChartBean(source);
        }

        @Override
        public MinerMasterPowerChartBean[] newArray(int size) {
            return new MinerMasterPowerChartBean[size];
        }
    };
}
