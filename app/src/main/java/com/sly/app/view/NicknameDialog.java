package com.sly.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.sly.app.R;

/**
 * Created by 01 on 2016/10/27.
 */
public class NicknameDialog {
    private static android.content.Context Context;
    private static EditText editText;
    public interface onInputNameEvent {
        void onClick(String data, EditText editText1);
    }
    public static Dialog setNicknameDialog(android.content.Context context, final onInputNameEvent listener, int sceenwith) {
        Context = context;
        final AlertDialog dialog = new AlertDialog.Builder(Context).create();
        LayoutInflater inflaterDl =  (LayoutInflater) Context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflaterDl.inflate(R.layout.dialogtool_nickname, null);
        dialog.setView(layout);
        dialog.show();
        Window mWindow = dialog.getWindow();
        mWindow.setContentView(R.layout.dialogtool_nickname);
        setShowWith(sceenwith, mWindow, 0.95);
        editText= (EditText) dialog.findViewById(R.id.et_nickname);
        Button mSure = (Button) dialog.findViewById(R.id.dialogtool_time_select_sure);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                listener.onClick(name,editText);
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
}
