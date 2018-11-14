package com.sly.app.view;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * 作者 by K
 * 时间：on 2017/9/23 12:47
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ViewUtils {
    public static void addFooterViewForList(ListView listView, View footerView) {
        if (listView!=null&&listView.getFooterViewsCount() == 0 && footerView != null) {
            listView.addFooterView(footerView);
        }
    }

    /**
     * 获取屏幕的宽高
     */
    private static int getWindowHeight(Context context) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //不含虚拟按键
        windowManager.getDefaultDisplay().getSize(point);
        //包含虚拟按键
        //windowManager.getDefaultDisplay().getRealSize(point);
        int height = point.y;
        //屏幕高度
        int width = point.x;
        return height;
    }

    /**
     * 获取屏幕的宽高
     */
    private static int getWindowWidth(Context context) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        int width = point.x;
        return width;
    }

}
