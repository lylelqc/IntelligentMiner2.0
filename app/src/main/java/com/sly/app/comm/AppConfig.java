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
package com.sly.app.comm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import com.sly.app.utils.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vip.devkit.common.share.ShareConfig;
import vip.devkit.common.share.ShareManager;
import vip.devkit.library.Logcat;
import vip.devkit.view.common.ImgPicker.ImagePicker;
import vip.devkit.view.common.ImgPicker.loader.ImgPickerLoader;
import vip.devkit.view.common.ImgPicker.view.CropImageView;

/**
 * 文 件 名: AppConfig
 * 创 建 人: By k
 * 创建日期: 2017/11/1 15:26
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：app 初始化 配置
 */
public class AppConfig {
      Context mContext;
    static  AppConfig mAppConfig;

    public AppConfig() {
        mAppConfig = this;
        mContext = AppContext.getInstance();
    }

    public static synchronized AppConfig getInstance() {
        if (mAppConfig == null) {
            mAppConfig = new AppConfig();
        }
        return mAppConfig;

    }

    protected void initCommonConfig() {
        /**
         * LogCat
         */
        Logcat.init(true, "App");
        /**
         *  缓存
         */
        SharedPreferencesUtil.init(mContext, "App");
        /**
         *  扫码 ，生成二维码
         */
        ZXingLibrary.initDisplayOpinion(mContext);
    }

    protected void initShare() {
        ShareConfig config = ShareConfig.instance()
                .qqId(Constants.QQ_APP_ID)
                .wxId(Constants.WX_APP_ID)
                .weiboId(Constants.WB_APP_ID)
                .wxSecret(Constants.WX_SECRET_ID)
                .weiboRedirectUrl(Constants.WB_APP_ID);
        ShareManager.init(config);
    }

    protected void initLeakCanary(boolean isE) {
        if (isE){
            LeakCanary.install(AppContext.getInstance());
        }
    }

    protected void initBugly( ) {
        setStrictMode();
        //  Bugly.setIsDevelopmentDevice(mContext, true);
        Beta.autoCheckUpgrade = true;
        Beta.enableHotfix = true;
        Beta.canAutoDownloadPatch = true;
        Beta.canNotifyUserRestart = true;
        Beta.canAutoPatch = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.initDelay = 1 * 1000;
        Bugly.init(mContext, Constants.BUGLY_APP_ID, true);
    }

    protected void initRealm() {
        Realm.init(mContext);
//        RealmConfiguration config = new RealmConfiguration.Builder().name("dqc.realm").build();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new CustomMigration())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        initAppData(mContext);
    }

    protected void initAppData(Context mContext ){
        try {
            PackageManager packageManager = mContext.getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            //应用装时间
            long firstInstallTime = packageInfo.firstInstallTime;
            //应用最后一次更新时间
            long lastUpdateTime = packageInfo.lastUpdateTime;
            if (firstInstallTime == lastUpdateTime) {
                Logcat.i("第一次安装：" + firstInstallTime + "/" + lastUpdateTime);
            } else {
                Logcat.i("非第一次安装：" + firstInstallTime + "/" + lastUpdateTime + "/" + new Date().getTime());
//                Long s = (System.currentTimeMillis() - lastUpdateTime) / (1000 * 60);//分
                Long s = (System.currentTimeMillis() - lastUpdateTime) / (1000);// 秒
                Logcat.i("s:"+s+"/"+System.currentTimeMillis() +"/"+(System.currentTimeMillis() - lastUpdateTime) );
                if ((System.currentTimeMillis() - lastUpdateTime) / (1000) <10) {
                    Logcat.i("刚刚安装/更新");
                    SharedPreferencesUtil.clearAll(mContext);//清除数据
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initImgPicker(int countImg) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImgPickerLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(countImg);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }


}
