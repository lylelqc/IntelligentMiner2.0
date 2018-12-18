package com.sly.app.model.notice;

public class MinerNoticeNewDetailsBean {

    /**
     * \"Mine34_Title\":\"维修单\",
     \"Mine34_Time\":\"2018-12-11 16:33:15\",
     \"Mine34_Message\":\"您有一张维修单已报价，请查收！\",
     \"Mine34_BillNo\":null,
     \"Mine08_MineMachineSysCode\":null,
     \"Mine08_IP\":null,
     \"Mine08_Model\":null,
     \"Mine08_MineCode\":null,
     \"isShow\":\"False\"
     */

    private String Mine34_Title;
    private String Mine34_Time;
    private String Mine34_Message;
    private String Mine34_BillNo;
    private String Mine08_MineMachineSysCode;
    private String Mine08_IP;
    private String Mine08_Model;
    private String Mine08_MineCode;
    private String isShow;

    public String getMine34_Title() {
        return Mine34_Title;
    }

    public void setMine34_Title(String mine34_Title) {
        Mine34_Title = mine34_Title;
    }

    public String getMine34_Time() {
        return Mine34_Time;
    }

    public void setMine34_Time(String mine34_Time) {
        Mine34_Time = mine34_Time;
    }

    public String getMine34_Message() {
        return Mine34_Message;
    }

    public void setMine34_Message(String mine34_Message) {
        Mine34_Message = mine34_Message;
    }

    public String getMine34_BillNo() {
        return Mine34_BillNo;
    }

    public void setMine34_BillNo(String mine34_BillNo) {
        Mine34_BillNo = mine34_BillNo;
    }

    public String getMine08_MineMachineSysCode() {
        return Mine08_MineMachineSysCode;
    }

    public void setMine08_MineMachineSysCode(String mine08_MineMachineSysCode) {
        Mine08_MineMachineSysCode = mine08_MineMachineSysCode;
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

    public String getMine08_MineCode() {
        return Mine08_MineCode;
    }

    public void setMine08_MineCode(String mine08_MineCode) {
        Mine08_MineCode = mine08_MineCode;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
