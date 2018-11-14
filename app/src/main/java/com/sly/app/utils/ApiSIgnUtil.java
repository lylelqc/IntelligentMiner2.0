package com.sly.app.utils;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ApiSIgnUtil {
    Context context;


    public static ApiSIgnUtil init (Context context){
        return  new ApiSIgnUtil(context);
    }

    public ApiSIgnUtil (Context context){
        this.context=context;
    }
    /**要写一个公共类 把所有请求参数  按首字母排序，参数名转大写＋参数值＋链接符＋后台返回的key  转md5加密*/
    public String getSign(Map<String,String> map, String key,Boolean isMd5){
        ArrayList<String> list = new ArrayList<>();
        for (String keys:map.keySet()) {
            list.add(keys);
        }
        Collections.sort(list,new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                String a = s.toLowerCase();
                String b = t1.toLowerCase();
                return a.compareTo(b);
            }
        });
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <list.size() ; i++) {
            buffer.append(list.get(i).toUpperCase()+":").append(map.get(list.get(i)).toUpperCase()+">");
            // Logcat.i("list - Key:"+list.get(i).toUpperCase());
        }
        buffer.append(key);
        if (isMd5){
            return  EncryptUtil.MD5( buffer.toString());
        }
        return buffer.toString();
    }
    public String getSign(Map<String,String> map, String key ){
        ArrayList<String> list = new ArrayList<>();
        for (String keys:map.keySet()) {
            list.add(keys);
        }
        Collections.sort(list,new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                String a = s.toLowerCase();
                String b = t1.toLowerCase();
                return a.compareTo(b);
            }
        });
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <list.size() ; i++) {
            buffer.append(list.get(i).toUpperCase()+":").append(map.get(list.get(i)).toUpperCase()+">");
            // Logcat.i("list - Key:"+list.get(i).toUpperCase());
        }
        buffer.append(key);
        return buffer.toString();
    }

}
