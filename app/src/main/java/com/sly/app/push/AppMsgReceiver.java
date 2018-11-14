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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.sly.app.R;
import com.sly.app.utils.CommonUtils;

import vip.devkit.library.Logcat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 文 件 名: AppMsgReceiver
 * 创 建 人: By k
 * 创建日期: 2017/11/8 10:37
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class AppMsgReceiver extends BroadcastReceiver {
    public static final String TYPE = "type"; //这个type是为了Notification更新信息的，这个不明白的朋友可以去搜搜，很多

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if (CommonUtils.getAppSatus(context, context.getPackageName()) == 1) {
            Logcat.i("app进程在前台");
        } else if (CommonUtils.getAppSatus(context,  context.getPackageName()) == 2 || CommonUtils.getAppSatus(context,  context.getPackageName())== 3) {
            Logcat.i("app进程在后台或者没有启动");
        }
        if (intent != null) {
            String title = intent.getExtras().getString("title");
            String info = intent.getExtras().getString("info");
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.view_noite_notifiction);
//view_custom.setImageViewResource(R.id.custom_icon, R.drawable.icon);
//view_custom.setInt(R.id.custom_icon,"setBackgroundResource",R.drawable.icon);
            view_custom.setTextViewText(R.id.tv_msg_title, title);
            view_custom.setTextViewText(R.id.tv_msg_data, info);
            Intent startIntent = new Intent(context, SplashActivity.class);
            startIntent.putExtra("notificationId", 1);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0);
//            mBuilder.setContent(view_custom);
            mBuilder.setContentTitle(title)
                    .setContentText(info)
                    .setTicker("您有新的消息！") //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                     //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                    .setSmallIcon(R.drawable.ic_launcher);
                     mNotificationManager.notify(1, mBuilder.build());

        }
    }
}
