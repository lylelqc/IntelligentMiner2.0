package com.sly.app.model;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/23 17:53
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class OrderDetailBean {


    /**
     * MemberCode : null
     * D021_OrderNo : null
     * Token : null
     * Phone : 18322545898
     * Name : sun
     * OrderStatus : 已发货
     * OrderClassCode : DC
     * OrderAdress : 北京市东城区东城区ghjk
     * PayType : 钱包
     * Points : null
     * CountSum : 198.00
     * Postage : 0.00
     * OrderDeliveryList : [{"D027_ID":5,"D027_OrderNo":"DC2017121800000027","D027_CompanyCode":"shunfeng","D027_CompanyName":"顺丰快递","D027_TransportNo":"20171200000001","D027_Remarks":"测试收货","D027_Oper":"master","D027_AddTime":"2017-12-18 17:37:35","ListD027DeliverInfo":[],"Status":1,"PrimaryKey":5}]
     * OrderDetailList : [{"D021_DetailID":10083,"D021_OrderNo":"DC2017121800000027","D021_OrderStatusCode":"WaitPay","D021_ProductCode":"40","D021_ProductName":"尚枣茶","D021_OptionName":"30条/盒","D021_MarketPrice":198,"D021_Price":198,"D021_Quantity":1,"D021_Discount":1,"D021_Sum":198,"D021_SupplierPrice":198,"D021_Rebate":0,"D021_IsPresell":true,"D021_Remarks":"Android","D021_DetailStatusCode":"","ListD021Detail":[],"MemberCode":null,"Token":null,"ComPic":"http://58.64.196.38:8081//Upload/Thumbnail/40-40/17d10b84-8f71-419c-b149-9d8130d225c8.jpg","OrderDateTime":"2017/12/18 17:35:13","Status":0,"PrimaryKey":10083}]
     * OrderDateTime : 0001-01-01 00:00:00
     */

    private String MemberCode;
    private String D021_OrderNo;
    private String Token;
    private String Phone;
    private String Name;
    private String OrderStatus;
    private String OrderClassCode;
    private String OrderAdress;
    private String PayType;
    private String Points;
    private String CountSum;
    private String Postage;
    private String OrderDateTime;
    private List<OrderDeliveryListBean> OrderDeliveryList;
    private List<OrderDetailListBean> OrderDetailList;

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String MemberCode) {
        this.MemberCode = MemberCode;
    }

    public String getD021_OrderNo() {
        return D021_OrderNo;
    }

    public void setD021_OrderNo(String D021_OrderNo) {
        this.D021_OrderNo = D021_OrderNo;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getOrderClassCode() {
        return OrderClassCode;
    }

    public void setOrderClassCode(String OrderClassCode) {
        this.OrderClassCode = OrderClassCode;
    }

    public String getOrderAdress() {
        return OrderAdress;
    }

    public void setOrderAdress(String OrderAdress) {
        this.OrderAdress = OrderAdress;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String PayType) {
        this.PayType = PayType;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String Points) {
        this.Points = Points;
    }

    public String getCountSum() {
        return CountSum;
    }

    public void setCountSum(String CountSum) {
        this.CountSum = CountSum;
    }

    public String getPostage() {
        return Postage;
    }

    public void setPostage(String Postage) {
        this.Postage = Postage;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public void setOrderDateTime(String OrderDateTime) {
        this.OrderDateTime = OrderDateTime;
    }

    public List<OrderDeliveryListBean> getOrderDeliveryList() {
        return OrderDeliveryList;
    }

    public void setOrderDeliveryList(List<OrderDeliveryListBean> OrderDeliveryList) {
        this.OrderDeliveryList = OrderDeliveryList;
    }

    public List<OrderDetailListBean> getOrderDetailList() {
        return OrderDetailList;
    }

    public void setOrderDetailList(List<OrderDetailListBean> OrderDetailList) {
        this.OrderDetailList = OrderDetailList;
    }

    public static class OrderDeliveryListBean {
        /**
         * D027_ID : 5
         * D027_OrderNo : DC2017121800000027
         * D027_CompanyCode : shunfeng
         * D027_CompanyName : 顺丰快递
         * D027_TransportNo : 20171200000001
         * D027_Remarks : 测试收货
         * D027_Oper : master
         * D027_AddTime : 2017-12-18 17:37:35
         * ListD027DeliverInfo : []
         * Status : 1
         * PrimaryKey : 5
         */

        private int D027_ID;
        private String D027_OrderNo;
        private String D027_CompanyCode;
        private String D027_CompanyName;
        private String D027_TransportNo;
        private String D027_Remarks;
        private String D027_Oper;
        private String D027_AddTime;
        private int Status;
        private int PrimaryKey;
        private List<?> ListD027DeliverInfo;

        public int getD027_ID() {
            return D027_ID;
        }

        public void setD027_ID(int D027_ID) {
            this.D027_ID = D027_ID;
        }

        public String getD027_OrderNo() {
            return D027_OrderNo;
        }

        public void setD027_OrderNo(String D027_OrderNo) {
            this.D027_OrderNo = D027_OrderNo;
        }

        public String getD027_CompanyCode() {
            return D027_CompanyCode;
        }

        public void setD027_CompanyCode(String D027_CompanyCode) {
            this.D027_CompanyCode = D027_CompanyCode;
        }

        public String getD027_CompanyName() {
            return D027_CompanyName;
        }

        public void setD027_CompanyName(String D027_CompanyName) {
            this.D027_CompanyName = D027_CompanyName;
        }

        public String getD027_TransportNo() {
            return D027_TransportNo;
        }

        public void setD027_TransportNo(String D027_TransportNo) {
            this.D027_TransportNo = D027_TransportNo;
        }

        public String getD027_Remarks() {
            return D027_Remarks;
        }

        public void setD027_Remarks(String D027_Remarks) {
            this.D027_Remarks = D027_Remarks;
        }

        public String getD027_Oper() {
            return D027_Oper;
        }

        public void setD027_Oper(String D027_Oper) {
            this.D027_Oper = D027_Oper;
        }

        public String getD027_AddTime() {
            return D027_AddTime;
        }

        public void setD027_AddTime(String D027_AddTime) {
            this.D027_AddTime = D027_AddTime;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getPrimaryKey() {
            return PrimaryKey;
        }

        public void setPrimaryKey(int PrimaryKey) {
            this.PrimaryKey = PrimaryKey;
        }

        public List<?> getListD027DeliverInfo() {
            return ListD027DeliverInfo;
        }

        public void setListD027DeliverInfo(List<?> ListD027DeliverInfo) {
            this.ListD027DeliverInfo = ListD027DeliverInfo;
        }
    }

    public static class OrderDetailListBean {
        /**
         * D021_DetailID : 10083
         * D021_OrderNo : DC2017121800000027
         * D021_OrderStatusCode : WaitPay
         * D021_ProductCode : 40
         * D021_ProductName : 尚枣茶
         * D021_OptionName : 30条/盒
         * D021_MarketPrice : 198
         * D021_Price : 198
         * D021_Quantity : 1
         * D021_Discount : 1
         * D021_Sum : 198
         * D021_SupplierPrice : 198
         * D021_Rebate : 0
         * D021_IsPresell : true
         * D021_Remarks : Android
         * D021_DetailStatusCode :
         * ListD021Detail : []
         * MemberCode : null
         * Token : null
         * ComPic : http://58.64.196.38:8081//Upload/Thumbnail/40-40/17d10b84-8f71-419c-b149-9d8130d225c8.jpg
         * OrderDateTime : 2017/12/18 17:35:13
         * Status : 0
         * PrimaryKey : 10083
         */

        private int D021_DetailID;
        private String D021_OrderNo;
        private String D021_OrderStatusCode;
        private String D021_ProductCode;
        private String D021_ProductName;
        private String D021_OptionName;
        private int D021_MarketPrice;
        private int D021_Price;
        private int D021_Quantity;
        private int D021_Discount;
        private int D021_Sum;
        private int D021_SupplierPrice;
        private int D021_Rebate;
        private boolean D021_IsPresell;
        private String D021_Remarks;
        private String D021_DetailStatusCode;
        private String MemberCode;
        private String Token;
        private String ComPic;
        private String OrderDateTime;
        private int Status;
        private int PrimaryKey;
        private List<?> ListD021Detail;

        public int getD021_DetailID() {
            return D021_DetailID;
        }

        public void setD021_DetailID(int D021_DetailID) {
            this.D021_DetailID = D021_DetailID;
        }

        public String getD021_OrderNo() {
            return D021_OrderNo;
        }

        public void setD021_OrderNo(String D021_OrderNo) {
            this.D021_OrderNo = D021_OrderNo;
        }

        public String getD021_OrderStatusCode() {
            return D021_OrderStatusCode;
        }

        public void setD021_OrderStatusCode(String D021_OrderStatusCode) {
            this.D021_OrderStatusCode = D021_OrderStatusCode;
        }

        public String getD021_ProductCode() {
            return D021_ProductCode;
        }

        public void setD021_ProductCode(String D021_ProductCode) {
            this.D021_ProductCode = D021_ProductCode;
        }

        public String getD021_ProductName() {
            return D021_ProductName;
        }

        public void setD021_ProductName(String D021_ProductName) {
            this.D021_ProductName = D021_ProductName;
        }

        public String getD021_OptionName() {
            return D021_OptionName;
        }

        public void setD021_OptionName(String D021_OptionName) {
            this.D021_OptionName = D021_OptionName;
        }

        public int getD021_MarketPrice() {
            return D021_MarketPrice;
        }

        public void setD021_MarketPrice(int D021_MarketPrice) {
            this.D021_MarketPrice = D021_MarketPrice;
        }

        public int getD021_Price() {
            return D021_Price;
        }

        public void setD021_Price(int D021_Price) {
            this.D021_Price = D021_Price;
        }

        public int getD021_Quantity() {
            return D021_Quantity;
        }

        public void setD021_Quantity(int D021_Quantity) {
            this.D021_Quantity = D021_Quantity;
        }

        public int getD021_Discount() {
            return D021_Discount;
        }

        public void setD021_Discount(int D021_Discount) {
            this.D021_Discount = D021_Discount;
        }

        public int getD021_Sum() {
            return D021_Sum;
        }

        public void setD021_Sum(int D021_Sum) {
            this.D021_Sum = D021_Sum;
        }

        public int getD021_SupplierPrice() {
            return D021_SupplierPrice;
        }

        public void setD021_SupplierPrice(int D021_SupplierPrice) {
            this.D021_SupplierPrice = D021_SupplierPrice;
        }

        public int getD021_Rebate() {
            return D021_Rebate;
        }

        public void setD021_Rebate(int D021_Rebate) {
            this.D021_Rebate = D021_Rebate;
        }

        public boolean isD021_IsPresell() {
            return D021_IsPresell;
        }

        public void setD021_IsPresell(boolean D021_IsPresell) {
            this.D021_IsPresell = D021_IsPresell;
        }

        public String getD021_Remarks() {
            return D021_Remarks;
        }

        public void setD021_Remarks(String D021_Remarks) {
            this.D021_Remarks = D021_Remarks;
        }

        public String getD021_DetailStatusCode() {
            return D021_DetailStatusCode;
        }

        public void setD021_DetailStatusCode(String D021_DetailStatusCode) {
            this.D021_DetailStatusCode = D021_DetailStatusCode;
        }

        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String MemberCode) {
            this.MemberCode = MemberCode;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getComPic() {
            return ComPic;
        }

        public void setComPic(String ComPic) {
            this.ComPic = ComPic;
        }

        public String getOrderDateTime() {
            return OrderDateTime;
        }

        public void setOrderDateTime(String OrderDateTime) {
            this.OrderDateTime = OrderDateTime;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getPrimaryKey() {
            return PrimaryKey;
        }

        public void setPrimaryKey(int PrimaryKey) {
            this.PrimaryKey = PrimaryKey;
        }

        public List<?> getListD021Detail() {
            return ListD021Detail;
        }

        public void setListD021Detail(List<?> ListD021Detail) {
            this.ListD021Detail = ListD021Detail;
        }
    }
}
