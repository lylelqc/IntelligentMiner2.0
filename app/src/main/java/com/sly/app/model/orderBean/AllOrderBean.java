package com.sly.app.model.orderBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 01 on 2017/6/14.
 */
public class AllOrderBean implements Serializable {


    /**
     * status : 1
     * msg : 成功
     * data : [{"D001_OrderNo":"M1706160000000009","D001_OrderDate":"2017-06-16 15:25:03","D001_BargainTime":null,"D001_Buyer":"13632244809","D001_Saler":"Yml888888","D001_Sum":1212,"D001_Points":0,"D001_Postage":0,"D001_BayerLeaveWord":"普通会员","D001_SendPoint":0,"D001_SendScore":0,"D001_Remark":"","D001_OrderClassCode":"00","D001_OrderStatusCode":"WaitPay","D001_IsAppraise":false,"OrderDetail":[{"D002_OrderDetailID":442,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":442},{"D002_OrderDetailID":443,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":443},{"D002_OrderDetailID":440,"D002_OrderNo":"M1706160000000007","D002_OrderDate":"2017-06-16 14:35:36","D002_ComID":17,"D002_ComTitle":"天然植物美白面霜","D002_OptionIDCombin":"单品50g","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/9402ccdd-51bb-41d3-b978-6b45a85cf721.jpg","D002_ComInfo":"","D002_CostPrice":326,"D002_BuyPrice":326,"D002_BuyPoints":0,"D002_Quantity":2,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":440}]},{"D001_OrderNo":"M1706160000000007","D001_OrderDate":"2017-06-16 14:35:36","D001_BargainTime":null,"D001_Buyer":"13632244809","D001_Saler":"Yml888888","D001_Sum":326,"D001_Points":0,"D001_Postage":0,"D001_BayerLeaveWord":"普通会员","D001_SendPoint":0,"D001_SendScore":0,"D001_Remark":"","D001_OrderClassCode":"00","D001_OrderStatusCode":"WaitPay","D001_IsAppraise":false,"OrderDetail":[{"D002_OrderDetailID":442,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":442},{"D002_OrderDetailID":443,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":443},{"D002_OrderDetailID":440,"D002_OrderNo":"M1706160000000007","D002_OrderDate":"2017-06-16 14:35:36","D002_ComID":17,"D002_ComTitle":"天然植物美白面霜","D002_OptionIDCombin":"单品50g","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/9402ccdd-51bb-41d3-b978-6b45a85cf721.jpg","D002_ComInfo":"","D002_CostPrice":326,"D002_BuyPrice":326,"D002_BuyPoints":0,"D002_Quantity":2,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":440}]}]
     */

    private String status;
    private String msg;
    /**
     * D001_OrderNo : M1706160000000009
     * D001_OrderDate : 2017-06-16 15:25:03
     * D001_BargainTime : null
     * D001_Buyer : 13632244809
     * D001_Saler : Yml888888
     * D001_Sum : 1212.0
     * D001_Points : 0.0
     * D001_Postage : 0.0
     * D001_BayerLeaveWord : 普通会员
     * D001_SendPoint : 0.0
     * D001_SendScore : 0.0
     * D001_Remark :
     * D001_OrderClassCode : 00
     * D001_OrderStatusCode : WaitPay
     * D001_IsAppraise : false
     * OrderDetail : [{"D002_OrderDetailID":442,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":442},{"D002_OrderDetailID":443,"D002_OrderNo":"M1706160000000009","D002_OrderDate":"2017-06-16 15:25:03","D002_ComID":15,"D002_ComTitle":"紧致保湿化妆水","D002_OptionIDCombin":"单品120ml","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg","D002_ComInfo":"","D002_CostPrice":606,"D002_BuyPrice":606,"D002_BuyPoints":0,"D002_Quantity":6,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":443},{"D002_OrderDetailID":440,"D002_OrderNo":"M1706160000000007","D002_OrderDate":"2017-06-16 14:35:36","D002_ComID":17,"D002_ComTitle":"天然植物美白面霜","D002_OptionIDCombin":"单品50g","D002_OptionIDCombinRemark":"","D002_ComPicUrl":"/Upload/9402ccdd-51bb-41d3-b978-6b45a85cf721.jpg","D002_ComInfo":"","D002_CostPrice":326,"D002_BuyPrice":326,"D002_BuyPoints":0,"D002_Quantity":2,"D002_Remark":"","D002_IsAppraise":false,"Status":0,"PrimaryKey":440}]
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements Serializable {
        private String D001_OrderNo;
        private String D001_OrderDate;
        private Object D001_BargainTime;
        private String D001_Buyer;
        private String D001_Saler;
        private double D001_Sum;
        private double D001_Points;
        private double D001_Postage;
        private String D001_BayerLeaveWord;
        private double D001_SendPoint;
        private double D001_SendScore;
        private String D001_Remark;
        private String D001_OrderClassCode;
        private String D001_OrderStatusCode;
        private boolean D001_IsAppraise;
        /**
         * D002_OrderDetailID : 442
         * D002_OrderNo : M1706160000000009
         * D002_OrderDate : 2017-06-16 15:25:03
         * D002_ComID : 15
         * D002_ComTitle : 紧致保湿化妆水
         * D002_OptionIDCombin : 单品120ml
         * D002_OptionIDCombinRemark :
         * D002_ComPicUrl : /Upload/e9131a2d-5071-45ed-9d48-0ec20a5c293b.jpg
         * D002_ComInfo :
         * D002_CostPrice : 606.0
         * D002_BuyPrice : 606.0
         * D002_BuyPoints : 0.0
         * D002_Quantity : 6
         * D002_Remark :
         * D002_IsAppraise : false
         * Status : 0
         * PrimaryKey : 442
         */

