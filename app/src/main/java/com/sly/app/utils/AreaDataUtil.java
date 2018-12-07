package com.sly.app.utils;

import android.content.Context;

import com.sly.app.activity.mine.address.AddressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 全国省份城市操作类
 *
 * @author JiangPing
 */
public class AreaDataUtil {

    /**
     * 一个省份对应多个城市
     */
    private String[] single_province_city;
    /**
     * 全国省市Map key:省份 |Value:城市集合
     */
    private HashMap<String, ArrayList<String>> mCityMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> mCityCodeMap = new HashMap<>();
    private AddressWheel addressWheel;
    /**
     * 全国市区Map key:市 |Value:区
     */
    private HashMap<String, ArrayList<String>> mDistrict = new HashMap<>();
    private HashMap<String, ArrayList<String>> mDistrictCode = new HashMap<>();
    public AreaDataUtil(Context context) {
        addressWheel=new AddressWheel();
        getAllCityMap();
    }


    /**
     * 获得全国省份的列表
     *
     * @return
     */
    public ArrayList<String> getProvinces() {
        ArrayList<String> provinceList = new ArrayList<>();
        provinceList= AddressWheel.Province;
        return provinceList;
    }
    /**
     * 省份区map
     *
     * @return
     */
    private void getAllCityMap() {

        mCityMap= AddressWheel.mCitisDatasMap;
        mCityCodeMap= AddressWheel.mCitisCodeDatasMap;
        mDistrict= AddressWheel.mDistrictDatasMap;
        mDistrictCode= AddressWheel.mDistrictCodeDatasMap;
    }
    /**
     * 根据省份查找对应的城市列表
     *
     * @return 城市集合
     */

    public HashMap<String, ArrayList<String>> getCityMap(){
        return  mCityMap;
    }

    public HashMap<String, ArrayList<String>> getDistrictMap(){
        return  mDistrict;
    }
    /**
     * 根据省份查找对应的城市列表
     *
     * @return 区集合
     */
    public ArrayList<String> getDistrictByCity(String city){
        List<String> list =mDistrict.get(city);
        ArrayList<String> arrayList=new ArrayList<>();
        for (String district:list){
            arrayList.add(district);
        }
        return arrayList;
    }
    /**
     * 根据省份查找对应的城市列表
     *
     * @return 城市集合
     */
    public ArrayList<String> getCityByProvince(String provinceStr) {
     try {
         List<String> list = mCityMap.get(provinceStr);
         ArrayList<String> arrList = new ArrayList<>();
         for (String city : list) {
             arrList.add(city);
         }
         return arrList;
     }catch (Exception e){
     }
        return null;
    }


    public ArrayList<String> getProvincesCode() {
        ArrayList<String> provinceCodeList = new ArrayList<>();
        provinceCodeList= AddressWheel.ProvinceCode;
        return provinceCodeList;
    }

    public HashMap<String, ArrayList<String>> getCityCodeMap() {
        return mCityCodeMap;
    }

    public HashMap<String, ArrayList<String>> getDistrictCodeMap() {
        return mDistrictCode;
    }
}
