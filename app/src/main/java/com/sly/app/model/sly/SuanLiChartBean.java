package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class SuanLiChartBean implements Parcelable {
    private int ID;
    private String MineCode;
    private String MineName;
    private double WeekCalc;
    private double MonthCalc;
    private double Hour24Calc;
    private String Date1;
    private double Date1Calc;
    private String Date2;
    private double Date2Calc;
    private String Date3;
    private double Date3Calc;
    private String Date4;
    private double Date4Calc;
    private String Date5;
    private double Date5Calc;
    private String Date6;
    private double Date6Calc;
    private String Date7;
    private double Date7Calc;

    @Override
    public String toString() {
        return "SuanLiChartBean{" +
                "ID=" + ID +
                ", MineCode='" + MineCode + '\'' +
                ", MineName='" + MineName + '\'' +
                ", WeekCalc=" + WeekCalc +
                ", MonthCalc=" + MonthCalc +
                ", Hour24Calc=" + Hour24Calc +
                ", Date1='" + Date1 + '\'' +
                ", Date1Calc=" + Date1Calc +
                ", Date2='" + Date2 + '\'' +
                ", Date2Calc=" + Date2Calc +
                ", Date3='" + Date3 + '\'' +
                ", Date3Calc=" + Date3Calc +
                ", Date4='" + Date4 + '\'' +
                ", Date4Calc=" + Date4Calc +
                ", Date5='" + Date5 + '\'' +
                ", Date5Calc=" + Date5Calc +
                ", Date6='" + Date6 + '\'' +
                ", Date6Calc=" + Date6Calc +
                ", Date7='" + Date7 + '\'' +
                ", Date7Calc=" + Date7Calc +
                '}';
    }

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

    public double getWeekCalc() {
        return WeekCalc;
    }

    public void setWeekCalc(double weekCalc) {
        WeekCalc = weekCalc;
    }

    public double getMonthCalc() {
        return MonthCalc;
    }

    public void setMonthCalc(double monthCalc) {
        MonthCalc = monthCalc;
    }

    public double getHour24Calc() {
        return Hour24Calc;
    }

    public void setHour24Calc(double hour24Calc) {
        Hour24Calc = hour24Calc;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public double getDate1Calc() {
        return Date1Calc;
    }

    public void setDate1Calc(double date1Calc) {
        Date1Calc = date1Calc;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    public double getDate2Calc() {
        return Date2Calc;
    }

    public void setDate2Calc(double date2Calc) {
        Date2Calc = date2Calc;
    }

    public String getDate3() {
        return Date3;
    }

    public void setDate3(String date3) {
        Date3 = date3;
    }

    public double getDate3Calc() {
        return Date3Calc;
    }

    public void setDate3Calc(double date3Calc) {
        Date3Calc = date3Calc;
    }

    public String getDate4() {
        return Date4;
    }

    public void setDate4(String date4) {
        Date4 = date4;
    }

    public double getDate4Calc() {
        return Date4Calc;
    }

    public void setDate4Calc(double date4Calc) {
        Date4Calc = date4Calc;
    }

    public String getDate5() {
        return Date5;
    }

    public void setDate5(String date5) {
        Date5 = date5;
    }

    public double getDate5Calc() {
        return Date5Calc;
    }

    public void setDate5Calc(double date5Calc) {
        Date5Calc = date5Calc;
    }

    public String getDate6() {
        return Date6;
    }

    public void setDate6(String date6) {
        Date6 = date6;
    }

    public double getDate6Calc() {
        return Date6Calc;
    }

    public void setDate6Calc(double date6Calc) {
        Date6Calc = date6Calc;
    }

    public String getDate7() {
        return Date7;
    }

    public void setDate7(String date7) {
        Date7 = date7;
    }

    public double getDate7Calc() {
        return Date7Calc;
    }

    public void setDate7Calc(double date7Calc) {
        Date7Calc = date7Calc;
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
        dest.writeDouble(this.WeekCalc);
        dest.writeDouble(this.MonthCalc);
        dest.writeDouble(this.Hour24Calc);
        dest.writeString(this.Date1);
        dest.writeDouble(this.Date1Calc);
        dest.writeString(this.Date2);
        dest.writeDouble(this.Date2Calc);
        dest.writeString(this.Date3);
        dest.writeDouble(this.Date3Calc);
        dest.writeString(this.Date4);
        dest.writeDouble(this.Date4Calc);
        dest.writeString(this.Date5);
        dest.writeDouble(this.Date5Calc);
        dest.writeString(this.Date6);
        dest.writeDouble(this.Date6Calc);
        dest.writeString(this.Date7);
        dest.writeDouble(this.Date7Calc);
    }

    public SuanLiChartBean() {
    }

    protected SuanLiChartBean(Parcel in) {
        this.ID = in.readInt();
        this.MineCode = in.readString();
        this.MineName = in.readString();
        this.WeekCalc = in.readDouble();
        this.MonthCalc = in.readDouble();
        this.Hour24Calc = in.readDouble();
        this.Date1 = in.readString();
        this.Date1Calc = in.readDouble();
        this.Date2 = in.readString();
        this.Date2Calc = in.readDouble();
        this.Date3 = in.readString();
        this.Date3Calc = in.readDouble();
        this.Date4 = in.readString();
        this.Date4Calc = in.readDouble();
        this.Date5 = in.readString();
        this.Date5Calc = in.readDouble();
        this.Date6 = in.readString();
        this.Date6Calc = in.readDouble();
        this.Date7 = in.readString();
        this.Date7Calc = in.readDouble();
    }

    public static final Parcelable.Creator<SuanLiChartBean> CREATOR = new Parcelable.Creator<SuanLiChartBean>() {
        @Override
        public SuanLiChartBean createFromParcel(Parcel source) {
            return new SuanLiChartBean(source);
        }

        @Override
        public SuanLiChartBean[] newArray(int size) {
            return new SuanLiChartBean[size];
        }
    };
}
