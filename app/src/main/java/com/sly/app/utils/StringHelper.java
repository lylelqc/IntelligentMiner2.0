package com.sly.app.utils;

import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 字符串助手
 */
public class StringHelper {


    public static boolean isEmpty(EditText... editTexts) {
        if(editTexts == null || editTexts.length == 0){
            return true;
        }

        for(EditText editText:editTexts){
            if(editText == null || editText.getText().toString().trim().length() == 0 || isEmpty(editText.getText().toString())){
                return true;
            }
        }

        return false;

    }



    public static boolean isNoAllEmpty(EditText... editTexts) {
        boolean isAllNull = false;
        if(editTexts == null || editTexts.length == 0){
            return true;
        }

        for(EditText editText:editTexts){
            if(editText == null || editText.getText().toString().trim().length() == 0 || isEmpty(editText.getText().toString())){
                isAllNull = true;
            }else{
                isAllNull = false;
                break;
            }
        }

        return isAllNull;

    }



    public static boolean isNoAllEmpty(TextView... textViews) {
        boolean isAllNull = false;
        if(textViews == null || textViews.length == 0){
            return true;
        }

        for(TextView textView:textViews){
            if(textView == null || textView.getText().toString().trim().length() == 0 || isEmpty(textView.getText().toString())){
                isAllNull = true;
            }else{
                isAllNull = false;
                break;
            }
        }

        return isAllNull;

    }


    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 全角转换成半角
     *
     * @param input 原始字符串
     * @return 转换后的字符串
     */
    public static String toBanJiao(String input) {
        if (isEmpty(input) == true) {
            return input;
        } else {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == '\u3000') {
                    c[i] = ' ';
                } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            return new String(c);
        }
    }

    /**
     * 半角转换成全角
     *
     * @param input 原始字符串
     * @return 转换后的字符串
     */
    public static String toQuanJiao(String input) {
        if (isEmpty(input) == true) {
            return input;
        } else {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == ' ') {
                    c[i] = '\u3000';
                } else if (c[i] < '\177') {
                    c[i] = (char) (c[i] + 65248);
                }
            }
            return new String(c);
        }
    }

    /**
     * 判断电话逻辑，直接改成判断11位，之前的正则无法判断147,177号段
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        return mobiles.matches("^1[0-9]{10}");
    }

    /**
     * 判断密码是否正确
     * @param password
     * @return
     */
    public static boolean isCorrectPassword(String password)
    {
        String reg = "^[a-zA-Z0-9]+$";
        return Pattern.compile(reg).matcher(password).find();
    }

    /**
     * 保留两位小数
     */
    public static String getTwoDecimal(double value){
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(value);
    }

    /**
     * 转换
     * */
    public static String[] ToDecimal(String str){
        if(isEmpty(str)){
            return new String[]{"0.","0"};
        }

        String[] array = new String[2];;
        if(str.split("\\.").length>1){
            array[0] = str.split("\\.")[0]+".";
            array[1] = str.split("\\.")[1];
        }else{
            array[0] = str.split("\\.")[0]+".";
            array[1] = "0";
        }
        return array;

    }

    /**
     * 正则表达式
     **/
    public static boolean isPattern(String str){

        return true;
//        String regEx = "^(?![0-9]+$)(?![a-zA-Z]+$)[.A-Za-z0-9_\\d]{6,12}$";
//        Pattern pattern = Pattern.compile(regEx);
//        Matcher matcher = pattern.matcher(str);
//        boolean rs = matcher.matches();
//        return rs;
    }



}
