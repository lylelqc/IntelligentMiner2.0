package com.sly.app.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON数据解析工具
 */
public class JsonHelper<T> {

    private static volatile JsonHelper instance = null;

    private Class cls;
    private List<T> list;

    public JsonHelper(Class cls) {
        this.cls = cls;
        list = new ArrayList<T>();
    }

    /**
     * 根据节点转化对象列表
     *
     * @param json
     * @param nodeName
     * @return
     */
    public List<T> getDatas(String json, String nodeName) {
        try {
            JSONArray array = new JSONObject(json).getJSONArray(nodeName);

            for (int i = 0; i < array.length(); i++) {
                ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

                    @Override
                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz == Field.class || clazz == Method.class;
                    }
                };

                Gson gson = new GsonBuilder()
                        .addSerializationExclusionStrategy(exclusionStrategy)
                        .addDeserializationExclusionStrategy(exclusionStrategy)
                        .create();
                list.add((T) gson.fromJson(array.getJSONObject(i).toString(),
                        cls));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }


    /**
     * 根据节点转化对象列表
     *
     * @param json
     * @return
     */
    public List<T> getDatas(String json) {
        try {
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

                    @Override
                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz == Field.class || clazz == Method.class;
                    }
                };

                Gson gson = new GsonBuilder()
                        .addSerializationExclusionStrategy(exclusionStrategy)
                        .addDeserializationExclusionStrategy(exclusionStrategy)
                        .create();
                list.add((T) gson.fromJson(array.getJSONObject(i).toString(),
                        cls));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }


    /**
     * 根据节点转化对象
     *
     * @param json
     * @param nodeName
     * @return
     */
    public T getData(String json, String nodeName) {
        try {

            JSONObject object = new JSONObject(json);
            if (!StringHelper.isEmpty(nodeName)) {
                object = object.getJSONObject(nodeName);
            }

            ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return clazz == Field.class || clazz == Method.class;
                }
            };

            Gson gson = new GsonBuilder()
                    .addSerializationExclusionStrategy(exclusionStrategy)
                    .addDeserializationExclusionStrategy(exclusionStrategy)
                    .create();
            System.out.println("解析："+object.toString());
            return (T) gson.fromJson(object.toString(), cls);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getJson(Object object){
        return new Gson().toJson(object);
    }


}
