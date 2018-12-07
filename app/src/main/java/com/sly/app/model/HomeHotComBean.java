package com.sly.app.model;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:32
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class HomeHotComBean {
    private String ImageUrl;//商品图片URL
    private String SupplierCode;//店铺名称
    private int ID;//商品id
    private String LastTime;
    private String ClassID;//商品分类ID
    private String Title;//商品名称
    private double Price;//价格
    private double MarketPrice;//市场价
    private String ShopID;//店铺编号
    private String MallTypeCode;
    private double RebateToken;//返券
    private double Point;//返券

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
    }

    private double carriage;
    private String FilePath;


    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public double getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getMallTypeCode() {
        return MallTypeCode;
    }

    public void setMallTypeCode(String mallTypeCode) {
        MallTypeCode = mallTypeCode;
    }

    public double getRebateToken() {
        return RebateToken;
    }

    public void setRebateToken(double rebateToken) {
        RebateToken = rebateToken;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getCarriage() {
        return carriage;
    }

    public void setCarriage(double carriage) {
        this.carriage = carriage;
    }

    @Override
    public String toString() {
        return "GoodsCategoryBean{" +
                "FilePath='" + FilePath + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", ID=" + ID +
                ", ClassID='" + ClassID + '\'' +
                ", Title='" + Title + '\'' +
                ", Price=" + Price +
                ", MallTypeCode='" + MallTypeCode + '\'' +
                ", RebateToken=" + RebateToken +
                ", carriage=" + carriage +
                ", MarketPrice=" + MarketPrice +
                ", ShopID='" + ShopID + '\'' +
                ", SupplierCode='" + SupplierCode + '\'' +
                '}';
    }
}

