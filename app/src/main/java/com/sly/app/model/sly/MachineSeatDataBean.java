package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class MachineSeatDataBean implements Parcelable {
    /**
     * "Mine20_Name\":\"\",
     * \"Mine18_ImageURl\":\"\",
     \"Province\":\"北京\",
     \"Mine79_ID\":\"1\",
     \"Mine79_MinerSysCode\":\"M0000004\",
     \"Mine79_Intro\":\"666\",
     \"Mine79_Position\":\"66\",
     \"Mine79_Count\":100,
     \"Mine79_MinPrice\":0.3000,
     \"Mine79_MaxPrice\":0.5000,
     \"Mine79_ManageFee\":55.0000,
     \"Mine79_Aptitude\":\"55\",
     \"Mine79_Link\":\"55\",
     \"Mine79_Pic1\":null,
     \"Mine79_Pic2\":null,
     \"Mine79_Pic3\":null,
     \"Mine79_Pic4\":null,
     \"Mine79_Province\":\"110000\",
     \"Mine79_City\":null,
     \"Mine79_Country\":null,
     \"Mine79_AuditStatusCode\":"1"
     \"isDelete\":\"True\"}

     */
    private String Mine20_Name;
    private String Mine18_ImageURl;
    private String Province;
//    private String City;
//    private String Country;
    private String Mine79_ID;
    private String Mine79_MinerSysCode;
    private String Mine79_Intro;
    private String Mine79_Position;
    private int Mine79_Count;
    private double Mine79_MinPrice;
    private double Mine79_MaxPrice;
    private double Mine79_ManageFee;
    private String Mine79_Aptitude;
    private String Mine79_Link;
    private String Mine79_Pic1;
    private String Mine79_Pic2;
    private String Mine79_Pic3;
    private String Mine79_Pic4;
    private String Mine79_Province;
    private String Mine79_City;
    private String Mine79_Country;
    private String Mine79_AuditStatusCode;
    private String isDelete;

    public String getMine20_Name() {
        return Mine20_Name;
    }

    public void setMine20_Name(String mine20_Name) {
        Mine20_Name = mine20_Name;
    }

    public String getMine18_ImageURl() {
        return Mine18_ImageURl;
    }

    public void setMine18_ImageURl(String mine18_ImageURl) {
        Mine18_ImageURl = mine18_ImageURl;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getMine79_ID() {
        return Mine79_ID;
    }

    public void setMine79_ID(String mine79_ID) {
        Mine79_ID = mine79_ID;
    }

    public String getMine79_MinerSysCode() {
        return Mine79_MinerSysCode;
    }

    public void setMine79_MinerSysCode(String mine79_MinerSysCode) {
        Mine79_MinerSysCode = mine79_MinerSysCode;
    }

    public String getMine79_Intro() {
        return Mine79_Intro;
    }

    public void setMine79_Intro(String mine79_Intro) {
        Mine79_Intro = mine79_Intro;
    }

    public String getMine79_Position() {
        return Mine79_Position;
    }

    public void setMine79_Position(String mine79_Position) {
        Mine79_Position = mine79_Position;
    }

    public int getMine79_Count() {
        return Mine79_Count;
    }

    public void setMine79_Count(int mine79_Count) {
        Mine79_Count = mine79_Count;
    }

    public double getMine79_MinPrice() {
        return Mine79_MinPrice;
    }

    public void setMine79_MinPrice(double mine79_MinPrice) {
        Mine79_MinPrice = mine79_MinPrice;
    }

    public double getMine79_MaxPrice() {
        return Mine79_MaxPrice;
    }

    public void setMine79_MaxPrice(double mine79_MaxPrice) {
        Mine79_MaxPrice = mine79_MaxPrice;
    }

    public double getMine79_ManageFee() {
        return Mine79_ManageFee;
    }

    public void setMine79_ManageFee(double mine79_ManageFee) {
        Mine79_ManageFee = mine79_ManageFee;
    }

    public String getMine79_Aptitude() {
        return Mine79_Aptitude;
    }

    public void setMine79_Aptitude(String mine79_Aptitude) {
        Mine79_Aptitude = mine79_Aptitude;
    }

    public String getMine79_Link() {
        return Mine79_Link;
    }

    public void setMine79_Link(String mine79_Link) {
        Mine79_Link = mine79_Link;
    }

    public String getMine79_Pic1() {
        return Mine79_Pic1;
    }

    public void setMine79_Pic1(String mine79_Pic1) {
        Mine79_Pic1 = mine79_Pic1;
    }

    public String getMine79_Pic2() {
        return Mine79_Pic2;
    }

    public void setMine79_Pic2(String mine79_Pic2) {
        Mine79_Pic2 = mine79_Pic2;
    }

    public String getMine79_Pic3() {
        return Mine79_Pic3;
    }

    public void setMine79_Pic3(String mine79_Pic3) {
        Mine79_Pic3 = mine79_Pic3;
    }

    public String getMine79_Pic4() {
        return Mine79_Pic4;
    }

    public void setMine79_Pic4(String mine79_Pic4) {
        Mine79_Pic4 = mine79_Pic4;
    }

    public String getMine79_Province() {
        return Mine79_Province;
    }

    public void setMine79_Province(String mine79_Province) {
        Mine79_Province = mine79_Province;
    }

    public String getMine79_City() {
        return Mine79_City;
    }

    public void setMine79_City(String mine79_City) {
        Mine79_City = mine79_City;
    }

    public String getMine79_Country() {
        return Mine79_Country;
    }

    public void setMine79_Country(String mine79_Country) {
        Mine79_Country = mine79_Country;
    }

    public String getMine79_AuditStatusCode() {
        return Mine79_AuditStatusCode;
    }

    public void setMine79_AuditStatusCode(String mine79_AuditStatusCode) {
        Mine79_AuditStatusCode = mine79_AuditStatusCode;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Mine20_Name);
        dest.writeString(this.Mine18_ImageURl);
        dest.writeString(this.Province);
//        dest.writeString(this.City);
//        dest.writeString(this.Country);
        dest.writeString(this.Mine79_ID);
        dest.writeString(this.Mine79_MinerSysCode);
        dest.writeString(this.Mine79_Intro);
        dest.writeString(this.Mine79_Position);
        dest.writeInt(this.Mine79_Count);
        dest.writeDouble(this.Mine79_MinPrice);
        dest.writeDouble(this.Mine79_MaxPrice);
        dest.writeDouble(this.Mine79_ManageFee);
        dest.writeString(this.Mine79_Aptitude);
        dest.writeString(this.Mine79_Link);
        dest.writeString(this.Mine79_Pic1);
        dest.writeString(this.Mine79_Pic2);
        dest.writeString(this.Mine79_Pic3);
        dest.writeString(this.Mine79_Pic4);
        dest.writeString(this.Mine79_Province);
        dest.writeString(this.Mine79_City);
        dest.writeString(this.Mine79_Country);
        dest.writeString(this.Mine79_AuditStatusCode);
        dest.writeString(this.isDelete);
    }

    public MachineSeatDataBean() {
    }

    protected MachineSeatDataBean(Parcel in) {
        this.Mine20_Name = in.readString();
        this.Mine18_ImageURl = in.readString();
        this.Province = in.readString();
//        this.City = in.readString();
//        this.Country = in.readString();
        this.Mine79_ID = in.readString();
        this.Mine79_MinerSysCode = in.readString();
        this.Mine79_Intro = in.readString();
        this.Mine79_Position = in.readString();
        this.Mine79_Count = in.readInt();
        this.Mine79_MinPrice = in.readDouble();
        this.Mine79_MaxPrice = in.readDouble();
        this.Mine79_ManageFee = in.readDouble();
        this.Mine79_Aptitude = in.readString();
        this.Mine79_Link = in.readString();
        this.Mine79_Pic1 = in.readString();
        this.Mine79_Pic2 = in.readString();
        this.Mine79_Pic3 = in.readString();
        this.Mine79_Pic4 = in.readString();
        this.Mine79_Province = in.readString();
        this.Mine79_City = in.readString();
        this.Mine79_Country = in.readString();
        this.Mine79_AuditStatusCode = in.readString();
        this.isDelete = in.readString();
    }

    public static final Parcelable.Creator<MachineSeatDataBean> CREATOR = new Parcelable.Creator<MachineSeatDataBean>() {
        @Override
        public MachineSeatDataBean createFromParcel(Parcel source) {
            return new MachineSeatDataBean(source);
        }

        @Override
        public MachineSeatDataBean[] newArray(int size) {
            return new MachineSeatDataBean[size];
        }
    };
}
