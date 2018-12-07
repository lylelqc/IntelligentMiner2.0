package com.sly.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者 by K
 * 时间：on 2017/8/21 11:17
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class CommonUtils {
    public CommonUtils() {
    }

    private static Random random = new Random();
    private static char ch[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};//最后又重复两个0和1，因为需要凑足数组长度为64

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static synchronized String createRandomString(int length) {
        if (length > 0) {
            int index = 0;
            char[] temp = new char[length];
            int num = random.nextInt();
            for (int i = 0; i < length % 5; i++) {
                temp[index++] = ch[num & 9];//取后面六位，记得对应的二进制是以补码形式存在的。
                num >>= 6;//63的二进制为:111111
                // 为什么要右移6位？因为数组里面一共有64个有效字符。为什么要除5取余？因为一个int型要用4个字节表示，也就是32位。
            }
            for (int i = 0; i < length / 5; i++) {
                num = random.nextInt();
                for (int j = 0; j < 5; j++) {
                    temp[index++] = ch[num & 9];
                    num >>= 6;
                }
            }
            return new String(temp, 0, length);
        } else if (length == 0) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 通过uri获取bitmap
     */
    public Bitmap getBitmapFromUri(Context mContext, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 打开微信扫一扫
     *
     * @param context
     */
    @SuppressLint("WrongConstant")
    public static void toWeChatScanDirect(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        /**
         移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         联通：130、131、132、152、155、156、185、186
         电信：133、153、180、189、（1349卫通）
         */
        String num = "[1][346578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        String regex = "^[A-Za-z]{1,40}@[A-Za-z0-9]{1,40}\\.[A-Za-z]{2,3}$";
        return email.matches(regex);
    }


    public static final int IDENTITYCODE_OLD = 15; // 老身份证15位
    public static final int IDENTITYCODE_NEW = 18; // 新身份证18位
    public static int[] Wi = new int[17];

    /**
     * 判断身份证号码是否正确。
     *
     * @param code 身份证号码。
     * @return 如果身份证号码正确，则返回true，否则返回false。
     */
    public static boolean isIdentityCode(String code) {

        if (StringUtils.isEmpty(code)) {
            return false;
        }

        code = code.trim().toUpperCase();

        // 长度只有15和18两种情况
        if ((code.length() != IDENTITYCODE_OLD)
                && (code.length() != IDENTITYCODE_NEW)) {
            return false;
        }

        // 身份证号码必须为数字(18位的新身份证最后一位可以是x)
        Pattern pt = Pattern.compile("(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)");
        Matcher mt = pt.matcher(code);
        if (!mt.find()) {
            return false;
        }

        //第一代身份证正则表达式(15位)
        String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //第二代身份证正则表达式(18位)
        String isIDCard2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

        //验证身份证
        if (!code.matches(isIDCard1) || !code.matches(isIDCard2)) {
            return false;
        }
        return true;
    }

    public static String proStr(String str) {
        if (!str.contains("</BODY></HTML") || !str.contains("</BODY></HTML>") || !str.contains("</BODY></HTML>\"}")) {
            if (!isBlank(str)) {
                str = str.replace("\\", "");
                str = str.replace("\\", "");
                str = str.substring(1, str.length() - 1);
                return str.toString();
            } else {
                return "{\"status\":\"0\",\"msg\":\"失败\",\"data\":\"接口返回数据：" + str + "\"}";
            }
        } else {
            return "{\"status\":\"0\",\"msg\":\"失败\",\"data\":\"接口返回数据：" + str + "\"}";
        }
    }

    public static String proStrs(String str) {
        if (!str.contains("</BODY></HTML") || !str.contains("</BODY></HTML>") || !str.contains("</BODY></HTML>\"}")) {
            if (!isBlank(str)) {
                if (str.contains("{\"Rows\":[]}}")) {
                    str = "null";
                    return "{\"status\":\"0\",\"msg\":\"失败\",\"data\":\"接口返回数据：" + str + "\"}";
                } else {
                    str = str.replace("\\", "");
                    str = str.substring(1, str.length() - 1);
                    return str.toString();
                }
            } else {
                return "{\"status\":\"0\",\"msg\":\"失败\",\"data\":\"接口返回数据：" + str + "\"}";
            }
        } else {
            return "{\"status\":\"0\",\"msg\":\"失败\",\"data\":\"接口返回数据：" + str + "\"}";
        }
    }

    public static String replaceStr(int start, int end, String rep, String str) {
        StringBuffer buffer = new StringBuffer(str);
        buffer.replace(start, end, rep);
        return buffer.toString();
    }

    public static String proMobile(String str) {
        StringBuffer buffer = new StringBuffer(str);
        buffer.replace(3, 7, "****");
        return buffer.toString();
    }

    public static String proIdCard(String str) {
        StringBuffer buffer = new StringBuffer(str);
        buffer.replace(4, str.length() - 2, " **** **** **** ");
        return buffer.toString();
    }

    public static <T> String GsonToJson(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    public static final <T> T GsonToObject(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        T tClass = gson.fromJson(json, clazz);
        return tClass;
    }


    private final static String ENCODE = "GBK";

    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDoubleStr(Double str) {
        return String.format(Locale.getDefault(), "%.2f", str);
    }

    /**
     * get App versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static int getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }

    public static String[] wrapperContent(Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + ".java";
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String msg = (objects == null) ? "Log with null object" : getObjectsString(objects);
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{msg, headString};
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                } else {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }

    public static boolean matcherIdentityCard(String value) {
        IDCardTester idCardTester = new IDCardTester();
        return idCardTester.test(value);
    }

    private static class IDCardTester {
        public boolean test(String content) {
            if (TextUtils.isEmpty(content)) {
                return false;
            }
            final int length = content.length();
            if (15 == length) {
                try {
                    return isOldCNIDCard(content);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else if (18 == length) {
                return isNewCNIDCard(content);
            } else {
                return false;
            }
        }

        final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        final char[] VALID = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        public boolean isNewCNIDCard(String numbers) {
            numbers = numbers.toUpperCase();
            int sum = 0;
            for (int i = 0; i < WEIGHT.length; i++) {
                final int cell = Character.getNumericValue(numbers.charAt(i));
                sum += WEIGHT[i] * cell;
            }
            int index = sum % 11;
            return VALID[index] == numbers.charAt(17);
        }

        public boolean isOldCNIDCard(String numbers) {
            String yymmdd = numbers.substring(6, 11);
            boolean aPass = numbers.equals(String.valueOf(Long.parseLong(numbers)));
            boolean yPass = true;
            try {
                new SimpleDateFormat("yyMMdd").parse(yymmdd);
            } catch (Exception e) {
                e.printStackTrace();
                yPass = false;
            }
            return aPass && yPass;
        }
    }

    private static String ProDate(Date mDate, int year, int month, int day) {
        return null;
    }

    private static void reStartActivity(Activity mActivity) {
        Intent intent = mActivity.getIntent();
        mActivity.finish();
        mActivity.startActivity(intent);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
