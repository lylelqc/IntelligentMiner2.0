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
package com.sly.app.view.dateview;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文 件 名: DataUtil
 * 创 建 人: By k
 * 创建日期: 2017/12/11 13:53
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：日期操作工具类
 */
public class DataUtil {

    /**
     * mYearMonth  Map key:Year | Value:Month
     */
    private Map<String, ArrayList<String>> mYearMonthMap = new HashMap<>();
    /**
     * mMonthDayMap  Map key:Month  | Value:Day
     */
    private Map<String, ArrayList<String>> mMonthDayMap = new HashMap<>();
    DataWheel mDataWheel;

    public DataUtil(Context mContent) {
        mDataWheel=new DataWheel();
        getAlDataMap();
    }
    /**
     * 年份 月份MAP
     *
     * @return
     */
    private void getAlDataMap() {
        mYearMonthMap= DataWheel.mYearMonthMap;
        mMonthDayMap= DataWheel.mMonthDayMap;
    }

    /**
     * 获得年份列表
     *
     * @return
     */
    public ArrayList<String> getYearList() {
        ArrayList<String> mYearList = new ArrayList<>();
        mYearList = DataWheel.mYear;
        return mYearList;
    }
    public Map<String, ArrayList<String>> getYearMonthMap() {
        return mYearMonthMap;
    }
    public Map<String, ArrayList<String>> getMonthDayMap() {
        return mMonthDayMap;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getDayByMonth(String month){
        List<String> list =mMonthDayMap.get(month);
        ArrayList<String> arrayList=new ArrayList<>();
        for (String district:list){
            arrayList.add(district);
        }
        return arrayList;
    }
    /**
     *
     * @return
     */
    public ArrayList<String> getMonthByYear(String year) {
        try {
            List<String> list = mYearMonthMap.get(year);
            ArrayList<String> arrList = new ArrayList<>();
            for (String city : list) {
                arrList.add(city);
            }
            return arrList;
        }catch (Exception e){
        }
        return null;
    }



}
