package com.sly.app.model.sly;

public class JPushGetIDBean {

    /**
     "uid": "1",
     "username": "12154545",
     "name": "吴系挂",
     "groupid": 2 ,
     "reg_time": "1436864169",
     "last_login_time": "0",**/

    private String uid;
    private String username;
    private int groupid;
    private String reg_time;
    private String last_login_time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }
}
