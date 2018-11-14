package com.sly.app.utils;

import android.content.Context;
import android.os.Bundle;

import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.model.PostResult;
import com.sly.app.model.sly.LoginInfoBean;

import de.greenrobot.event.EventBus;


/**
 * Created by jm on 2016/6/18.
 */
public class LoginMsgHelper {

    //是否登录
    public static boolean isLogin(Context mContext){

        String User = SharedPreferencesUtil.getString(mContext, "User", null);
        String Token = SharedPreferencesUtil.getString(mContext, "Token", null);

        if(CommonUtils.isBlank(User)|| CommonUtils.isBlank(Token)) {
            return false;
        }
        return !"None".equals(User) && !"None".equals(Token);
    }

    //登录之后返回的结果
    public static LoginInfoBean getResult(Context mContext){

        String result = PreferenceUtils.getPrefString(mContext, Contants.Preference.loginMsg, null);

        if(!StringHelper.isEmpty(result)) {
            try {
                JsonHelper<LoginInfoBean> jsonHelper = new JsonHelper<LoginInfoBean>(LoginInfoBean.class);
                LoginInfoBean loginMsg = jsonHelper.getData(result,null);
                return loginMsg;

            }catch(Exception ex){
                return null;
            }
        }
        return null;
    }

    //登录退出处理
    public static  void exitLogin(Context mContext){

        SharedPreferencesUtil.putString(mContext, "User", "None");
        SharedPreferencesUtil.putString(mContext, "Token", "None");
        SharedPreferencesUtil.putString(mContext, "FrSysCode", "None");
        SharedPreferencesUtil.putString(mContext, "FMasterCode", "None");
        SharedPreferencesUtil.putString(mContext, "PersonSysCode", "None");
        /**角色Role**/
        SharedPreferencesUtil.putString(mContext,"LoginType","None");
        SharedPreferencesUtil.putString(mContext, "mineType", "None");
        SharedPreferencesUtil.putString(mContext, "Key", "None");
        SharedPreferencesUtil.putString(mContext,"RegistrationID", "None");

        EventBus.getDefault().post(new PostResult(EventBusTags.LOGOUT));
    }

//    /**
//     * 需要重新登陆 //项当做singleTask
//     * @param context
//     */
    public static void reLogin(Context context)
    {
        LoginMsgHelper.exitLogin(context);

//        判断如果是后台运行的话不弹出登陆框
        if(!CommonUtil2.isAppRunningForeground(context))
        {
            return;
        }

        Bundle bundle = new Bundle();
//        bundle.putBoolean(LoginActivity.Afresh,true);
        CommonUtil2.goActivity(context,LoginActivity.class,bundle);
    }


    public void setLoginListener(){

    }

}
