package com.sly.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sly.app.R;
import com.sly.app.utils.AreaDataUtil;

import java.util.ArrayList;
import java.util.HashMap;

import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2016/11/4.
 */
public class AddressDialog extends LinearLayout {
    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mDistrictPicker;
    private static String mProvinceName;
    private static String mCityID;
    private static String mDisttrctID = "";

    private int mCurrProvinceIndex = -1;
    private int mCurrCityIndex = -1;
    private int mCurrDistricIndex = -1;
    private AreaDataUtil mAreaDataUtil;
    private ArrayList<String> mProvinceList = new ArrayList<>();
    private ArrayList<String> mCityList = new ArrayList<String>();
    private ArrayList<String> mDistric = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> cityMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> districMap = new HashMap<>();
    private ArrayList<String> mProvinceCodeList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> cityCodeMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> districCodeMap = new HashMap<>();
    private String mProvinceCode;

    public AddressDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAreaInfo();
    }

    public AddressDialog(Context context) {
        this(context, null);
    }

    private void getAreaInfo() {
        mAreaDataUtil = new AreaDataUtil(getContext());
        mProvinceList = mAreaDataUtil.getProvinces();
        mProvinceCodeList = mAreaDataUtil.getProvincesCode();
        cityMap = mAreaDataUtil.getCityMap();
        cityCodeMap = mAreaDataUtil.getCityCodeMap();
        districMap = mAreaDataUtil.getDistrictMap();
        districCodeMap = mAreaDataUtil.getDistrictCodeMap();
        Logcat.i("districMap --------------" + cityMap.size());
        Logcat.i("districMap --------------" + districMap.size());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_city, this);
        mProvincePicker = findViewById(R.id.province_wv);
        mCityPicker = findViewById(R.id.city_wv);
        mDistrictPicker = findViewById(R.id.distric_wv);
        mProvincePicker.setData(mProvinceList);
        mProvincePicker.setDefault(0);
        String defaultProvince = mProvinceList.get(0);
        cityMap = mAreaDataUtil.getCityMap();
        mCityList = cityMap.get(defaultProvince);
        mCityPicker.setData(mCityList);
        mCityPicker.setDefault(0);
        String defaultCity = mCityList.get(0);
        districMap = mAreaDataUtil.getDistrictMap();
        mDistric = districMap.get(defaultCity);
        mDistrictPicker.setData(mDistric);
        mDistrictPicker.setDefault(0);
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrProvinceIndex != id) {
                    mCurrProvinceIndex = id;
                    mProvinceCode = mProvinceCodeList.get(id);
                    mProvinceName = text;
                    String selectProvince = mProvincePicker.getSelectedText();
                    if (selectProvince == null || selectProvince.equals(""))
                        return;
                    // get city names by province
                    try {
                        mCityList = mAreaDataUtil.getCityByProvince(mProvinceList.get(id));
                        if (mCityList.size() == 0) {
                            return;
                        }
                        mCityPicker.setData(mCityList);
                        if (mCityList.size() > 1) {
                            //if city is more than one,show start index == 1
                            mCityPicker.setDefault(1);
                            mDistric = mAreaDataUtil.getDistrictByCity(mCityList.get(1));
                            mDistrictPicker.setData(mDistric);
                            if (mDistric.size() > 1) {
                                mDistrictPicker.setDefault(1);
                            } else {
                                mDistrictPicker.setDefault(0);
                            }
                        } else {
                            mCityPicker.setDefault(0);
                            mDistric = mAreaDataUtil.getDistrictByCity(mCityList.get(0));
                            mDistrictPicker.setData(mDistric);
                            if (mDistric.size() > 1) {
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
                    mCityID = cityCodeMap.get(mProvinceCode).get(id);
                    String selectCity = mCityPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    // get city names by province
                    try {
                        mDistric = mAreaDataUtil.getDistrictByCity(mCityList.get(id));
                        if (mDistric.size() == 0) {
                            return;
                        }
                        mDistrictPicker.setData(mDistric);
                        if (mDistric.size() > 1) {
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
                    mDisttrctID = districCodeMap.get(mCityID).get(id);
                    String selectCity = mDistrictPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    int lastIndex = Integer.valueOf(mDistrictPicker.getListSize());
                    if (id > lastIndex) {
                        mDistrictPicker.setDefault(lastIndex - 1);
                    }
                } else {

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
        if (mProvinceCode == null) {
            mProvinceCode = mProvinceCodeList.get(0);
        }
        return mProvinceCode;
    }

    public String getCid() {
        mCityID = cityCodeMap.get(mProvinceCode).get(0);
        return mCityID;
    }

    public String getAid() {
        mDisttrctID = districCodeMap.get(mCityID).get(0);
        return mDisttrctID;
    }

}
