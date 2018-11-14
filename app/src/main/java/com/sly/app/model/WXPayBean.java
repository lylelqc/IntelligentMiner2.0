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

import com.google.gson.annotations.SerializedName;

/**
 * 文 件 名: WXPayBean
 * 创 建 人: By k
 * 创建日期: 2017/10/10 16:42
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class WXPayBean {

    /**
     * appid : wx70bcff35ed6cf7b8
     * mch_id : 1489865882
     * out_trade_no : null
     * noncestr : eF0IQI2456PakRm6
     * prepayid : wx20171010164120041492332a0693455728
     * timestamp : 1507624882
     * sign : 69C4917B28B0FCB35ACE674416008171
     * package : Sign=WXPay
     */

    private String appid;
    private String mch_id;
    private Object out_trade_no;
    private String noncestr;
    private String prepayid;
    private String timestamp;
    private String sign;
    @SerializedName("package")
    private String packageX;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public Object getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(Object out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }
}
