package com.sly.app.model.notice;

public class YunwNoticeNewDetailsBean {

    /**
     * \"Mine48_Title\":\"矿机故障\",
     \"Mine48_Time\":\"2018-11-20 14:12:47\",
     \"Mine48_Message\":\"设备异常:网络异常。IP:2.1.8.4,
     型号为:蚂蚁S9,所在区域为:B,当前算力为:0.0000000000T\",
     \"Mine48_BillNo\":\"2018112000000083\",
     \"Mine08_IP\":\"2.1.8.4\",
     \"Mine08_Model\":\"52:C1:85:4E:72:1B\"
     */

    private String Mine48_Title;
    private String Mine48_Time;
    private String Mine48_Message;
    private String Mine48_BillNo;
    private String Mine08_IP;
    private String Mine08_Model;

    public String getMine48_Title() {
        return Mine48_Title;
    }

    public void setMine48_Title(String mine48_Title) {
        Mine48_Title = mine48_Title;
    }

    public String getMine48_Time() {
        return Mine48_Time;
    }

    public void setMine48_Time(String mine48_Time) {
        Mine48_Time = mine48_Time;
    }

    public String getMine48_Message() {
        return Mine48_Message;
    }

    public void setMine48_Message(String mine48_Message) {
        Mine48_Message = mine48_Message;
    }

    public String getMine48_BillNo() {
        return Mine48_BillNo;
    }

    public void setMine48_BillNo(String mine48_BillNo) {
        Mine48_BillNo = mine48_BillNo;
    }

    public String getMine08_IP() {
        return Mine08_IP;
    }

    public void setMine08_IP(String mine08_IP) {
        Mine08_IP = mine08_IP;
    }

    public String getMine08_Model() {
        return Mine08_Model;
    }

    public void setMine08_Model(String mine08_Model) {
        Mine08_Model = mine08_Model;
    }
}
