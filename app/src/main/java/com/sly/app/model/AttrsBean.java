package com.sly.app.model;

/**
 * 作者 by K
 * 时间：on 2017/9/21 15:33
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AttrsBean {


    /**
     * ComID : 164
     * Points : 0
     * OptionGroupName : 16支/盒
     * ComOptionName :
     * GroupID : 10548
     * stock : 1000
     * Price : 0.0
     * Sold : 0
     */

    private String ComID;
    private int Points;
    private String OptionGroupName;
    private String ComOptionName;
    private int GroupID;
    private int stock;
    private double Price;
    private int Sold;

    public String getComID() {
        return ComID;
    }

    public void setComID(String ComID) {
        this.ComID = ComID;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int Points) {
        this.Points = Points;
    }

    public String getOptionGroupName() {
        return OptionGroupName;
    }

    public void setOptionGroupName(String OptionGroupName) {
        this.OptionGroupName = OptionGroupName;
    }

    public String getComOptionName() {
        return ComOptionName;
    }

    public void setComOptionName(String ComOptionName) {
        this.ComOptionName = ComOptionName;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getSold() {
        return Sold;
    }

    public void setSold(int Sold) {
        this.Sold = Sold;
    }
}
