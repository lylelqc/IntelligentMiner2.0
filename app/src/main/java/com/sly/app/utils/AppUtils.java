package com.sly.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.sly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    /**
     * 解析没有Rows结果
     *
     * @param result
     * @return
     */
    public static List<?> parseResult(String result, Class className) {
        JsonHelper<?> dataParser = new JsonHelper<>(className);

        try {
            String searchResult = new JSONObject(result).getString("data");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 解析有Rows结果
     *
     * @param result
     * @return
     */
    public static List<?> parseRowsResult(String result, Class className) {
        JsonHelper<?> dataParser = new JsonHelper<>(className);

        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * @description: Activity跳转
     * @param context
     * @param activity 目标Activity
     */
    public static void goActivity(Context context, Class<?> activity) {
        goActivity(context,activity,null);
    }


    /**
     * @description: Activity跳转
     * @param context
     * @param activity 目标Activity
     * @param bundle 携带的数据
     */
    public static void goActivity(Context context, Class<?> activity,Bundle bundle) {
        goActivity(context,activity,bundle,false);
    }

    /**
     * @description: Activity跳转
     * @param context
     * @param activity 目标Activity
     * @param bundle 携带的数据
     * @param isFinish
     */
    public static void goActivity(Context context, Class<?> activity, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(context, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if(isFinish){
            ((Activity)context).finish();
        }
    }

    /**
     * @description: Activity跳转,带返回结果
     * @param context
     * @param activity 目标Activity
     * @param bundle 携带的数据
     * @param requestCode 请求码
     * @param isFinish
     */
    public static void goActivityForResult(Context context, Class<?> activity, Bundle bundle, int requestCode, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(context, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
        if(isFinish){
            ((Activity)context).finish();
        }
    }

    /**
     * @description: ForResult跳转,带返回结果
     * @param context
     * @param bundle 携带的数据
     * @param resultCode 返回标示码
     */
    public static void goResult(Context context,Bundle bundle, int resultCode) {
        Intent intent = ((Activity)context).getIntent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) context).setResult(resultCode, intent);
        ((Activity)context).finish();
    }

    /**
     * String 是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    /**
     * list 是否为空
     * @param list
     * @return
     */
    public static boolean isListBlank(List<?> list) {
        return (list == null || list.size() == 0);
    }


    /**
     * 获取未读消息数量
     * @param context
     * @return
     */
    public static String getNewsCount(Context context){
        String newsCount = SharedPreferencesUtil.getString(context, "NewsCount", "0");
        int count = Integer.parseInt(newsCount);
        if(count > 99){
            return "99+";
        }
        return newsCount;
    }

    public static void setStatusBarColor(Activity activity, int color){
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }


    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
