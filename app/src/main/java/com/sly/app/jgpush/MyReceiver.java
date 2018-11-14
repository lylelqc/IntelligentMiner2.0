package com.sly.app.jgpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.comm.AppContext;
import com.sly.app.utils.PreferenceUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;
import vip.devkit.library.Logcat;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	private int notificationId = 0;//接受的notification的id
	private int badgeCount = 0;//记录图标的角标数量

	NotificationManager mManager;
	Notification mNotification;
	Notification.Builder mBuilder;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Logger.w(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			Logger.e(TAG, "action=" + intent.getAction());

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.w(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.w(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.w(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				notificationId = notifactionId;

                Logger.w(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

				if(!AppContext.getInstance().isForeground()){
                    badgeCount = SharedPreferencesUtil.getInt(context, "badgeCount", 0);
                    badgeCount++;
                    SharedPreferencesUtil.putInt(context, "badgeCount", badgeCount);

                    //不同系统设置角标
                    if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")
							|| Build.MANUFACTURER.equalsIgnoreCase("hongmi")) {
                        String title = context.getResources().getString(R.string.app_name);
                        String content = bundle.getString(JPushInterface.EXTRA_ALERT);

                        getNotification(context, title, content, notificationId);
                        ShortcutBadger.applyNotification(context, mNotification, badgeCount);
                    } else {
                        ShortcutBadger.applyCount(context, badgeCount);
                    }
                }

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.w(TAG, "[MyReceiver] 用户点击打开了通知");

				//打开自定义的Activity
				Intent i = new Intent(context, MainActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.w(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			}else if ("com.sly.app.REMOVE_BADGE".equals(intent.getAction())) {
				Logger.w(TAG, "[MyReceiver] 移除桌面角标");
				removeBadgeNum(context);
			}
			else {
				Logger.w(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){
			Logcat.e(e.getMessage());
		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if(key.equals(JPushInterface.EXTRA_REGISTRATION_ID)){
//				SharedPreferencesUtil.putString(AppContext.mContext, "RegistrationID", bundle.get(key)+"");
			} else if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.get(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		/*if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}*/
	}

	/**
	 * 获取notification
	 *
	 * @param context
	 * @param title          标题
	 * @param message        内容
	 * @param notificationId notification的id
	 */
	private void getNotification(Context context, String title, String message, int notificationId) {
		if (mManager == null) {
			mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		mManager.cancel(notificationId);

		mBuilder = new Notification.Builder(context);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		mBuilder.setSmallIcon(R.drawable.icon_sly);
		mNotification = mBuilder.build();
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;

		//添加点击事件
		Intent intent = new Intent();
		intent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
		intent.addCategory("com.sly.app");
		Bundle bundle = new Bundle();
		bundle.putInt(JPushInterface.EXTRA_NOTIFICATION_ID, notificationId);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);

		mManager.notify(notificationId, mNotification);
	}

	private void removeBadgeNum(Context context) {
        int count = SharedPreferencesUtil.getInt(context, "badgeCount", 0);
	    if(count > 0){
	        Logcat.e("移除成功");
            if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
                ShortcutBadger.applyNotification(context, mNotification, 0);
            } else {
                ShortcutBadger.applyCount(context, 0);
            }
        }
	}
}
