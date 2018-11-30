package com.sly.app.view.PopupView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.view.ToggleRadioButton;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ManageStatusCheckPopView extends PopupWindow implements View.OnClickListener{

    private View contentView;
    private Context mContext;

    private ToggleRadioButton cbChoseItem1;
    private ToggleRadioButton cbChoseItem2;
    private ToggleRadioButton cbChoseItem3;
    private ToggleRadioButton cbChoseItem4;

    private TextView tvSearch;
    private TextView tvReSet;

    public ManageStatusCheckPopView(final Activity context){
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_manage_status, null);

        cbChoseItem1 = contentView.findViewById(R.id.cb_chose_item1);
        cbChoseItem2 = contentView.findViewById(R.id.cb_chose_item2);
        cbChoseItem3 = contentView.findViewById(R.id.cb_chose_item3);
        cbChoseItem4 = contentView.findViewById(R.id.cb_chose_item4);

        tvSearch = contentView.findViewById(R.id.tv_manage_status_reset);
        tvReSet = contentView.findViewById(R.id.tv_manage_status_confirm);

        tvReSet.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String[] getStatusInfo(){
        String[] info = new String[4];
        info[0] = cbChoseItem1.isChecked() ? "true" : "false";
        info[1] = cbChoseItem2.isChecked() ? "true" : "false";
        info[2] = cbChoseItem3.isChecked() ? "true" : "false";
        info[3] = cbChoseItem4.isChecked() ? "true" : "false";
        return info;
    }

    public void setStatusSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onStatusSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manage_status_reset:
                clearAll();
                break;
            case R.id.tv_manage_status_confirm:
                this.dismiss();
                mOnSearchClickListener.onStatusSearchClick(tvSearch, 0);
                break;
        }
    }

    public void clearAll(){
        cbChoseItem1.setChecked(false);
        cbChoseItem2.setChecked(false);
        cbChoseItem3.setChecked(false);
        cbChoseItem4.setChecked(false);
    }
}
