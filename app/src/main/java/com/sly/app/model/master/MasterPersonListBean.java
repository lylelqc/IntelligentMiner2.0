package com.sly.app.model.master;

public class MasterPersonListBean {

    /**
     * \"PersonSysCode\":\"a2c8db01-8e4e-478f-befd-729ee30de565\",
     \"PersonCode\":\"a2c8db01-8e4e-478f-befd-729ee30de565\",
     \"MineName\":\"班组-1\",
     \"Parent\":null,
     \"ClassCode\":\"7298e810-6cdf-447b-9835-07d4f293e63e\",
     \"ClassName\":\"班组长\",
     \"Grade\":2
     */

    private String PersonSysCode;
    private String PersonCode;
    private String MineName;
    private String Parent;
    private String ClassCode;
    private String ClassName;
    private int Grade;

    public String getPersonSysCode() {
        return PersonSysCode;
    }

    public void setPersonSysCode(String personSysCode) {
        PersonSysCode = personSysCode;
    }

    public String getPersonCode() {
        return PersonCode;
    }

    public void setPersonCode(String personCode) {
        PersonCode = personCode;
    }

    public String getMineName() {
        return MineName;
    }

    public void setMineName(String mineName) {
        MineName = mineName;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String classCode) {
        ClassCode = classCode;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }
}
