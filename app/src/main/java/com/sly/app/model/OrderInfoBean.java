package com.sly.app.model;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/22 20:46
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class OrderInfoBean {


    /**
     * D020_OrderNo : DC2017121800000022
     * D020_OrderDate : 2017-12-18 16:28:42
     * D020_BargainTime : null
     * D020_Payer : V8955979
     * D020_PayerPurseCode : 
     * D020_Buyer : 测试
     * D020_Saler : JW888888
     * D020_OrderClassCode : 04
     * D020_OrderStatusCode : WaitPay
     * D020_AcceptanceStatusCode : 01
     * D020_PayTypeCode : 01
     * D020_AuditStatusCode : Waitting
     * D020_SourceCode : JW
     * D020_BalanceStatusCode : 01
     * D020_ProcessCode : null
     * D020_Sum : 1280
     * D020_Carriage : 0
     * D020_Pv : 0
     * D020_Discount : 0
     * D020_Amount : 1280
     * D020_Remarks : Android
     * PageSize : 0
     * PageIndex : 0
     * MemberCode : null
     * Token : null
     * OrderDetail : [{"D021_DetailID":10078,"D021_OrderNo":"DC2017121800000022","D021_OrderStatusCode":"WaitPay","D021_ProductCode":"164","D021_ProductName":"女人花","D021_OptionName":"16支/盒","D021_MarketPrice":1280,"D021_Price":1280,"D021_Quantity":1,"D021_Discount":1,"D021_Sum":1280,"D021_SupplierPrice":1280,"D021_Rebate":0,"D021_IsPresell":true,"D021_Remarks":"Android","D021_DetailStatusCode":"","ListD021Detail":[],"MemberCode":null,"Token":null,"ComPic":"http://58.64.196.38:8081//Upload/Thumbnail/40-40/2a122f9c-8d08-46d2-8194-963dc8b5483d.jpg","OrderDateTime":null,"Status":0,"PrimaryKey":10078}]
     * D003_MallCode : DC
     * OrderDetailComPic : http://58.64.196.38:8081//Upload/Thumbnail/40-40/2a122f9c-8d08-46d2-8194-963dc8b5483d.jpg
     * Total : 16支/盒
     * TotalQuantity : 1
     * Price : 1280
     */

    private String D020_OrderNo;
    private String D020_OrderDate;
    private String D020_BargainTime;
    private String D020_Payer;
    private String D020_PayerPurseCode;
    private String D020_Buyer;
    private String D020_Saler;
    private String D020_OrderClassCode;
    private String D020_OrderStatusCode;
    private String D020_AcceptanceStatusCode;
    private String D020_PayTypeCode;
    private String D020_AuditStatusCode;
    private String D020_SourceCode;
    private String D020_BalanceStatusCode;
    private String D020_ProcessCode;
    private int D020_Sum;
    private int D020_Carriage;
    private int D020_Pv;
    private int D020_Discount;
    private int D020_Amount;
    private String D020_Remarks;
    private int PageSize;
    private int PageIndex;
    private String MemberCode;
    private String Token;
    private String D003_MallCode;
    private String OrderDetailComPic;
    private String Total;
    private int TotalQuantity;
    private int Price;
    private List<OrderDetailBean> OrderDetail;

    public String getD020_OrderNo() {
        return D020_OrderNo;
    }

    public void setD020_OrderNo(String D020_OrderNo) {
        this.D020_OrderNo = D020_OrderNo;
    }

    public String getD020_OrderDate() {
        return D020_OrderDate;
    }

    public void setD020_OrderDate(String D020_OrderDate) {
        this.D020_OrderDate = D020_OrderDate;
    }

    public String getD020_BargainTime() {
        return D020_BargainTime;
    }

    public void setD020_BargainTime(String D020_BargainTime) {
        this.D020_BargainTime = D020_BargainTime;
    }

    public String getD020_Payer() {
        return D020_Payer;
    }

    public void setD020_Payer(String D020_Payer) {
        this.D020_Payer = D020_Payer;
    }

    public String getD020_PayerPurseCode() {
        return D020_PayerPurseCode;
    }

    public void setD020_PayerPurseCode(String D020_PayerPurseCode) {
        this.D020_PayerPurseCode = D020_PayerPurseCode;
    }

    public String getD020_Buyer() {
        return D020_Buyer;
    }

    public void setD020_Buyer(String D020_Buyer) {
        this.D020_Buyer = D020_Buyer;
    }

    public String getD020_Saler() {
        return D020_Saler;
    }

    public void setD020_Saler(String D020_Saler) {
        this.D020_Saler = D020_Saler;
    }

    public String getD020_OrderClassCode() {
        return D020_OrderClassCode;
    }

    public void setD020_OrderClassCode(String D020_OrderClassCode) {
        this.D020_OrderClassCode = D020_OrderClassCode;
    }

    public String getD020_OrderStatusCode() {
        return D020_OrderStatusCode;
    }

    public void setD020_OrderStatusCode(String D020_OrderStatusCode) {
        this.D020_OrderStatusCode = D020_OrderStatusCode;
    }

    public String getD020_AcceptanceStatusCode() {
        return D020_AcceptanceStatusCode;
    }

    public void setD020_AcceptanceStatusCode(String D020_AcceptanceStatusCode) {
        this.D020_AcceptanceStatusCode = D020_AcceptanceStatusCode;
    }

    public String getD020_PayTypeCode() {
        return D020_PayTypeCode;
    }

    public void setD020_PayTypeCode(String D020_PayTypeCode) {
        this.D020_PayTypeCode = D020_PayTypeCode;
    }

    public String getD020_AuditStatusCode() {
        return D020_AuditStatusCode;
    }

    public void setD020_AuditStatusCode(String D020_AuditStatusCode) {
        this.D020_AuditStatusCode = D020_AuditStatusCode;
    }

    public String getD020_SourceCode() {
        return D020_SourceCode;
    }

    public void setD020_SourceCode(String D020_SourceCode) {
        this.D020_SourceCode = D020_SourceCode;
    }

    public String getD020_BalanceStatusCode() {
        return D020_BalanceStatusCode;
    }

    public void setD020_BalanceStatusCode(String D020_BalanceStatusCode) {
        this.D020_BalanceStatusCode = D020_BalanceStatusCode;
    }

    public String getD020_ProcessCode() {
        return D020_ProcessCode;
    }

    public void setD020_ProcessCode(String D020_ProcessCode) {
        this.D020_ProcessCode = D020_ProcessCode;
    }

    public int getD020_Sum() {
        return D020_Sum;
    }

    public void setD020_Sum(int D020_Sum) {
        this.D020_Sum = D020_Sum;
    }

    public int getD020_Carriage() {
        return D020_Carriage;
    }

    public void setD020_Carriage(int D020_Carriage) {
        this.D020_Carriage = D020_Carriage;
    }

    public int getD020_Pv() {
        return D020_Pv;
    }

    public void setD020_Pv(int D020_Pv) {
        this.D020_Pv = D020_Pv;
    }

    public int getD020_Discount() {
        return D020_Discount;
    }

    public void setD020_Discount(int D020_Discount) {
        this.D020_Discount = D020_Discount;
    }

    public int getD020_Amount() {
        return D020_Amount;
    }

    public void setD020_Amount(int D020_Amount) {
        this.D020_Amount = D020_Amount;
    }

    public String getD020_Remarks() {
        return D020_Remarks;
    }

    public void setD020_Remarks(String D020_Remarks) {
        this.D020_Remarks = D020_Remarks;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int PageIndex) {
        this.PageIndex = PageIndex;
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

    public String getD003_MallCode() {
        return D003_MallCode;
    }

    public void setD003_MallCode(String D003_MallCode) {
        this.D003_MallCode = D003_MallCode;
    }

    public String getOrderDetailComPic() {
        return OrderDetailComPic;
    }

    public void setOrderDetailComPic(String OrderDetailComPic) {
        this.OrderDetailComPic = OrderDetailComPic;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public int getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(int TotalQuantity) {
        this.TotalQuantity = TotalQuantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public List<OrderDetailBean> getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(List<OrderDetailBean> OrderDetail) {
        this.OrderDetail = OrderDetail;
    }

    public static class OrderDetailBean {
        /**
         * D021_DetailID : 10078
         * D021_OrderNo : DC2017121800000022
         * D021_OrderStatusCode : WaitPay
         * D021_ProductCode : 164
         * D021_ProductName : 女人花
         * D021_OptionName : 16支/盒
         * D021_MarketPrice : 1280
         * D021_Price : 1280
         * D021_Quantity : 1
         * D021_Discount : 1
         * D021_Sum : 1280
         * D021_SupplierPrice : 1280
         * D021_Rebate : 0
         * D021_IsPresell : true
         * D021_Remarks : Android
         * D021_DetailStatusCode : 
         * ListD021Detail : []
         * MemberCode : null
         * Token : null
         * ComPic : http://58.64.196.38:8081//Upload/Thumbnail/40-40/2a122f9c-8d08-46d2-8194-963dc8b5483d.jpg
         * OrderDateTime : null
         * Status : 0
         * PrimaryKey : 10078
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
