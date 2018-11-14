/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.push;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;

/**
 * 文 件 名: SplashActivity
 * 创 建 人: By k
 * 创建日期: 2017/11/8 14:08
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class SplashActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //移除标记为id的通知 (只是针对当前Context下的所有Notification)
            notificationManager.cancel(1);
        //隐藏ActionBar
      //  getSupportActionBar().hide();
        //使用handler倒数3秒后进入MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                //将参数取出，传递到MainActivity中
//                if(getIntent().getBundleExtra(Constants.EXTRA_BUNDLE) != null){
//                    intent.putExtra(Constants.EXTRA_BUNDLE,
//                            getIntent().getBundleExtra(Constants.EXTRA_BUNDLE));
//                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
