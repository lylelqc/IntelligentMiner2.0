package com.sly.app.model;

import java.util.List;

/**
 * Created by 01 on 2016/11/3.
 */
public class CityBean {


    private RECORDSBean RECORDS;

    public RECORDSBean getRECORDS() {
        return RECORDS;
    }

    public void setRECORDS(RECORDSBean RECORDS) {
        this.RECORDS = RECORDS;
    }

    public static class RECORDSBean {
        /**
         * RegionalismCode : 110000
         * RegionalismName : 北京市
         * Grade : 1
         */

        private List<RECORDBean> RECORD;

        public List<RECORDBean> getRECORD() {
            return RECORD;
        }

        public void setRECORD(List<RECORDBean> RECORD) {
            this.RECORD = RECORD;
        }

        public static class RECORDBean {

            /**
             * RegionalismCode : 110100
             * ParentCode : 110000
             * RegionalismName : 市辖区
             * Grade : 2
             */

            private String RegionalismCode;
            private String ParentCode;
            private String RegionalismName;
            private String Grade;


            public String getRegionalismCode() {
                return RegionalismCode;
            }

            public void setRegionalismCode(String RegionalismCode) {
                this.RegionalismCode = RegionalismCode;
            }

            public String getParentCode() {
                return ParentCode;
            }

            public void setParentCode(String ParentCode) {
                this.ParentCode = ParentCode;
            }

            public String getRegionalismName() {
                return RegionalismName;
            }

            public void setRegionalismName(String RegionalismName) {
                this.RegionalismName = RegionalismName;
            }

            public String getGrade() {
                return Grade;
            }

            public void setGrade(String Grade) {
                this.Grade = Grade;
            }

            @Override
            public String toString() {
                return "RECORDBean{" +
                        "RegionalismCode='" + RegionalismCode + '\'' +
                        ", ParentCode='" + ParentCode + '\'' +
                        ", RegionalismName='" + RegionalismName + '\'' +
                        ", Grade='" + Grade + '\'' +
                        '}';
            }
        }
    }
}
