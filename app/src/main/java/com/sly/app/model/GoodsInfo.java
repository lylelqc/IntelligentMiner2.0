package com.sly.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者 by K
 * 时间：on 2017/9/13 15:29
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class GoodsInfo implements Parcelable {
    private String CartCode;//购物车ID
    private String ComID;//商品ID
    private String ComTitle;//商品名称
    private double Price;//商品价格
    private double Point;//商品价格
    private int count;//数量
    private String ComPicUrl;//图片地址
    private String OptionIDCombin;//商品选项
    private String OptionIDCombinRemark;//商品选项备注
    private String Fanxuan;//返卷
    private String mallType;//商城
    private String mallClass;//商城

    public String getMallClass() {
        return mallClass;
    }

    public void setMallClass(String mallClass) {
        this.mallClass = mallClass;
    }

    private String AddTime;//添加时间
    private boolean isChoosed;
    public GoodsInfo(){}
    public GoodsInfo(String cartCode, String comID, String comTitle, double price, int count, String comPicUrl, String optionIDCombin, String optionIDCombinRemark, String fanxuan, String mallType,String mallClass,String addTime, boolean isChoosed) {
        CartCode = cartCode;
        ComID = comID;
        ComTitle = comTitle;
        Price = price;
        this.count = count;
        ComPicUrl = comPicUrl;
        OptionIDCombin = optionIDCombin;
        OptionIDCombinRemark = optionIDCombinRemark;
        Fanxuan = fanxuan;
        this.mallType = mallType;
        this.mallClass = mallClass;
        this.isChoosed = isChoosed;
        this.AddTime=addTime;
    }

    public GoodsInfo(String cartCode, String comID, String comTitle, double price,  double point,int count, String comPicUrl, String optionIDCombin, String optionIDCombinRemark, String fanxuan, String mallType,String mallClass,String addTime, boolean isChoosed) {
        CartCode = cartCode;
        ComID = comID;
        ComTitle = comTitle;
        Price = price;
        Point = point;
        this.count = count;
        ComPicUrl = comPicUrl;
        OptionIDCombin = optionIDCombin;
        OptionIDCombinRemark = optionIDCombinRemark;
        Fanxuan = fanxuan;
        this.mallType = mallType;
        this.mallClass = mallClass;
        this.isChoosed = isChoosed;
        this.AddTime=addTime;
    }

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getCartCode() {
        return CartCode;
    }

    public void setCartCode(String cartCode) {
        CartCode = cartCode;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getMallType() {
        return mallType;
    }

    public void setMallType(String mallType) {
        this.mallType = mallType;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(CartCode);
        parcel.writeString(ComID);
        parcel.writeString(ComTitle);
        parcel.writeDouble(Price);
        parcel.writeDouble(Point);
        parcel.writeInt(count);
        parcel.writeString(ComPicUrl);
        parcel.writeString(OptionIDCombin);
        parcel.writeString(OptionIDCombinRemark);
        parcel.writeString(Fanxuan);
        parcel.writeString(mallType);
        parcel.writeString(mallClass);
        parcel.writeString(AddTime);
        parcel.writeByte((byte) (isChoosed ? 1 : 0));
    }
    protected GoodsInfo(Parcel in) {
        CartCode = in.readString();
        ComID = in.readString();
        ComTitle = in.readString();
        Price = in.readDouble();
        Point = in.readDouble();
        count = in.readInt();
        ComPicUrl = in.readString();
        OptionIDCombin = in.readString();
        OptionIDCombinRemark = in.readString();
        Fanxuan = in.readString();
        mallType = in.readString();
        mallClass=in.readString();
        AddTime = in.readString();
        isChoosed = in.readByte() != 0;
    }

    public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel in) {
            return new GoodsInfo(in);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "CartCode='" + CartCode + '\'' +
                ", ComID='" + ComID + '\'' +
                ", ComTitle='" + ComTitle + '\'' +
                ", Price=" + Price +
                ", Point=" + Point +
                ", count=" + count +
                ", ComPicUrl='" + ComPicUrl + '\'' +
                ", OptionIDCombin='" + OptionIDCombin + '\'' +
                ", OptionIDCombinRemark='" + OptionIDCombinRemark + '\'' +
                ", Fanxuan='" + Fanxuan + '\'' +
                ", mallType='" + mallType + '\'' +
                ", mallClass='" + mallClass + '\'' +
                ", AddTime='" + AddTime + '\'' +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
