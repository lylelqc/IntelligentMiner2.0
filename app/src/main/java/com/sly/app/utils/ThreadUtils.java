package com.sly.app.utils;

import com.sly.app.comm.AppContext;

/**
 * 作者 by K
 * 时间：on 2017/8/21 14:09
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ThreadUtils {
    /**
     * 在主线程中运行的线程
     */
    public static void runOnUiThread(Runnable r) {
        AppContext.mHandler.post(r);
    }

    public static void runOnChildThread(Runnable r) {
        new Thread(r).start();
    }

}
