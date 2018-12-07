package com.sly.app.model;

import io.realm.RealmObject;

/**
 * 作者 by K
 * 时间：on 2017/9/19 19:33
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class UserInfoBean extends RealmObject {

    /**
     * MemberCode : V15772747952
     * LoginPassword : null
     * Name :
     * MemberLeveName : 普通会员
     * Discount : 0
     * PruseCode : null
     * LoginType : null
     * RegisterType : null
     * RegisterTime : 2017/12/11
     * ImageURl : http://117.48.196.62:9899//Adminmanager/Themes/img/jw_logo_128.png
     * Pv : 0.0000
     * phone : 15772747952
     * BirthDay : 2017/7/6 10:41:20
     * Sex : Unknow
     * MemberLeveCode : 3
     * EmployeeLevelCode : 1
     * EmployeeLevel : 无
     * Province : null
     * City : null
     * County : null
     * ParperNo :
     */

    private String MemberCode;
    private String LoginPassword;
    private String Name;
    private String MemberLeveName;
    private int Discount;
    private String PruseCode;
    private String LoginType;
    private String RegisterType;
    private String RegisterTime;
    private String ImageURl;
    private String Pv;
    private String phone;
    private String BirthDay;
    private String Sex;
    private String MemberLeveCode;
    private String EmployeeLevelCode;
    private String EmployeeLevel;
    private String Province;
    private String City;
    private String County;
    private String ParperNo;

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String MemberCode) {
        this.MemberCode = MemberCode;
    }

    public String getLoginPassword() {
        return LoginPassword;
    }

    public void setLoginPassword(String LoginPassword) {
        this.LoginPassword = LoginPassword;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMemberLeveName() {
        return MemberLeveName;
    }

    public void setMemberLeveName(String MemberLeveName) {
        this.MemberLeveName = MemberLeveName;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int Discount) {
        this.Discount = Discount;
    }

    public String getPruseCode() {
        return PruseCode;
    }

    public void setPruseCode(String PruseCode) {
        this.PruseCode = PruseCode;
    }

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String LoginType) {
        this.LoginType = LoginType;
    }

    public String getRegisterType() {
        return RegisterType;
    }

    public void setRegisterType(String RegisterType) {
        this.RegisterType = RegisterType;
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

    public String getPv() {
        return Pv;
    }

    public void setPv(String Pv) {
        this.Pv = Pv;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(String BirthDay) {
        this.BirthDay = BirthDay;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getMemberLeveCode() {
        return MemberLeveCode;
    }

    public void setMemberLeveCode(String MemberLeveCode) {
        this.MemberLeveCode = MemberLeveCode;
    }

    public String getEmployeeLevelCode() {
        return EmployeeLevelCode;
    }

    public void setEmployeeLevelCode(String EmployeeLevelCode) {
        this.EmployeeLevelCode = EmployeeLevelCode;
    }

    public String getEmployeeLevel() {
        return EmployeeLevel;
    }

    public void setEmployeeLevel(String EmployeeLevel) {
        this.EmployeeLevel = EmployeeLevel;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String County) {
        this.County = County;
    }

    public String getParperNo() {
        return ParperNo;
    }

    public void setParperNo(String ParperNo) {
        this.ParperNo = ParperNo;
    }
}
