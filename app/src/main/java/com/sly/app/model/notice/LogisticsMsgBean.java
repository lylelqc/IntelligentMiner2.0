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
package com.sly.app.model.notice;

/**
 * 文 件 名: LogisticsMsgBean
 * 创 建 人: By k
 * 创建日期: 2017/11/7 19:38
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class LogisticsMsgBean   {
    /**
     * MemberMessageClassCode : 03
     * TransPort : 518378276777
     * LogisticsNumber : 518378276777
     * Time : 2017-08-30 17:05:55
     * Title : 氨基酸片
     * Picurl : http://202.101.233.167:8083/Upload/4df30074bc81.jpg
     * content : 订单已签收
     * quantity : 2
     */

    private String MemberMessageClassCode;
    private String TransPort;
    private String LogisticsNumber;
    private String Time;
    private String Title;
    private String Picurl;
    private String content;
    private String quantity;
    private String OrderNo;

    public String getMemberMessageClassCode() {
        return MemberMessageClassCode;
    }

    public void setMemberMessageClassCode(String memberMessageClassCode) {
        MemberMessageClassCode = memberMessageClassCode;
    }

    public String getTransPort() {
        return TransPort;
    }

    public void setTransPort(String transPort) {
        TransPort = transPort;
    }

    public String getLogisticsNumber() {
        return LogisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        LogisticsNumber = logisticsNumber;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPicurl() {
        return Picurl;
    }

    public void setPicurl(String picurl) {
        Picurl = picurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
}
