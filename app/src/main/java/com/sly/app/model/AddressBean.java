package com.sly.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者 by K
 * 时间：on 2017/9/20 13:38
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AddressBean implements Parcelable {

    /**
     * M045_ID : 2
     * M045_MemberCode : V8955979
     * M045_ProvinceName : 测试
     * M045_CityName : 测试
     * M045_CountyName : 测试
     * M045_Address : 测试
     * M045_Consignee : 测试
     * M045_Mobile : 测试
     * M045_Tel : 测试
     * M045_Default : true
     */

    private int M045_ID;
    private String M045_MemberCode;
    private String M045_ProvinceName;
    private String M045_CityName;
    private String M045_CountyName;
    private String M045_Address;
    private String M045_Consignee;
    private String M045_Mobile;
    private String M045_Tel;
    private boolean M045_Default;

    public int getM045_ID() {
        return M045_ID;
    }

    public void setM045_ID(int M045_ID) {
        this.M045_ID = M045_ID;
    }

    public String getM045_MemberCode() {
        return M045_MemberCode;
    }

    public void setM045_MemberCode(String M045_MemberCode) {
        this.M045_MemberCode = M045_MemberCode;
    }

    public String getM045_ProvinceName() {
        return M045_ProvinceName;
    }

    public void setM045_ProvinceName(String M045_ProvinceName) {
        this.M045_ProvinceName = M045_ProvinceName;
    }

    public String getM045_CityName() {
        return M045_CityName;
    }

    public void setM045_CityName(String M045_CityName) {
        this.M045_CityName = M045_CityName;
    }

    public String getM045_CountyName() {
        return M045_CountyName;
    }

    public void setM045_CountyName(String M045_CountyName) {
        this.M045_CountyName = M045_CountyName;
    }

    public String getM045_Address() {
        return M045_Address;
    }

    public void setM045_Address(String M045_Address) {
        this.M045_Address = M045_Address;
    }

    public String getM045_Consignee() {
        return M045_Consignee;
    }

    public void setM045_Consignee(String M045_Consignee) {
        this.M045_Consignee = M045_Consignee;
    }

    public String getM045_Mobile() {
        return M045_Mobile;
    }

    public void setM045_Mobile(String M045_Mobile) {
        this.M045_Mobile = M045_Mobile;
    }

    public String getM045_Tel() {
        return M045_Tel;
    }

    public void setM045_Tel(String M045_Tel) {
        this.M045_Tel = M045_Tel;
    }

    public boolean isM045_Default() {
        return M045_Default;
    }

    public void setM045_Default(boolean M045_Default) {
        this.M045_Default = M045_Default;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.M045_ID);
        dest.writeString(this.M045_MemberCode);
        dest.writeString(this.M045_ProvinceName);
        dest.writeString(this.M045_CityName);
        dest.writeString(this.M045_CountyName);
        dest.writeString(this.M045_Address);
        dest.writeString(this.M045_Consignee);
        dest.writeString(this.M045_Mobile);
        dest.writeString(this.M045_Tel);
        dest.writeByte(this.M045_Default ? (byte) 1 : (byte) 0);
    }

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        this.M045_ID = in.readInt();
        this.M045_MemberCode = in.readString();
        this.M045_ProvinceName = in.readString();
        this.M045_CityName = in.readString();
        this.M045_CountyName = in.readString();
        this.M045_Address = in.readString();
        this.M045_Consignee = in.readString();
        this.M045_Mobile = in.readString();
        this.M045_Tel = in.readString();
        this.M045_Default = in.readByte() != 0;
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    @Override
    public String toString() {
        return "AddressBean{" +
                "M045_ID=" + M045_ID +
                ", M045_MemberCode='" + M045_MemberCode + '\'' +
                ", M045_ProvinceName='" + M045_ProvinceName + '\'' +
                ", M045_CityName='" + M045_CityName + '\'' +
                ", M045_CountyName='" + M045_CountyName + '\'' +
                ", M045_Address='" + M045_Address + '\'' +
                ", M045_Consignee='" + M045_Consignee + '\'' +
                ", M045_Mobile='" + M045_Mobile + '\'' +
                ", M045_Tel='" + M045_Tel + '\'' +
                ", M045_Default=" + M045_Default +
                '}';
    }
}