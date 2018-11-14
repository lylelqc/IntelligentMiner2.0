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

/**
 * 文 件 名: ShareRecord
 * 创 建 人: By k
 * 创建日期: 2017/11/4 17:33
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class ShareRecord {


    /**
     * MemberCode : V6555967
     * MemberLeveName : VIP会员
     * RegisterTime : 2017-12-11
     * ImageURl : http://117.48.196.62:9899//Adminmanager/Themes/img/jw_logo_128.png
     */

    private String MemberCode;
    private String MemberLeveName;
    private String RegisterTime;
    private String ImageURl;

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String MemberCode) {
        this.MemberCode = MemberCode;
    }

    public String getMemberLeveName() {
        return MemberLeveName;
    }

    public void setMemberLeveName(String MemberLeveName) {
        this.MemberLeveName = MemberLeveName;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String RegisterTime) {
        this.RegisterTime = RegisterTime;
    }

    public String getImageURl() {
        return ImageURl;
    }

    public void setImageURl(String ImageURl) {
        this.ImageURl = ImageURl;
    }
}
