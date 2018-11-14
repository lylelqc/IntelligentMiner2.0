package com.sly.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sly.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * JDK 创建于 2016/10/22 12:00
 */

public class TimeDialogChoos {

    private static String Year;
    private static String Month;
    private static String Day;
    private static android.content.Context Context;
    private static WheelView mWheelYear;
    private static WheelView mWheelMonth;
    private static WheelView mWheelDay;

    public interface onInputNameEvent {
        void onClick(String data);
    }

    /**
     * 年月日时间选择器
     * @param context 上下文
     * @param listener 监听
     * @param sceenwith 手机屏幕宽
     * @param defulData 默认时间
     *  dnegdeng
     *
     *                   TimeDialogChoos.setTimeDialog(this, new TimeDialogChoos.onInputNameEvent() {
                            @Override
                            public void onClick(String data) {

                            }
                            },1080,"1991-1-1");
     */
    public static Dialog setTimeDialog(android.content.Context context, final onInputNameEvent listener, int sceenwith, String defulData) {
        Context = context;
        LayoutInflater inflaterDl = LayoutInflater.from(Context);
        View layout = inflaterDl.inflate(R.layout.dialogtool_time_picker, null);
        final Dialog dialog = new AlertDialog.Builder(Context).create();
        dialog.show();
        dialog.setCancelable(true);
        Window mWindow = dialog.getWindow();
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setContentView(layout);
        setShowWith(sceenwith, mWindow, 0.95);
        mWheelYear = layout.findViewById(R.id.year);
        mWheelMonth = layout.findViewById(R.id.month);
        mWheelDay = layout.findViewById(R.id.day);
        mWheelYear.setData(getYearData());
        mWheelMonth.setData(getMonthData());
        mWheelDay.setData(getDayData());
        if (!defulData.isEmpty()) {
            try {
                int[] date = getYMDArray(defulData, "-");
                String Defaultyear = date[0] + "";
                int mYearbefore = Integer.parseInt(Defaultyear.substring(0, 2));
                int mDefaultyear = Integer
                        .parseInt(Defaultyear.substring(2, 4));
                if (mDefaultyear == 00 && mYearbefore < 20) {
                    mDefaultyear = 0;
                } else if (mYearbefore > 19) {
                    mDefaultyear = 100 + mDefaultyear;
                }

                mWheelYear.setDefault(mDefaultyear); // +1
                mWheelMonth.setDefault(date[1] - 1);
                mWheelDay.setDefault(date[2] - 1);
            } catch (Exception e) {
                mWheelYear.setDefault(0); // +1
                mWheelMonth.setDefault(0);
                mWheelDay.setDefault(0);
            }

        } else {
            mWheelYear.setDefault(0); // +1
            mWheelMonth.setDefault(0);
            mWheelDay.setDefault(0);
        }

        Button mSure = layout.findViewById(R.id.dialogtool_time_select_sure);

        mSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mTime = "";
                Year = mWheelYear.getSelectedText().replace( "年", "");
                Month = mWheelMonth.getSelectedText().replace( "月", "");
                Day = mWheelDay.getSelectedText().replace( "日", "");
                if (Month.length() == 1 && Day.length() == 1) {
                    mTime = Year + "-" + "0" + Month + "-" + "0" + Day;
                } else if (Month.length() == 2 && Day.length() == 1) {
                    mTime = Year + "-" + Month + "-" + "0" + Day;
                } else if (Month.length() == 2 && Day.length() == 2) {
                    mTime = Year + "-" + Month + "-" + Day;
                } else if (Month.length() == 1 && Day.length() == 2) {
                    mTime = Year + "-" + "0" + Month + "-" + Day;
                }
                listener.onClick( mTime);
                dialog.dismiss();

            }
        });
        return dialog;
    }
    private static void setShowWith(int mWidth, Window mWindow, double d) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = (int) (mWidth * d);
        mWindow.setAttributes(lp);
    }
    private static ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1900; i <=  getStringYear(); i++) {
            list.add(String.valueOf(i)
                    +"年");
        }
        return list;
    }

    private static ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(String.valueOf(i)
                    +  "月");
        }
        return list;
    }

    private static ArrayList<String> getDayData() {
        // ignore condition
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 32; i++) {
            list.add(String.valueOf(i)
                    + "日");
        }
        return list;
    }

    public static int[] getYMDArray(String datetime, String splite) {
        int date[] = {0, 0, 0, 0, 0};
        if (datetime != null && datetime.length() > 0) {
            String[] dates = datetime.split(splite);
            int position = 0;
            for (String temp : dates) {
                date[position] = Integer.valueOf(temp);
                position++;
            }
        }
        return date;
    }

    public static int getStringYear() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(currentTime);
        return Integer.parseInt(dateString);
    }
}
