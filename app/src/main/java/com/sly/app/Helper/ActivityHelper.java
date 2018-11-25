package com.sly.app.Helper;

import android.app.Activity;

import java.util.Stack;

/**
 *
 */
public class ActivityHelper {
    private static ActivityHelper instance;
    private Stack<Activity> activityStack;//activity栈
    private ActivityHelper() {
    }
    public static ActivityHelper getInstance() {
        if (instance == null) {
            instance = new ActivityHelper();
        }
        return instance;
    }
    //把一个activity压入栈中
    public void pushOneActivity(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(actvity);
    }
    //获取栈顶的activity，先进后出原则
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }
    //移除一个activity
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }

    }
    public void popMoreActivity(int num) {
        for (int i = 0;i<num;i++) {
            if (activityStack != null && activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }

    }
    //退出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }
    public void BackToHomePage(){
        if (activityStack != null) {
            while (activityStack.size() > 1) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }
}
