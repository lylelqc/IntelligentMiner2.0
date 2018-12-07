/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sly.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sly.app.model.sly.LoginInfoBean;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description: 公共工具类
 */
public class CommonUtil2 {

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get format date
     *
     * @param timemillis
     * @return
     */
    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timemillis));
    }

    public static String getFormatDate2(long timemillis){
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(timemillis));
    }

    public static String getFormatDate3(long timemillis){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(timemillis));
    }

    /**
     * decode Unicode string
     *
     * @param s
     * @return
     */
    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * encode Unicode string
     *
     * @param s
     * @return
     */
    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }

    /**
     * convert time str
     *
     * @param time
     * @return
     */
    public static String convertTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * url is usable
     *
     * @param url
     * @return
     */
    public static boolean isUrlUsable(String url) {
        if (CommonUtil2.isEmpty(url)) {
            return false;
        }

        URL urlTemp = null;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            connt.disconnect();
        }
        return false;
    }

    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     * get toolbar height
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
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
    public static void goActivityForResult(Context context, Class<?> activity,Bundle bundle, int requestCode,boolean isFinish) {
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
     * @description: 发送广播
     * @param context
     * @param action
     */
    public static void goBrocast(Context context,Bundle bundle, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.sendOrderedBroadcast(intent,null);
    }


    /**
     * 获取屏幕宽
     */
    public static int screenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     */
    public static int screenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 判断是否已经登录
     *
     * @return
     */
    public static boolean hasLogin(Activity activity) {
        LoginInfoBean loginMsg = LoginMsgHelper.getResult(activity);
        if (loginMsg == null) {
            return false;
        }
        return true;
    }

    /**
     * 转换
     * */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获取内置SD卡路径
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static boolean isAppRunningForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList==null){
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(context.getPackageName())
                    && processInfo.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
    }

    public static List<Map<String, Object>> doExchange(List arrayLists){

        List<Map<String, Object>> notes = new ArrayList<>();

        if(arrayLists.size() == 1){
            String[] arr0= (String[]) arrayLists.get(0);
            for (int j=0;j<arr0.length;j++){
                Map<String,Object> map = new HashMap<>();
                map.put("title",arr0[j]);
                notes.add(map);
            }
            return notes;
        }

        String[] arr0= (String[]) arrayLists.get(0);
        int len0 = arr0.length;
        String[] arr1= (String[]) arrayLists.get(1);
        int len1 = arr1.length;

        int lenBoth=len0*len1;

        //定义临时存放排列数据的集合
        ArrayList<ArrayList<String>> tempArrayLists=new ArrayList<>(lenBoth);

        //第一层for就是循环arrayLists第一个元素的
        for (int i=0;i<len0;i++){
            //第二层for就是循环arrayLists第二个元素的
            for (int j=0;j<len1;j++){
                //判断第一个元素如果是数组说明，循环才刚开始
                Map<String,Object> map = new HashMap<>();
                map.put("title",arr0[i]+","+arr1[j]);
                notes.add(map);
            }
        }

        return notes;
    }


    public static List<Map<String, Object>> doExchange(List arrayLists,String pirce){

        List<Map<String, Object>> notes = new ArrayList<>();

        if(arrayLists.size() == 1){
            String[] arr0= (String[]) arrayLists.get(0);
            for (int j=0;j<arr0.length;j++){
                Map<String,Object> map = new HashMap<>();
                map.put("title",arr0[j]);

                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(">=1"+"(单位)"+":"+pirce+"元");
                map.put("value",arrayList);
                notes.add(map);
            }
            return notes;
        }

        String[] arr0= (String[]) arrayLists.get(0);
        int len0 = arr0.length;
        String[] arr1= (String[]) arrayLists.get(1);
        int len1 = arr1.length;

        int lenBoth=len0*len1;

        //定义临时存放排列数据的集合
        ArrayList<ArrayList<String>> tempArrayLists=new ArrayList<>(lenBoth);

        //第一层for就是循环arrayLists第一个元素的
        for (int i=0;i<len0;i++){
            //第二层for就是循环arrayLists第二个元素的
            for (int j=0;j<len1;j++){
                //判断第一个元素如果是数组说明，循环才刚开始
                Map<String,Object> map = new HashMap<>();
                map.put("title",arr0[i]+","+arr1[j]);
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(">=1"+"(单位)"+":"+pirce+"元");
                map.put("value",arrayList);
                notes.add(map);
            }
        }

        return notes;
    }



    public static String[] compareArrayFromList(String str){
        String[] ints = new String[3];
        String num = str.substring(0,str.indexOf("("));
        String pirce = str.substring(str.indexOf(":")+1,str.indexOf("元"));
        if(num.contains(">=")){
            ints[0] = num.replaceAll(">=","");
            ints[1] = "";
        }else if(num.indexOf("-")>0){
            ints[0] = num.split("-")[0];
            ints[1] = num.split("-")[1];
        }else {
            ints[0] = num;
            ints[1] = "";

        }
//        if(num.indexOf("-")>0 && num.indexOf("-")+1!= num.length()){
//            ints[0] = num.split("-")[0];
//            ints[1] = num.split("-")[1];
//        }else if(num.indexOf("-")>0){
//            ints[0] = num.split("-")[0];
//            ints[1] = "";
//        }else {
//            ints[0] = num;
//        }
        ints [2] = pirce;
        return ints;
    }

    public static void showPirce(TextView textView, List<Map<String,Object>> maps){
        String str = "";
        for(Map<String,Object> map : maps){
            str+=map.get("title").toString()+":";
            if(map.containsKey("value")){
                for(String s:(ArrayList<String>)map.get("value")){
                    str+=s+",";
                }
            }
        }
        textView.setText(str.length()>0?str.substring(0,str.length()-1):"");
    }

    public static String getPirce(double d1){
        DecimalFormat    df   = new DecimalFormat("######0.00");
        return df.format(d1);
    }

    public static String getOrderStatus(String status){
//        0(已取消)10(默认):未付款;20:已付款未发货;30:已发货;40:已收货未评价50已评价;
        if("0".equals(status)){
            return "已取消";
        }else if("10".equals(status)){
            return "未付款";
        }else if("20".equals(status)){
            return "已付款未发货";
        }else if("30".equals(status)){
            return "已发货";
        }else if("40".equals(status)){
            return "已收货未评价";
        }else if("50".equals(status)){
            return "已评价";
        }
        return "未付款";
    }



}
