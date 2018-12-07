package com.sly.app.model;

import java.util.List;

public class pcc {

    /**
     * ProvinceCode : 110000
     * ProvinceName : 北京
     * City : [{"CityCode":"110100","CityName":"北京市","CityParentCode":"110000","Area":[{"AreaCode":"110101","AreaName":"东城区","AreaParentCode":"110100"},{"AreaCode":"110102","AreaName":"西城区","AreaParentCode":"110100"},{"AreaCode":"110105","AreaName":"朝阳区","AreaParentCode":"110100"},{"AreaCode":"110106","AreaName":"丰台区","AreaParentCode":"110100"},{"AreaCode":"110107","AreaName":"石景山区","AreaParentCode":"110100"},{"AreaCode":"110108","AreaName":"海淀区","AreaParentCode":"110100"},{"AreaCode":"110109","AreaName":"门头沟区","AreaParentCode":"110100"},{"AreaCode":"110111","AreaName":"房山区","AreaParentCode":"110100"},{"AreaCode":"110112","AreaName":"通州区","AreaParentCode":"110100"},{"AreaCode":"110113","AreaName":"顺义区","AreaParentCode":"110100"},{"AreaCode":"110114","AreaName":"昌平区","AreaParentCode":"110100"},{"AreaCode":"110115","AreaName":"大兴区","AreaParentCode":"110100"},{"AreaCode":"110116","AreaName":"怀柔区","AreaParentCode":"110100"},{"AreaCode":"110117","AreaName":"平谷区","AreaParentCode":"110100"},{"AreaCode":"110228","AreaName":"密云县","AreaParentCode":"110100"},{"AreaCode":"110229","AreaName":"延庆县","AreaParentCode":"110100"}]}]
     */

    private String ProvinceCode;
    private String ProvinceName;
    private List<CityBean> City;

    public String getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(String ProvinceCode) {
        this.ProvinceCode = ProvinceCode;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public List<CityBean> getCity() {
        return City;
    }

    public void setCity(List<CityBean> City) {
        this.City = City;
    }

    public static class CityBean {
        /**
         * CityCode : 110100
         * CityName : 北京市
         * CityParentCode : 110000
         * Area : [{"AreaCode":"110101","AreaName":"东城区","AreaParentCode":"110100"},{"AreaCode":"110102","AreaName":"西城区","AreaParentCode":"110100"},{"AreaCode":"110105","AreaName":"朝阳区","AreaParentCode":"110100"},{"AreaCode":"110106","AreaName":"丰台区","AreaParentCode":"110100"},{"AreaCode":"110107","AreaName":"石景山区","AreaParentCode":"110100"},{"AreaCode":"110108","AreaName":"海淀区","AreaParentCode":"110100"},{"AreaCode":"110109","AreaName":"门头沟区","AreaParentCode":"110100"},{"AreaCode":"110111","AreaName":"房山区","AreaParentCode":"110100"},{"AreaCode":"110112","AreaName":"通州区","AreaParentCode":"110100"},{"AreaCode":"110113","AreaName":"顺义区","AreaParentCode":"110100"},{"AreaCode":"110114","AreaName":"昌平区","AreaParentCode":"110100"},{"AreaCode":"110115","AreaName":"大兴区","AreaParentCode":"110100"},{"AreaCode":"110116","AreaName":"怀柔区","AreaParentCode":"110100"},{"AreaCode":"110117","AreaName":"平谷区","AreaParentCode":"110100"},{"AreaCode":"110228","AreaName":"密云县","AreaParentCode":"110100"},{"AreaCode":"110229","AreaName":"延庆县","AreaParentCode":"110100"}]
         */

        private String CityCode;
        private String CityName;
        private String CityParentCode;
        private List<AreaBean> Area;

        public String getCityCode() {
            return CityCode;
        }

        public void setCityCode(String CityCode) {
            this.CityCode = CityCode;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }

        public String getCityParentCode() {
            return CityParentCode;
        }

        public void setCityParentCode(String CityParentCode) {
            this.CityParentCode = CityParentCode;
        }

        public List<AreaBean> getArea() {
            return Area;
        }

        public void setArea(List<AreaBean> Area) {
            this.Area = Area;
        }

        public static class AreaBean {
            /**
             * AreaCode : 110101
             * AreaName : 东城区
             * AreaParentCode : 110100
             */

            private String AreaCode;
            private String AreaName;
            private String AreaParentCode;

            public String getAreaCode() {
                return AreaCode;
            }

            public void setAreaCode(String AreaCode) {
                this.AreaCode = AreaCode;
            }

            public String getAreaName() {
                return AreaName;
            }

            public void setAreaName(String AreaName) {
                this.AreaName = AreaName;
            }

            public String getAreaParentCode() {
                return AreaParentCode;
            }

            public void setAreaParentCode(String AreaParentCode) {
                this.AreaParentCode = AreaParentCode;
            }
        }
    }
}
