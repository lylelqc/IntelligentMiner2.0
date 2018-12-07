/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.model;

import java.util.List;

/**
 * 文 件 名: UserGroupTeam
 * 创 建 人: By k
 * 创建日期: 2017/12/15 10:56
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class UserGroupTeam {


    private List<RowsBean> Rows;

    public List<RowsBean> getRows() {
        return Rows;
    }

    public void setRows(List<RowsBean> Rows) {
        this.Rows = Rows;
    }

    public static class RowsBean {
        /**
         * ChildMember : JW766655
         * Name : 美好地球村001
         * Layer : 1
         * LevelName : 普通会员
         */

        private String ChildMember;
        private String Name;
        private int Layer;
        private String LevelName;

        public String getChildMember() {
            return ChildMember;
        }

        public void setChildMember(String ChildMember) {
            this.ChildMember = ChildMember;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getLayer() {
            return Layer;
        }

        public void setLayer(int Layer) {
            this.Layer = Layer;
        }

        public String getLevelName() {
            return LevelName;
        }

        public void setLevelName(String LevelName) {
            this.LevelName = LevelName;
        }
    }
}
