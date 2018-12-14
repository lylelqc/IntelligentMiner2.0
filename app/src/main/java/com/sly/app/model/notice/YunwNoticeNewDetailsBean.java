package com.sly.app.model.notice;

public class YunwNoticeNewDetailsBean {

    /**
     * \"Mine48_Title\":\"矿场区域(b3_3)可能断电或者服务器宕机\",
     \"Mine48_Time\":\"2018-11-23 02:30:00\",
     \"Mine48_Message\":\"矿场区域(b3_3)可能断电或者服务器宕机\",
     \"Mine48_BillNo\":\"miner_test_M666\",
     \"Mine08_MineMachineSysCode\":null,
     \"Mine08_IP\":null,
     \"Mine08_Model\":null,
     \"isShow\":\"False\"}
     */

    private String Mine48_Title;
    private String Mine48_Time;
    private String Mine48_Message;
    private String Mine48_BillNo;
    private String Mine08_MineMachineSysCode;
    private String Mine08_IP;
    private String Mine08_Model;
    private String isShow;

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

    public String getMine08_MineMachineSysCode() {
        return Mine08_MineMachineSysCode;
    }

    public void setMine08_MineMachineSysCode(String mine08_MineMachineSysCode) {
        Mine08_MineMachineSysCode = mine08_MineMachineSysCode;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
