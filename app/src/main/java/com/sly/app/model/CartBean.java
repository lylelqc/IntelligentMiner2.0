package com.sly.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者 by K
 * 时间：on 2017/9/21 12:11
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class CartBean implements Parcelable {
    /**
     * MemberCode : 15772747952
     * Token : null
     * CartCode : CC1505966210746
     * AddTime : 2017-09-21 11:56:49
     * ComID : 20
     * ComTitle : 测试 宋瓷产品B
     * Price : 120.0
     * Quantity : 1
     * ComPicUrl : http://202.101.233.167:8083//ckfinder//userfiles//images//20170921103059986.jpg
     * OptionIDCombin :
     * OptionIDCombinRemark : null
     * Fanxuan : null
     * PageIndex : null
     * PageSize : null
     * mallType : JX
     */

    private String MemberCode;
    private String Token;
    private String CartCode;
    private String AddTime;
    private String ComID;
    private String ComTitle;
    private double Price;
    private int Quantity;
    private String ComPicUrl;
    private String OptionIDCombin;
    private String OptionIDCombinRemark;
    private String Fanxuan;
    private String PageIndex;
    private String PageSize;
    private String mallType;


    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String memberCode) {
        MemberCode = memberCode;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCartCode() {
        return CartCode;
    }

    public void setCartCode(String cartCode) {
        CartCode = cartCode;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getComID() {
        return ComID;
    }

    public void setComID(String comID) {
        ComID = comID;
    }

    public String getComTitle() {
        return ComTitle;
    }

    public void setComTitle(String comTitle) {
        ComTitle = comTitle;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getComPicUrl() {
        return ComPicUrl;
    }

    public void setComPicUrl(String comPicUrl) {
        ComPicUrl = comPicUrl;
    }

    public String getOptionIDCombin() {
        return OptionIDCombin;
    }

    public void setOptionIDCombin(String optionIDCombin) {
        OptionIDCombin = optionIDCombin;
    }

    public String getOptionIDCombinRemark() {
        return OptionIDCombinRemark;
    }

    public void setOptionIDCombinRemark(String optionIDCombinRemark) {
        OptionIDCombinRemark = optionIDCombinRemark;
    }

    public String getFanxuan() {
        return Fanxuan;
    }

    public void setFanxuan(String fanxuan) {
        Fanxuan = fanxuan;
    }

    public String getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }

    public String getPageSize() {
        return PageSize;
    }

    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }

    public String getMallType() {
        return mallType;
    }

    public void setMallType(String mallType) {
        this.mallType = mallType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.MemberCode);
        dest.writeString(this.Token);
        dest.writeString(this.CartCode);
        dest.writeString(this.AddTime);
        dest.writeString(this.ComID);
        dest.writeString(this.ComTitle);
        dest.writeDouble(this.Price);
        dest.writeInt(this.Quantity);
        dest.writeString(this.ComPicUrl);
        dest.writeString(this.OptionIDCombin);
        dest.writeString(this.OptionIDCombinRemark);
        dest.writeString(this.Fanxuan);
        dest.writeString(this.PageIndex);
        dest.writeString(this.PageSize);
        dest.writeString(this.mallType);
    }

    public CartBean() {
    }

    protected CartBean(Parcel in) {
        this.MemberCode = in.readString();
        this.Token = in.readString();
        this.CartCode = in.readString();
        this.AddTime = in.readString();
        this.ComID = in.readString();
        this.ComTitle = in.readString();
        this.Price = in.readDouble();
        this.Quantity = in.readInt();
        this.ComPicUrl = in.readString();
        this.OptionIDCombin = in.readString();
        this.OptionIDCombinRemark = in.readString();
        this.Fanxuan = in.readString();
        this.PageIndex = in.readString();
        this.PageSize = in.readString();
        this.mallType = in.readString();
    }

    public static final Creator<CartBean> CREATOR = new Creator<CartBean>() {
        @Override
        public CartBean createFromParcel(Parcel source) {
            return new CartBean(source);
        }

        @Override
        public CartBean[] newArray(int size) {
            return new CartBean[size];
        }
    };
}
