package com.sly.app.model;

import java.util.List;

public class DeviceBean {
    private String groupName;
    private boolean isExpand = true;
    private DeviceChildBean mChildBean;
    private List<DeviceChildBean> mChildBeans;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<DeviceChildBean> getChildBeans() {
        return mChildBeans;
    }

    public void setChildBeans(List<DeviceChildBean> childBeans) {
        mChildBeans = childBeans;
    }

    public DeviceChildBean getChildBean() {
        return mChildBean;
    }

    public void setChildBean(DeviceChildBean childBean) {
        mChildBean = childBean;
    }

    public static class DeviceChildBean {
        private String childName;
        private Class mHelpClass;
        private boolean isStudy;

        public DeviceChildBean() {
        }

        public DeviceChildBean(String childName, Class helpClass) {
            this.childName = childName;
            mHelpClass = helpClass;
        }

        public DeviceChildBean(String childName, Class helpClass, boolean isStudy) {
            this.childName = childName;
            mHelpClass = helpClass;
            this.isStudy = isStudy;
        }

        public Class getHelpClass() {
            return mHelpClass;
        }
        public void setHelpClass(Class helpClass) {
            mHelpClass = helpClass;
        }
        public boolean isStudy() {
            return isStudy;
        }

        public void setStudy(boolean study) {
            isStudy = study;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }
}
