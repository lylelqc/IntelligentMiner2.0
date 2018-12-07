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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文 件 名: MsgBean
 * 创 建 人: By k
 * 创建日期: 2017/12/25 16:41
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class MsgBean implements Parcelable {


    /**
     * ID : 13
     * ClassID : 10
     * ClassName : 新闻
     * Title : 好消息！邀请好友注册享丰厚待遇！！1
     * Intro : 美好地球村APP用户分享邀请链接或者邀请二维码享受官方提供的各种好礼！！！
     * logo : http://58.64.137.184:8081/UploadThumbnail40-40ce9397df-bfff-4c6b-beb4-45e75ba3302a.png
     * AddTime : 2017-12-27 16:57:19
     * Url : #
     * IsRecommend : true
     * ClickCount : 0
     * Pci1 :
     * Pci2 :
     * Pci3 :
     */

    private int ID;
    private int ClassID;
    private String ClassName;
    private String Title;
    private String Intro;
    private String logo;
    private String AddTime;
    private String Url;
    private boolean IsRecommend;
    private int ClickCount;
    private String Pci1;
    private String Pci2;
    private String Pci3;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getIntro() {
        return Intro;
    }

    public void setIntro(String Intro) {
        this.Intro = Intro;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public boolean isIsRecommend() {
        return IsRecommend;
    }

    public void setIsRecommend(boolean IsRecommend) {
        this.IsRecommend = IsRecommend;
    }

    public int getClickCount() {
        return ClickCount;
    }

    public void setClickCount(int ClickCount) {
        this.ClickCount = ClickCount;
    }

    public String getPci1() {
        return Pci1;
    }

    public void setPci1(String Pci1) {
        this.Pci1 = Pci1;
    }

    public String getPci2() {
        return Pci2;
    }

    public void setPci2(String Pci2) {
        this.Pci2 = Pci2;
    }

    public String getPci3() {
        return Pci3;
    }

    public void setPci3(String Pci3) {
        this.Pci3 = Pci3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeInt(this.ClassID);
        dest.writeString(this.ClassName);
        dest.writeString(this.Title);
        dest.writeString(this.Intro);
        dest.writeString(this.logo);
        dest.writeString(this.AddTime);
        dest.writeString(this.Url);
        dest.writeByte(this.IsRecommend ? (byte) 1 : (byte) 0);
        dest.writeInt(this.ClickCount);
        dest.writeString(this.Pci1);
        dest.writeString(this.Pci2);
        dest.writeString(this.Pci3);
    }

    public MsgBean() {
    }

    protected MsgBean(Parcel in) {
        this.ID = in.readInt();
        this.ClassID = in.readInt();
        this.ClassName = in.readString();
        this.Title = in.readString();
        this.Intro = in.readString();
        this.logo = in.readString();
        this.AddTime = in.readString();
        this.Url = in.readString();
        this.IsRecommend = in.readByte() != 0;
        this.ClickCount = in.readInt();
        this.Pci1 = in.readString();
        this.Pci2 = in.readString();
        this.Pci3 = in.readString();
    }

    public static final Creator<MsgBean> CREATOR = new Creator<MsgBean>() {
        @Override
        public MsgBean createFromParcel(Parcel source) {
            return new MsgBean(source);
        }

        @Override
        public MsgBean[] newArray(int size) {
            return new MsgBean[size];
        }
    };
}
