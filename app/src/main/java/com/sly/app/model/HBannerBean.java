package com.sly.app.model;

/**
 * 作者 by K
 * 时间：on 2017/9/4 11:45
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class HBannerBean {

    /**
     * M070_adId : 1
     * M070_imageUrl : http://117.48.196.62:9896/Uploads/Img/MemberPhoto/banner1.jpg
     * M070_type : 首页
     * M070_dateTime : 2017-12-09 12:35:17
     * M070_Link :
     * Status : 1
     * PrimaryKey : 1
     */

    private int M070_adId;
    private String M070_imageUrl;
    private String M070_type;
    private String M070_dateTime;
    private String M070_Link;
    private int Status;
    private int PrimaryKey;

    public int getM070_adId() {
        return M070_adId;
    }

    public void setM070_adId(int M070_adId) {
        this.M070_adId = M070_adId;
    }

    public String getM070_imageUrl() {
        return M070_imageUrl;
    }

    public void setM070_imageUrl(String M070_imageUrl) {
        this.M070_imageUrl = M070_imageUrl;
    }

    public String getM070_type() {
        return M070_type;
    }

    public void setM070_type(String M070_type) {
        this.M070_type = M070_type;
    }

    public String getM070_dateTime() {
        return M070_dateTime;
    }

    public void setM070_dateTime(String M070_dateTime) {
        this.M070_dateTime = M070_dateTime;
    }

    public String getM070_Link() {
        return M070_Link;
    }

    public void setM070_Link(String M070_Link) {
        this.M070_Link = M070_Link;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getPrimaryKey() {
        return PrimaryKey;
    }

    public void setPrimaryKey(int PrimaryKey) {
        this.PrimaryKey = PrimaryKey;
    }
}
