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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sly.app.R;
import com.sly.app.view.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vip.devkit.library.Logcat;

/**
 * 文 件 名: DateDialog
 * 创 建 人: By k
 * 创建日期: 2017/12/11 11:58
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class DateView extends LinearLayout {

    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mDistrictPicker;
    private static String mProvinceName;
    private static int mCityID;
    private static int mDisttrctID;

    private int mCurrProvinceIndex = -1;
    private int mCurrCityIndex = -1;
    private int mCurrDistricIndex = -1;
    private DataUtil mDataUtil;
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<String> mMonthList = new ArrayList<String>();
    private ArrayList<String> mDayList = new ArrayList<String>();
    private Map<String, ArrayList<String>> mYearMonthMap = new HashMap<>();
    private Map<String, ArrayList<String>> mMonthDayMap = new HashMap<>();

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAreaInfo();
    }

    public DateView(Context context) {
        this(context, null);
    }

    private void getAreaInfo() {
        mDataUtil = new DataUtil(getContext());
        mYearList = mDataUtil.getYearList();
        mYearMonthMap = mDataUtil.getYearMonthMap();
        mMonthDayMap = mDataUtil.getMonthDayMap();
        Logcat.i("Year:"+mYearList.size());
        Logcat.i("mYearMonthMap:"+mYearMonthMap.size());
        Logcat.i("mMonthDayMap:"+mMonthDayMap.size());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_city, this);
        mProvincePicker = (WheelView) findViewById(R.id.province_wv);
        mCityPicker = (WheelView) findViewById(R.id.city_wv);
        mDistrictPicker = (WheelView) findViewById(R.id.distric_wv);
        mProvincePicker.setData(mYearList);
        mProvincePicker.setDefault(0);
        String defaultYear = mYearList.get(0);
        mMonthList = mYearMonthMap.get(defaultYear);
        mCityPicker.setData(mMonthList);
        mCityPicker.setDefault(0);
        String defaultMonth = mMonthList.get(0);
        mDayList = mMonthDayMap.get(defaultMonth);
        mDistrictPicker.setData(mDayList);
        mDistrictPicker.setDefault(0);
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrProvinceIndex != id) {
                    mCurrProvinceIndex = id;
                    mProvinceName = text;
                    String selectProvince = mProvincePicker.getSelectedText();
                    if (selectProvince == null || selectProvince.equals(""))
                        return;
                    // get city names by province
                    try {
                        mMonthList = mDataUtil.getMonthByYear(mYearList.get(id));
                        if (mMonthList.size() == 0) {
                            return;
                        }
                        mCityPicker.setData(mMonthList);
                        if (mMonthList.size() > 1) {
                            //if city is more than one,show start index == 1
                            mCityPicker.setDefault(1);
                            mDayList = mDataUtil.getDayByMonth(mMonthList.get(1));
                            mDistrictPicker.setData(mDayList);
                            if (mDayList.size() > 1) {
                                mDistrictPicker.setDefault(1);
                            } else {
                                mDistrictPicker.setDefault(0);
                            }
                        } else {
                            mCityPicker.setDefault(0);
                            mDayList = mDataUtil.getDayByMonth(mMonthList.get(0));
                            mDistrictPicker.setData(mDayList);
                            if (mDayList.size() > 1) {
                                mDistrictPicker.setDefault(1);
                            } else {
                                mDistrictPicker.setDefault(0);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrCityIndex != id) {
                    mCurrCityIndex = id;
                    mCityID = id;
                    String selectCity = mCityPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    // get city names by province
                    try {
                        mDayList = mDataUtil.getDayByMonth(mMonthList.get(id));
                        if (mDayList.size() == 0) {
                            return;
                        }
                        mDistrictPicker.setData(mDayList);
                        if (mDayList.size() > 1) {
                            //if city is more than one,show start index == 1
                            mDistrictPicker.setDefault(1);
                        } else {
                            mDistrictPicker.setDefault(0);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        mDistrictPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrDistricIndex != id) {
                    mCurrDistricIndex = id;
                    mDisttrctID = id;
                    String selectCity = mDistrictPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    int lastIndex = Integer.valueOf(mDistrictPicker.getListSize());
                    if (id > lastIndex) {
                        mDistrictPicker.setDefault(lastIndex - 1);
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    public String getProvince() {
        if (mProvincePicker == null) {
            return null;
        }
        return mProvincePicker.getSelectedText();
    }

    public String getCity() {
        if (mCityPicker == null) {
            return null;
        }
        return mCityPicker.getSelectedText();
    }

    public String getDisteic() {
        if (mDistrictPicker == null) {
            return null;
        }
        return mDistrictPicker.getSelectedText();
    }
    public String getPid() {
        return mProvinceName;
    }
    public int getCid() {
        return mCityID;
    } public int getAid() {
        return mDisttrctID;
    }

}
