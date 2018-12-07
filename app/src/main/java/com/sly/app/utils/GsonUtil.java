/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 文 件 名: GsonUtil
 * 创 建 人: By k
 * 创建日期: 2017/10/31 09:46
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class GsonUtil {

    public static <T> List fromJsonArray(String  json,  Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        List<T> retList = gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
        return retList;
    }


    public static <T> T fromJsonObject(String  json, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        T tClass = gson.fromJson(json, clazz);
        return tClass;
    }
}
