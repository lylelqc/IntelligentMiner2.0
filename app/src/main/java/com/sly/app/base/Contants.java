package com.sly.app.base;

import android.content.Context;
import android.os.Environment;

import com.sly.app.R;
import com.sly.app.utils.PreferenceUtils;

/**
 * 常量
 */
public class Contants {

    public static String APK_STORE_FOLDER = Environment.getExternalStorageDirectory() + "/xiepinhui";

    public static final class LoadView {

        public final static String LOADING = "正在加载中...";//正在加载中

        public final static String CLICKLOAD = "点击加载数据";//点击加载
    }

    public static final class NetStatus {

        public final static String NETDISABLE = "网络不给力";
        public final static String NETDISABLEORNETWORKDISABLE = "网络不给力或者服务器异常";

        public final static String LOGIN_MSG_EMPTY = "登录信息为空,请重新登录";

        public final static String NETWORK_MAYBE_DISABLE = "暂时木有网络,刷新看看?";
    }


    public static final class Refresh{

        public final static int[] refreshColorScheme = {R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary};
    }

    public static final class Column {

        public final static int ONE = 1;//1列

        public final static int TWO = 2;//2列

        public final static int THREE = 3;//3列

        public final static int FOUR = 4;//4列
    }

    /**
     * 请求状态
     */
    public static final class HttpStatus {

        /**
         * 刷新请求状态
         */
        public static final int refresh_data = 1;

        /**
         * 加载更多请求状态
         */
        public static final int loadmore_data = 2;
    }



    /**
     * Preference 相关key
     */
    public static final class Preference {

        public final static String loginMsg = "login_message";//登录信息

        public final static String phone = "phone";

        public final static String OSOTIME = "oso_time"; //osom秘钥保存时间

        public final static String OSOMsg = "oso_msg"; //osom秘钥保存

        public final static String GUIDE_TIP = "guide_tip_V1.0";//引导提示

        public final static String USER_BG = "user_bg";  //个人页面背景

        public final static String COLLECTGOODSNUM = "collectgoodsnum"; //商品收藏数量

        public final static String COLLECTSHOPNUM = "collectshopnum"; //商品收藏数量

    }
    /**
     * 自定义ID
     * */
    public static final int BASE_VIEW_ID = 1230;

    public static void setCollectGoodsNum(Context mContext,boolean isAdd){
        int num = Integer.parseInt( PreferenceUtils.getPrefString(mContext,Contants.Preference.COLLECTGOODSNUM,"0"));
        if(isAdd){
            num++;
        }else{
            num--;
        }
        PreferenceUtils.setPrefString(mContext,Contants.Preference.COLLECTGOODSNUM,num+"");
    }

    public static void setCollecShopNum(Context mContext,boolean isAdd){
        int num = Integer.parseInt( PreferenceUtils.getPrefString(mContext,Contants.Preference.COLLECTSHOPNUM,"0"));
        if(isAdd){
            num++;
        }else{
            num--;
        }
        PreferenceUtils.setPrefString(mContext,Contants.Preference.COLLECTSHOPNUM,num+"");
    }



}