        private List<OrderDetailBean> OrderDetail;

        public String getD001_OrderNo() {
            return D001_OrderNo;
        }

        public void setD001_OrderNo(String D001_OrderNo) {
            this.D001_OrderNo = D001_OrderNo;
        }

        public String getD001_OrderDate() {
            return D001_OrderDate;
        }

        public void setD001_OrderDate(String D001_OrderDate) {
            this.D001_OrderDate = D001_OrderDate;
        }

        public Object getD001_BargainTime() {
            return D001_BargainTime;
        }

        public void setD001_BargainTime(Object D001_BargainTime) {
            this.D001_BargainTime = D001_BargainTime;
        }

        public String getD001_Buyer() {
            return D001_Buyer;
        }

        public void setD001_Buyer(String D001_Buyer) {
            this.D001_Buyer = D001_Buyer;
        }

        public String getD001_Saler() {
            return D001_Saler;
        }

        public void setD001_Saler(String D001_Saler) {
            this.D001_Saler = D001_Saler;
        }

        public double getD001_Sum() {
            return D001_Sum;
        }

        public void setD001_Sum(double D001_Sum) {
            this.D001_Sum = D001_Sum;
        }

        public double getD001_Points() {
            return D001_Points;
        }

        public void setD001_Points(double D001_Points) {
            this.D001_Points = D001_Points;
        }

        public double getD001_Postage() {
            return D001_Postage;
        }

        public void setD001_Postage(double D001_Postage) {
            this.D001_Postage = D001_Postage;
        }

        public String getD001_BayerLeaveWord() {
            return D001_BayerLeaveWord;
        }

        public void setD001_BayerLeaveWord(String D001_BayerLeaveWord) {
            this.D001_BayerLeaveWord = D001_BayerLeaveWord;
        }

        public double getD001_SendPoint() {
            return D001_SendPoint;
        }

        public void setD001_SendPoint(double D001_SendPoint) {
            this.D001_SendPoint = D001_SendPoint;
        }

        public double getD001_SendScore() {
            return D001_SendScore;
        }

        public void setD001_SendScore(double D001_SendScore) {
            this.D001_SendScore = D001_SendScore;
        }

        public String getD001_Remark() {
            return D001_Remark;
        }

        public void setD001_Remark(String D001_Remark) {
            this.D001_Remark = D001_Remark;
        }

        public String getD001_OrderClassCode() {
            return D001_OrderClassCode;
        }

        public void setD001_OrderClassCode(String D001_OrderClassCode) {
            this.D001_OrderClassCode = D001_OrderClassCode;
        }

        public String getD001_OrderStatusCode() {
            return D001_OrderStatusCode;
        }

        public void setD001_OrderStatusCode(String D001_OrderStatusCode) {
            this.D001_OrderStatusCode = D001_OrderStatusCode;
        }

