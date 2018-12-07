/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.model;

import java.util.List;

/**
 * 文 件 名: UserGroupTeam
 * 创 建 人: By k
 * 创建日期: 2017/12/15 10:56
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class UserGroup {

    /**
     * MemberCode : null
     * BeginDate : null
     * EndDate : null
     * PageNo : null
     * PageSize : null
     * TotalCount : 3
     * Sum : 132
     * qun : [{"Buyer":null,"OrderNo":null,"OrderDate":null,"OrderSum":null,"StatusName":null,"MemberCode":"V8955979","BonusTime":"2017/12/18 12:20:15","Bonus":"10.0000","BonusStatus":"未发放","RowNo":null},{"Buyer":null,"OrderNo":null,"OrderDate":null,"OrderSum":null,"StatusName":null,"MemberCode":"V8955979","BonusTime":"2017/12/18 12:20:15","Bonus":"120.0000","BonusStatus":"未发放","RowNo":null},{"Buyer":null,"OrderNo":null,"OrderDate":null,"OrderSum":null,"StatusName":null,"MemberCode":"V8955979","BonusTime":"2017/12/18 11:58:11","Bonus":"2.0000","BonusStatus":"未发放","RowNo":null}]
     */

    private String MemberCode;
    private String BeginDate;
    private String EndDate;
    private String PageNo;
    private String PageSize;
    private String TotalCount;
    private String Sum;
    private List<QunBean> qun;

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String MemberCode) {
        this.MemberCode = MemberCode;
    }

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String BeginDate) {
        this.BeginDate = BeginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getPageNo() {
        return PageNo;
    }

    public void setPageNo(String PageNo) {
        this.PageNo = PageNo;
    }

    public String getPageSize() {
        return PageSize;
    }

    public void setPageSize(String PageSize) {
        this.PageSize = PageSize;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String TotalCount) {
        this.TotalCount = TotalCount;
    }

    public String getSum() {
        return Sum;
    }

    public void setSum(String Sum) {
        this.Sum = Sum;
    }

    public List<QunBean> getQun() {
        return qun;
    }

    public void setQun(List<QunBean> qun) {
        this.qun = qun;
    }

    public static class QunBean {
        /**
         * Buyer : null
         * OrderNo : null
         * OrderDate : null
         * OrderSum : null
         * StatusName : null
         * MemberCode : V8955979
         * BonusTime : 2017/12/18 12:20:15
         * Bonus : 10.0000
         * BonusStatus : 未发放
         * RowNo : null
         */

        private String Buyer;
        private String OrderNo;
        private String OrderDate;
        private String OrderSum;
        private String StatusName;
        private String MemberCode;
        private String BonusTime;
        private String Bonus;
        private String BonusStatus;
        private String RowNo;

        public String getBuyer() {
            return Buyer;
        }

        public void setBuyer(String Buyer) {
            this.Buyer = Buyer;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String OrderDate) {
            this.OrderDate = OrderDate;
        }

        public String getOrderSum() {
            return OrderSum;
        }

        public void setOrderSum(String OrderSum) {
            this.OrderSum = OrderSum;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String StatusName) {
            this.StatusName = StatusName;
        }

        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String MemberCode) {
            this.MemberCode = MemberCode;
        }

        public String getBonusTime() {
            return BonusTime;
        }

        public void setBonusTime(String BonusTime) {
            this.BonusTime = BonusTime;
        }

        public String getBonus() {
            return Bonus;
        }

        public void setBonus(String Bonus) {
            this.Bonus = Bonus;
        }

        public String getBonusStatus() {
            return BonusStatus;
        }

        public void setBonusStatus(String BonusStatus) {
            this.BonusStatus = BonusStatus;
        }

        public String getRowNo() {
            return RowNo;
        }

        public void setRowNo(String RowNo) {
            this.RowNo = RowNo;
        }
    }
}
