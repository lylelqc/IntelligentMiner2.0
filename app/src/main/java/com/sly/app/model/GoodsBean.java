package com.sly.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/19 16:41
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class GoodsBean implements Parcelable {


    /**
     * ComID : 184
     * ComCode : YY002
     * ComTitle : 最享瘦 玲珑紧致草本油
     * ComName : 最享瘦 玲珑紧致草本油
     * MartPrice : 128
     * DefaultPrice : 128
     * Point : 0
     * Descript : http://58.64.137.184:8081//Upload/Thumbnail/40-40/2beca1c5-4fe8-4a77-9cd2-066ac6a2fd96.jpg
     * ImgList : ["http://58.64.137.184:8081//Upload/Thumbnail/40-40/149710b4-ba85-4534-aff3-85b2328f2f8e.jpg","http://58.64.137.184:8081//Upload/Thumbnail/40-40/96bb777a-aaa3-4ae7-a92d-967a161f7dee.jpg","http://58.64.137.184:8081//Upload/Thumbnail/40-40/b662cc73-461e-48db-b77a-5d810ae15820.jpg","http://58.64.137.184:8081//Upload/Thumbnail/40-40/2beca1c5-4fe8-4a77-9cd2-066ac6a2fd96.jpg"]
     * mallType : DC
     * Postage : 0
     * Sales : 0
     * Stock : 999
     */

    private int ComID;
    private String ComCode;
    private String ComTitle;
    private String ComName;
    private int MartPrice;
    private int DefaultPrice;
    private int Point;
    private String Descript;
    private String mallType;
    private String Postage;
    private String Sales;
    private String Stock;
    private List<String> ImgList;

    public int getComID() {
        return ComID;
    }

    public void setComID(int ComID) {
        this.ComID = ComID;
    }

    public String getComCode() {
        return ComCode;
    }

    public void setComCode(String ComCode) {
        this.ComCode = ComCode;
    }

    public String getComTitle() {
        return ComTitle;
    }

    public void setComTitle(String ComTitle) {
        this.ComTitle = ComTitle;
    }

    public String getComName() {
        return ComName;
    }

    public void setComName(String ComName) {
        this.ComName = ComName;
    }

    public int getMartPrice() {
        return MartPrice;
    }

    public void setMartPrice(int MartPrice) {
        this.MartPrice = MartPrice;
    }

    public int getDefaultPrice() {
        return DefaultPrice;
    }

    public void setDefaultPrice(int DefaultPrice) {
        this.DefaultPrice = DefaultPrice;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }

    public String getDescript() {
        return Descript;
    }

    public void setDescript(String Descript) {
        this.Descript = Descript;
    }

    public String getMallType() {
        return mallType;
    }

    public void setMallType(String mallType) {
        this.mallType = mallType;
    }

    public String getPostage() {
        return Postage;
    }

    public void setPostage(String Postage) {
        this.Postage = Postage;
    }

    public String getSales() {
        return Sales;
    }

    public void setSales(String Sales) {
        this.Sales = Sales;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String Stock) {
        this.Stock = Stock;
    }

    public List<String> getImgList() {
        return ImgList;
    }

    public void setImgList(List<String> ImgList) {
        this.ImgList = ImgList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ComID);
        dest.writeString(this.ComCode);
        dest.writeString(this.ComTitle);
        dest.writeString(this.ComName);
        dest.writeInt(this.MartPrice);
        dest.writeInt(this.DefaultPrice);
        dest.writeInt(this.Point);
        dest.writeString(this.Descript);
        dest.writeString(this.mallType);
        dest.writeString(this.Postage);
        dest.writeString(this.Sales);
        dest.writeString(this.Stock);
        dest.writeStringList(this.ImgList);
    }

    public GoodsBean() {
    }

    protected GoodsBean(Parcel in) {
        this.ComID = in.readInt();
        this.ComCode = in.readString();
        this.ComTitle = in.readString();
        this.ComName = in.readString();
        this.MartPrice = in.readInt();
        this.DefaultPrice = in.readInt();
        this.Point = in.readInt();
        this.Descript = in.readString();
        this.mallType = in.readString();
        this.Postage = in.readString();
        this.Sales = in.readString();
        this.Stock = in.readString();
        this.ImgList = in.createStringArrayList();
    }

    public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
        @Override
        public GoodsBean createFromParcel(Parcel source) {
            return new GoodsBean(source);
        }

        @Override
        public GoodsBean[] newArray(int size) {
            return new GoodsBean[size];
        }
    };
}