        public boolean isD001_IsAppraise() {
            return D001_IsAppraise;
        }

        public void setD001_IsAppraise(boolean D001_IsAppraise) {
            this.D001_IsAppraise = D001_IsAppraise;
        }

        public List<OrderDetailBean> getOrderDetail() {
            return OrderDetail;
        }

        public void setOrderDetail(List<OrderDetailBean> OrderDetail) {
            this.OrderDetail = OrderDetail;
        }

        public static class OrderDetailBean implements Serializable {
            private int D002_OrderDetailID;
            private String D002_OrderNo;
            private String D002_OrderDate;
            private int D002_ComID;
            private String D002_ComTitle;
            private String D002_OptionIDCombin;
            private String D002_OptionIDCombinRemark;
            private String D002_ComPicUrl;
            private String D002_ComInfo;
            private double D002_CostPrice;
            private double D002_BuyPrice;
            private double D002_BuyPoints;
            private int D002_Quantity;
            private String D002_Remark;
            private boolean D002_IsAppraise;
            private int Status;
            private int PrimaryKey;

            public int getD002_OrderDetailID() {
                return D002_OrderDetailID;
            }

            public void setD002_OrderDetailID(int D002_OrderDetailID) {
                this.D002_OrderDetailID = D002_OrderDetailID;
            }

            public String getD002_OrderNo() {
                return D002_OrderNo;
            }

            public void setD002_OrderNo(String D002_OrderNo) {
                this.D002_OrderNo = D002_OrderNo;
            }

            public String getD002_OrderDate() {
                return D002_OrderDate;
            }

            public void setD002_OrderDate(String D002_OrderDate) {
                this.D002_OrderDate = D002_OrderDate;
            }

            public int getD002_ComID() {
                return D002_ComID;
            }

            public void setD002_ComID(int D002_ComID) {
                this.D002_ComID = D002_ComID;
            }

            public String getD002_ComTitle() {
                return D002_ComTitle;
            }

            public void setD002_ComTitle(String D002_ComTitle) {
                this.D002_ComTitle = D002_ComTitle;
            }

            public String getD002_OptionIDCombin() {
                return D002_OptionIDCombin;
            }

            public void setD002_OptionIDCombin(String D002_OptionIDCombin) {
                this.D002_OptionIDCombin = D002_OptionIDCombin;
            }

            public String getD002_OptionIDCombinRemark() {
                return D002_OptionIDCombinRemark;
            }

            public void setD002_OptionIDCombinRemark(String D002_OptionIDCombinRemark) {
                this.D002_OptionIDCombinRemark = D002_OptionIDCombinRemark;
            }

            public String getD002_ComPicUrl() {
                return D002_ComPicUrl;
            }

            public void setD002_ComPicUrl(String D002_ComPicUrl) {
                this.D002_ComPicUrl = D002_ComPicUrl;
            }

            public String getD002_ComInfo() {
                return D002_ComInfo;
            }

            public void setD002_ComInfo(String D002_ComInfo) {
                this.D002_ComInfo = D002_ComInfo;
            }

            public double getD002_CostPrice() {
                return D002_CostPrice;
            }

            public void setD002_CostPrice(double D002_CostPrice) {
                this.D002_CostPrice = D002_CostPrice;
            }

            public double getD002_BuyPrice() {
                return D002_BuyPrice;
            }

            public void setD002_BuyPrice(double D002_BuyPrice) {
                this.D002_BuyPrice = D002_BuyPrice;
            }

            public double getD002_BuyPoints() {
                return D002_BuyPoints;
            }

            public void setD002_BuyPoints(double D002_BuyPoints) {
                this.D002_BuyPoints = D002_BuyPoints;
            }

            public int getD002_Quantity() {
                return D002_Quantity;
            }

            public void setD002_Quantity(int D002_Quantity) {
                this.D002_Quantity = D002_Quantity;
            }

            public String getD002_Remark() {
                return D002_Remark;
            }

            public void setD002_Remark(String D002_Remark) {
                this.D002_Remark = D002_Remark;
            }

            public boolean isD002_IsAppraise() {
                return D002_IsAppraise;
            }

            public void setD002_IsAppraise(boolean D002_IsAppraise) {
                this.D002_IsAppraise = D002_IsAppraise;
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
        }
    }
}
