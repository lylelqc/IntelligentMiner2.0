package com.sly.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplaceListBean implements Parcelable {
    /**
     * RowNo : 1
     * BillNo : 1CA99340-682F-42E5-8508-BD2BABB43FDC
     * BillTime : 2018-08-20 19:13:25
     * BillStatus : 处理中
     */

    private int RowNo;
    private String BillNo;
    private String BillTime;
    private String BillStatus;
    private String Reason;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getRowNo() {
        return RowNo;
    }

    public void setRowNo(int RowNo) {
        this.RowNo = RowNo;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String BillNo) {
        this.BillNo = BillNo;
    }

    public String getBillTime() {
        return BillTime;
    }

    public void setBillTime(String BillTime) {
        this.BillTime = BillTime;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String BillStatus) {
        this.BillStatus = BillStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(RowNo);
        parcel.writeString(BillNo);
        parcel.writeString(BillTime);
        parcel.writeString(BillStatus);
        parcel.writeString(Reason);

    }
    public ReplaceListBean() {
    }

    protected ReplaceListBean(Parcel in) {
        RowNo = in.readInt();
        BillNo = in.readString();
        BillTime = in.readString();
        BillStatus = in.readString();
        Reason = in.readString();
    }

    public static final Creator<ReplaceListBean> CREATOR = new Creator<ReplaceListBean>() {
        @Override
        public ReplaceListBean createFromParcel(Parcel in) {
            return new ReplaceListBean(in);
        }

        @Override
        public ReplaceListBean[] newArray(int size) {
            return new ReplaceListBean[size];
        }
    };
//    RowNo 行号
////    BillNo 维修单号
////    BillTime 单号时间
////    BillStatus 状态




}
