/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.model;

/**
 * 文 件 名: AssociationBean
 * 创 建 人: By k
 * 创建日期: 2017/10/26 15:54
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class AssociationBean {

    /**
     * MemberCode : 13100000002
     * EndDate : 2017/6/22 21:38:26
     * BuySum : 5237.00
     * Imageurl : http://202.101.233.167:8083/Upload//wd_zwt1.png
     * NickName :
     */

    private String MemberCode;
    private String EndDate;
    private String BuySum;
    private String Imageurl;
    private String NickName;

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String MemberCode) {
        this.MemberCode = MemberCode;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getBuySum() {
        return BuySum;
    }

    public void setBuySum(String BuySum) {
        this.BuySum = BuySum;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String Imageurl) {
        this.Imageurl = Imageurl;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public AssociationBean(String memberCode, String endDate, String buySum, String imageurl, String nickName) {
        MemberCode = memberCode;
        EndDate = endDate;
        BuySum = buySum;
        Imageurl = imageurl;
        NickName = nickName;
    }
    public AssociationBean(){
    }
}
