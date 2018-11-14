package com.sly.app.activity.mine.address;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.comm.AppContext;
import com.sly.app.model.CityBean;
import com.sly.app.model.pcc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressWheel {

    /**
     * 所有省
     */
    public static ArrayList<String> Province = new ArrayList<>();
    public static ArrayList<String> ProvinceCode = new ArrayList<>();
    /**
     * 所有市
     */
    public ArrayList<String> mCityDatas;
    public ArrayList<String> mCityCodeDatas;

    /**
     * 所有区
     */
    protected ArrayList<String> mDistrictDatas;
    protected ArrayList<String> mDistrictCodeDatas;
    /**
     * key - 省 value - 市
     */
    public static HashMap<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> mCitisCodeDatasMap = new HashMap<String, ArrayList<String>>();
    /**
     * key - 市 values - 区
     */
    public static HashMap<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> mDistrictCodeDatasMap = new HashMap<String, ArrayList<String>>();


    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mDistrictyName;
    /**
     * 当前区的名称
     */
    protected String mCurrentCityName;
    /**
     * 当城市ID
     */
    protected String mRegionalismCode = null;
    /**
     * 当城市父级城市ID
     */
    protected String ParentCode = null;
    public List<String> parentcodeList = new ArrayList<>();//省份code
    public List<String> parentcodeListname = new ArrayList<>();//省份name
    public List<String> RegionalismparentcodeCodelist = new ArrayList<>();//城市父code
    public List<String> mCityData = new ArrayList<>();//城市名字
    public List<String> mCitycode = new ArrayList<>();//城市code
    public List<String> disrctcode = new ArrayList<>();//城区父code
    public List<String> disrctcodename = new ArrayList<>();//城区名字


    /**
     * 解析省市区的XML数据
     */
    public AddressWheel() {
       // initProvinceDatas();
        getdatas();
        initPCAs();
    }

    protected List getdata() {
        List<CityBean.RECORDSBean.RECORDBean> recordsBeen = null;
        CityBean mPensons = null;
        AssetManager asset = AppContext.mContext.getAssets();
        InputStream input = null;
        try {
            input = asset.open("citys.json");
            String json = convertStreamToString(input);
            json = json.replace("/n", "");
            json = json.replace(" ", "");
//			Log.e("解析json",json);
            Gson gson = new Gson();
            mPensons = gson.fromJson(json, CityBean.class);
            recordsBeen = mPensons.getRECORDS().getRECORD();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordsBeen;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public void initPCAs(){
        for (int p = 0; p <been.size() ; p++) {
            Province.add(been.get(p).getProvinceName());//省
            ProvinceCode.add(been.get(p).getProvinceCode());//省编号
            mCityDatas = new ArrayList<>();
            mCityCodeDatas = new ArrayList<>();
            for (int c = 0; c <been.get(p).getCity().size() ; c++) {
                mCityDatas.add(been.get(p).getCity().get(c).getCityName());
                mCityCodeDatas.add(been.get(p).getCity().get(c).getCityCode());
                mDistrictDatas = new ArrayList<>();
                mDistrictCodeDatas = new ArrayList<>();
                for (int a = 0; a <been.get(p).getCity().get(c).getArea().size() ; a++) {
                    mDistrictDatas.add(been.get(p).getCity().get(c).getArea().get(a).getAreaName());
                    mDistrictCodeDatas.add(been.get(p).getCity().get(c).getArea().get(a).getAreaCode());
                }
                mDistrictDatasMap.put(been.get(p).getCity().get(c).getCityName(),mDistrictDatas);
                mDistrictCodeDatasMap.put(been.get(p).getCity().get(c).getCityCode(),mDistrictCodeDatas);
            }
            mCitisDatasMap.put(been.get(p).getProvinceName(),mCityDatas);
            mCitisCodeDatasMap.put(been.get(p).getProvinceCode(),mCityCodeDatas);
        }
    }

    List<pcc> been = null;
    protected void getdatas( ) {
        AssetManager asset = AppContext.mContext.getAssets();
        InputStream input = null;
        try {
            input = asset.open("pcc.json");
            String json = convertStreamToStrings(input);
            json = json.replace("/n", "");
            json = json.replace(" ", "");
        //    Logcat.i("\"解析json\"" + json);
            been = JSON.parseArray(json, pcc.class);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String convertStreamToStrings(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
