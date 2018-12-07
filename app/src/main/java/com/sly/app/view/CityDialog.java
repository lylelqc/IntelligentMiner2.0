package com.sly.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sly.app.R;

import vip.devkit.library.Logcat;


/**
 * Created by 01 on 2016/11/9.
 */
public class CityDialog {
    private static android.content.Context Context;
    private CityDialog() {

    }
    private static CityDialog single=null;
    public static CityDialog getInstance() {
        if (single == null) {
            single = new CityDialog();
        }
        return single;
    }
    public interface onInputNameEvent {
        void onClick(String data, String pid, String cid, String aid);
    }
    public Dialog setNicknameDialog(android.content.Context context, final onInputNameEvent listener, int sceenwith) {
        Context = context;
        final AlertDialog dialog = new AlertDialog.Builder(Context, R.style.quick_option_dialog).create();
        LayoutInflater inflaterDl =  (LayoutInflater) Context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflaterDl.inflate(R.layout.address_wheel, null);
        dialog.setView(layout);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.show();
     //   Window mWindow = dialog.getWindow();
     //   mWindow.setContentView(R.layout.address_wheel);
     //   setShowWith(sceenwith, mWindow, 0.95);
        TextView mSure = (TextView) dialog.findViewById(R.id.dialogtool_time_select_sure);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final AddressDialog addressDialog=(AddressDialog)dialog.findViewById(R.id.city_picker);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  StringBuffer city = new StringBuffer().append(addressDialog.getProvince())

                          .append(" ").append(addressDialog.getCity()).append(" ").append(addressDialog.getDisteic());
                Logcat.i("--------------------------------------------------------------"+addressDialog.getPid());
                Logcat.i("--------------------------------------------------------------"+addressDialog.getCid());
                Logcat.i("--------------------------------------------------------------"+addressDialog.getAid());
                listener.onClick(city.toString(),addressDialog.getPid(),addressDialog.getCid(),addressDialog.getAid());
                dialog.dismiss();

            }
        });
        return dialog;
    }
    private  void setShowWith(int mWidth, Window mWindow, double d) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = (int) (mWidth * d);
        mWindow.setAttributes(lp);
    }
}
