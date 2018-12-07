package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

public class RepaireBean implements Parcelable {


    private String JSON_rowno;
    private String JSON_acount;
    private String JSON_mine25_processtime;
    private String JSON_mine24_stoppageinfo;
    private String JSON_mine25_repairsum;
    private String JSON_managtimehour;
    private String JSON_managtimemin;
    private String JSON_mine27_repairresultname;

    @Override
    public String toString() {
        return "RepaireBean{" +
                "JSON_rowno='" + JSON_rowno + '\'' +
                ", JSON_acount='" + JSON_acount + '\'' +
                ", JSON_mine25_processtime='" + JSON_mine25_processtime + '\'' +
                ", JSON_mine24_stoppageinfo='" + JSON_mine24_stoppageinfo + '\'' +
                ", JSON_mine25_repairsum='" + JSON_mine25_repairsum + '\'' +
                ", JSON_managtimehour='" + JSON_managtimehour + '\'' +
                ", JSON_managtimemin='" + JSON_managtimemin + '\'' +
                ", JSON_mine27_repairresultname='" + JSON_mine27_repairresultname + '\'' +
                '}';
    }

    public String getJSON_rowno() {
        return JSON_rowno;
    }

    public void setJSON_rowno(String JSON_rowno) {
        this.JSON_rowno = JSON_rowno;
    }

    public String getJSON_acount() {
        return JSON_acount;
    }

    public void setJSON_acount(String JSON_acount) {
        this.JSON_acount = JSON_acount;
    }

    public String getJSON_mine25_processtime() {
        return JSON_mine25_processtime;
    }

    public void setJSON_mine25_processtime(String JSON_mine25_processtime) {
        this.JSON_mine25_processtime = JSON_mine25_processtime;
    }

    public String getJSON_mine24_stoppageinfo() {
        return JSON_mine24_stoppageinfo;
    }

    public void setJSON_mine24_stoppageinfo(String JSON_mine24_stoppageinfo) {
        this.JSON_mine24_stoppageinfo = JSON_mine24_stoppageinfo;
    }

    public String getJSON_mine25_repairsum() {
        return JSON_mine25_repairsum;
    }

    public void setJSON_mine25_repairsum(String JSON_mine25_repairsum) {
        this.JSON_mine25_repairsum = JSON_mine25_repairsum;
    }

    public String getJSON_managtimehour() {
        return JSON_managtimehour;
    }

    public void setJSON_managtimehour(String JSON_managtimehour) {
        this.JSON_managtimehour = JSON_managtimehour;
    }

    public String getJSON_managtimemin() {
        return JSON_managtimemin;
    }

    public void setJSON_managtimemin(String JSON_managtimemin) {
        this.JSON_managtimemin = JSON_managtimemin;
    }

    public String getJSON_mine27_repairresultname() {
        return JSON_mine27_repairresultname;
    }

    public void setJSON_mine27_repairresultname(String JSON_mine27_repairresultname) {
        this.JSON_mine27_repairresultname = JSON_mine27_repairresultname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.JSON_rowno);
        dest.writeString(this.JSON_acount);
        dest.writeString(this.JSON_mine25_processtime);
        dest.writeString(this.JSON_mine24_stoppageinfo);
        dest.writeString(this.JSON_mine25_repairsum);
        dest.writeString(this.JSON_managtimehour);
        dest.writeString(this.JSON_managtimemin);
        dest.writeString(this.JSON_mine27_repairresultname);
    }

    public RepaireBean() {
    }

    protected RepaireBean(Parcel in) {
        this.JSON_rowno = in.readString();
        this.JSON_acount = in.readString();
        this.JSON_mine25_processtime = in.readString();
        this.JSON_mine24_stoppageinfo = in.readString();
        this.JSON_mine25_repairsum = in.readString();
        this.JSON_managtimehour = in.readString();
        this.JSON_managtimemin = in.readString();
        this.JSON_mine27_repairresultname = in.readString();
    }

    public static final Creator<RepaireBean> CREATOR = new Creator<RepaireBean>() {
        @Override
        public RepaireBean createFromParcel(Parcel source) {
            return new RepaireBean(source);
        }

        @Override
        public RepaireBean[] newArray(int size) {
            return new RepaireBean[size];
        }
    };
}
