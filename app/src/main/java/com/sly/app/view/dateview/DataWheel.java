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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import vip.devkit.library.Logcat;

/**
 * 文 件 名: DataWheel
 * 创 建 人: By k
 * 创建日期: 2017/12/11 13:54
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class DataWheel {
    private int nowYear, endYear = 1970;
    /**
     * 年份
     */
    public static ArrayList<String> mYear = new ArrayList<>();
    /**
     * 月份
     */
    public ArrayList<String> mMonth = new ArrayList<>();

    /**
     * 天
     */
    protected ArrayList<String> mDate;

    /**
     * key - 年 value - 月
     */
    public static Map<String, ArrayList<String>> mYearMonthMap = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            // 降序排序
            return o2.compareTo(o1);
        }
    });
    /**
     * key - 月 values - 日
     */
    public static HashMap<String, ArrayList<String>> mMonthDayMap = new HashMap<String, ArrayList<String>>();


    public DataWheel() {
        initData();
    }

    public DataWheel(int endYear) {
        this.endYear = endYear;
        initData();
    }

    public DataWheel(int nowYear, int endYear) {
        this.endYear = endYear;
        this.nowYear = nowYear;
        initData();
    }

    private void initData() {
        Calendar nowYears = Calendar.getInstance();//可以对每个时间域单独修改
        nowYear = nowYears.get(Calendar.YEAR);
        initYear(nowYear, endYear);
        initMonth();
        initYearMonthDay();
        Logcat.i("\n年：" + mYear.size() + "\n月：" + mMonth.size() + "\n天：" + "年-月" + mYearMonthMap.size());
    /*    for (Iterator<String> iterator = mYearMonthMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            List<String> stringArrayList = mYearMonthMap.get(key);
            for (int i = 0; i <stringArrayList.size() ; i++) {
                Logcat.i("\n年："+key+"\n月："+stringArrayList.get(i)+"\n日："+mMonthDayMap.get(stringArrayList.get(i)));
            }}
      */
/*
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + map.get(key));
        }*/
    }

    private void initYearMonthDay() {
        for (int i = 0; i < mYear.size(); i++) {
            for (int j = 0; j < mMonth.size(); j++) {
                int getYear = Integer.valueOf(mYear.get(i).replace("年", ""));
                int getMonth = Integer.valueOf(mMonth.get(j).replace("月", ""));
                mDate = new ArrayList<>();
                for (int k = 1; k < initMonthDays(getYear, getMonth) + 1; k++) {
                    mDate.add(k + "日");
                }
                mMonthDayMap.put(mMonth.get(j), mDate);
            }
            mYearMonthMap.put(mYear.get(i), mMonth);
        }
    }

    /**
     * 初始化年份
     *
     * @param nowYear
     * @param endYear
     */
    private void initYear(int nowYear, int endYear) {
        int size = nowYear - endYear;
        for (int i = 0; i < size; i++) {
            mYear.add(nowYear - i + "年");
        }
    }

    /**
     * 初始化月份
     */
    private void initMonth() {
        for (int i = 1; i < 13; i++) {
            mMonth.add(i + "月");
        }
    }

    /**
     * 获取一个月多少天
     *
     * @param year
     * @param month
     * @return
     */
    private int initMonthDays(int year, int month) {
        Calendar day = Calendar.getInstance();
        /// day.set(Calendar.YEAR, year); // 年
        /// day.set(Calendar.MONTH, month); // 月
        day.set(year, month, 0); //输入类型为int类型
        return day.get(Calendar.DAY_OF_MONTH);
    }

}
