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
 * 文 件 名: AllNoticeAdapter
 * 创 建 人: By k
 * 创建日期: 2017/11/7 18:27
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class AllNoticeBean  {

    /**
     * MemberMessageClassCode : 02
     * TransPort : null
     * id : 6
     * LogisticsNumber : null
     * Time : 2017-11-07 17:20:36
     * Title : 您的100.0000提现审核未通过
     * Picurl : http://202.101.233.167:8083/Adminmanager/Themes/img/ptxx_notice_icon4_n.png
     * content : 失败
     * quantity : null
     * OrderNo : null
     * iseffective : 0
     */

    private String MemberMessageClassCode;
    private Object TransPort;
    private String id;
    private Object LogisticsNumber;
    private String Time;
    private String Title;
    private String Picurl;
    private String content;
    private Object quantity;
    private Object OrderNo;
    private String iseffective;

    public String getMemberMessageClassCode() {
        return MemberMessageClassCode;
    }

    public void setMemberMessageClassCode(String MemberMessageClassCode) {
        this.MemberMessageClassCode = MemberMessageClassCode;
    }

    public Object getTransPort() {
        return TransPort;
    }

    public void setTransPort(Object TransPort) {
        this.TransPort = TransPort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getLogisticsNumber() {
        return LogisticsNumber;
    }

    public void setLogisticsNumber(Object LogisticsNumber) {
        this.LogisticsNumber = LogisticsNumber;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPicurl() {
        return Picurl;
    }

    public void setPicurl(String Picurl) {
        this.Picurl = Picurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getQuantity() {
        return quantity;
    }

    public void setQuantity(Object quantity) {
        this.quantity = quantity;
    }

    public Object getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(Object OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getIseffective() {
        return iseffective;
    }

    public void setIseffective(String iseffective) {
        this.iseffective = iseffective;
    }
}
