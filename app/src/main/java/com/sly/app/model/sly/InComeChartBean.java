package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class InComeChartBean implements Parcelable {
    private int ID;
    private String MineCode;
    private String MineName;

    private double WeekIncome;
    private double MonthIncome;

    private String Date1;
    private double Date1Income;
    private String Date2;
    private double Date2Income;
    private String Date3;
    private double Date3Income;
    private String Date4;
    private double Date4Income;
    private String Date5;
    private double Date5Income;
    private String Date6;
    private double Date6Income;
    private String Date7;
    private double Date7Income;

    @Override
    public String toString() {
        return "InComeChartBean{" +
                "ID=" + ID +
                ", MineCode='" + MineCode + '\'' +
                ", MineName='" + MineName + '\'' +
                ", WeekIncome=" + WeekIncome +
                ", MonthIncome=" + MonthIncome +
                ", Date1='" + Date1 + '\'' +
                ", Date1Income=" + Date1Income +
                ", Date2='" + Date2 + '\'' +
                ", Date2Income=" + Date2Income +
                ", Date3='" + Date3 + '\'' +
                ", Date3Income=" + Date3Income +
                ", Date4='" + Date4 + '\'' +
                ", Date4Income=" + Date4Income +
                ", Date5='" + Date5 + '\'' +
                ", Date5Income=" + Date5Income +
                ", Date6='" + Date6 + '\'' +
                ", Date6Income=" + Date6Income +
                ", Date7='" + Date7 + '\'' +
                ", Date7Income=" + Date7Income +
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

    public double getWeekIncome() {
        return WeekIncome;
    }

    public void setWeekIncome(double weekIncome) {
        WeekIncome = weekIncome;
    }

    public double getMonthIncome() {
        return MonthIncome;
    }

    public void setMonthIncome(double monthIncome) {
        MonthIncome = monthIncome;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public double getDate1Income() {
        return Date1Income;
    }

    public void setDate1Income(double date1Income) {
        Date1Income = date1Income;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    public double getDate2Income() {
        return Date2Income;
    }

    public void setDate2Income(double date2Income) {
        Date2Income = date2Income;
    }

    public String getDate3() {
        return Date3;
    }

    public void setDate3(String date3) {
        Date3 = date3;
    }

    public double getDate3Income() {
        return Date3Income;
    }

    public void setDate3Income(double date3Income) {
        Date3Income = date3Income;
    }

    public String getDate4() {
        return Date4;
    }

    public void setDate4(String date4) {
        Date4 = date4;
    }

    public double getDate4Income() {
        return Date4Income;
    }

    public void setDate4Income(double date4Income) {
        Date4Income = date4Income;
    }

    public String getDate5() {
        return Date5;
    }

    public void setDate5(String date5) {
        Date5 = date5;
    }

    public double getDate5Income() {
        return Date5Income;
    }

    public void setDate5Income(double date5Income) {
        Date5Income = date5Income;
    }

    public String getDate6() {
        return Date6;
    }

    public void setDate6(String date6) {
        Date6 = date6;
    }

    public double getDate6Income() {
        return Date6Income;
    }

    public void setDate6Income(double date6Income) {
        Date6Income = date6Income;
    }

    public String getDate7() {
        return Date7;
    }

    public void setDate7(String date7) {
        Date7 = date7;
    }

    public double getDate7Income() {
        return Date7Income;
    }

    public void setDate7Income(double date7Income) {
        Date7Income = date7Income;
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
        dest.writeDouble(this.WeekIncome);
        dest.writeDouble(this.MonthIncome);
        dest.writeString(this.Date1);
        dest.writeDouble(this.Date1Income);
        dest.writeString(this.Date2);
        dest.writeDouble(this.Date2Income);
        dest.writeString(this.Date3);
        dest.writeDouble(this.Date3Income);
        dest.writeString(this.Date4);
        dest.writeDouble(this.Date4Income);
        dest.writeString(this.Date5);
        dest.writeDouble(this.Date5Income);
        dest.writeString(this.Date6);
        dest.writeDouble(this.Date6Income);
        dest.writeString(this.Date7);
        dest.writeDouble(this.Date7Income);
    }

    public InComeChartBean() {
    }

    protected InComeChartBean(Parcel in) {
        this.ID = in.readInt();
        this.MineCode = in.readString();
        this.MineName = in.readString();
        this.WeekIncome = in.readDouble();
        this.MonthIncome = in.readDouble();
        this.Date1 = in.readString();
        this.Date1Income = in.readDouble();
        this.Date2 = in.readString();
        this.Date2Income = in.readDouble();
        this.Date3 = in.readString();
        this.Date3Income = in.readDouble();
        this.Date4 = in.readString();
        this.Date4Income = in.readDouble();
        this.Date5 = in.readString();
        this.Date5Income = in.readDouble();
        this.Date6 = in.readString();
        this.Date6Income = in.readDouble();
        this.Date7 = in.readString();
        this.Date7Income = in.readDouble();
    }

    public static final Parcelable.Creator<InComeChartBean> CREATOR = new Parcelable.Creator<InComeChartBean>() {
        @Override
        public InComeChartBean createFromParcel(Parcel source) {
            return new InComeChartBean(source);
        }

        @Override
        public InComeChartBean[] newArray(int size) {
            return new InComeChartBean[size];
        }
    };
}
