package com.sly.app.model;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/7/26 09:45
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class pca {

    /**
     * ProvinceCode : 北京市
     * ProvinceName : 001
     * list : [{"CityCode":"东城区","CityName":"000061","CityParentCode":"001","mList":[{"AreaCode":"东城区","AreaName":"00000840","AreaParentCode":"000061"}]},{"CityCode":"西城区","CityName":"000062","CityParentCode":"001","mList":[{"AreaCode":"西城区","AreaName":"00000841","AreaParentCode":"000062"}]},{"CityCode":"崇文区","CityName":"000063","CityParentCode":"001","mList":[{"AreaCode":"崇文区","AreaName":"00000842","AreaParentCode":"000063"}]},{"CityCode":"宣武区","CityName":"000064","CityParentCode":"001","mList":[{"AreaCode":"宣武区","AreaName":"00000843","AreaParentCode":"000064"}]},{"CityCode":"朝阳区","CityName":"000065","CityParentCode":"001","mList":[{"AreaCode":"朝阳区","AreaName":"00000844","AreaParentCode":"000065"}]},{"CityCode":"丰台区","CityName":"000066","CityParentCode":"001","mList":[{"AreaCode":"丰台区","AreaName":"00000845","AreaParentCode":"000066"}]},{"CityCode":"石景山区","CityName":"000067","CityParentCode":"001","mList":[{"AreaCode":"石景山区","AreaName":"00000846","AreaParentCode":"000067"}]},{"CityCode":"海淀区","CityName":"000068","CityParentCode":"001","mList":[{"AreaCode":"海淀区","AreaName":"00000847","AreaParentCode":"000068"}]},{"CityCode":"门头沟区","CityName":"000069","CityParentCode":"001","mList":[{"AreaCode":"门头沟区","AreaName":"00000848","AreaParentCode":"000069"}]},{"CityCode":"房山区","CityName":"000070","CityParentCode":"001","mList":[{"AreaCode":"房山区","AreaName":"00000849","AreaParentCode":"000070"}]},{"CityCode":"通州区","CityName":"000071","CityParentCode":"001","mList":[{"AreaCode":"通州区","AreaName":"00000850","AreaParentCode":"000071"}]},{"CityCode":"顺义区","CityName":"000072","CityParentCode":"001","mList":[{"AreaCode":"顺义区","AreaName":"00000851","AreaParentCode":"000072"}]},{"CityCode":"昌平区","CityName":"000073","CityParentCode":"001","mList":[{"AreaCode":"昌平区","AreaName":"00000852","AreaParentCode":"000073"}]},{"CityCode":"大兴区","CityName":"000074","CityParentCode":"001","mList":[{"AreaCode":"大兴区","AreaName":"00000853","AreaParentCode":"000074"}]},{"CityCode":"怀柔区","CityName":"000075","CityParentCode":"001","mList":[{"AreaCode":"怀柔区","AreaName":"00000855","AreaParentCode":"000075"}]},{"CityCode":"平谷区","CityName":"000076","CityParentCode":"001","mList":[{"AreaCode":"平谷区","AreaName":"00000854","AreaParentCode":"000076"}]},{"CityCode":"延庆县","CityName":"000077","CityParentCode":"001","mList":[{"AreaCode":"延庆县","AreaName":"00000857","AreaParentCode":"000077"}]},{"CityCode":"密云县","CityName":"000078","CityParentCode":"001","mList":[{"AreaCode":"密云县","AreaName":"00000856","AreaParentCode":"000078"}]}]
     */

    private String ProvinceCode;
    private String ProvinceName;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * CityCode : 东城区
         * CityName : 000061
         * CityParentCode : 001
         * mList : [{"AreaCode":"东城区","AreaName":"00000840","AreaParentCode":"000061"}]
         */

        private String CityCode;
        private String CityName;
        private String CityParentCode;
        private List<MListBean> mList;

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

        public List<MListBean> getMList() {
            return mList;
        }

        public void setMList(List<MListBean> mList) {
            this.mList = mList;
        }

        public static class MListBean {
            /**
             * AreaCode : 东城区
             * AreaName : 00000840
             * AreaParentCode : 000061
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
