package com.sly.app.model.orderBean;

/**
 * Created by 01 on 2016/10/27.
 *
 *   订单状态
 */
public class OrderStatu {

    private String OrderNo;
    private String OrderStatusCode;
    private String Count;
    private Double Price;
    private String ComPic;
    private String ComID;
    private String C003_ServiceScore;
    private String OrderClassCode;//商城类型
    private String Carrige;

    public String getCarrige() {
        return Carrige;
    }

    public void setCarrige(String carrige) {
        Carrige = carrige;
    }

    public String getOrderClassCode() {
        if ("www".equals(OrderClassCode)){
            OrderClassCode="zg";
        }

        return OrderClassCode;
    }

    public void setOrderClassCode(String orderClassCode) {
        OrderClassCode = orderClassCode;
    }



    public String getC003_ServiceScore() {
        return C003_ServiceScore;
    }

    public void setC003_ServiceScore(String c003_ServiceScore) {
        C003_ServiceScore = c003_ServiceScore;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderStatusCode() {
        return OrderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        OrderStatusCode = orderStatusCode;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getComPic() {
        return ComPic;
    }

    public void setComPic(String comPic) {
        ComPic = comPic;
    }

    public String getComID() {
        return ComID;
    }

    public void setComID(String comID) {
        ComID = comID;
    }

    @Override
    public String toString() {
        return "OrderStatu{" +
                "OrderNo='" + OrderNo + '\'' +
                ", OrderStatusCode='" + OrderStatusCode + '\'' +
                ", Count='" + Count + '\'' +
                ", Price=" + Price +
                ", ComPic='" + ComPic + '\'' +
                ", ComID='" + ComID + '\'' +
                ", C003_ServiceScore='" + C003_ServiceScore + '\'' +
                ", OrderClassCode='" + OrderClassCode + '\'' +
                '}';
    }
}
