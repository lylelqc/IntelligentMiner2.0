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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ManageStatusCheckPopView extends PopupWindow implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View contentView;
    private Context mContext;

    private CheckBox cbChoseItem1;
    private CheckBox cbChoseItem2;
    private CheckBox cbChoseItem3;
    private CheckBox cbChoseItem4;

    private TextView tvSearch;
    private TextView tvReSet;

    private Set<String> indexSet = new TreeSet<>();

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

        cbChoseItem1.setOnCheckedChangeListener(this);
        cbChoseItem2.setOnCheckedChangeListener(this);
        cbChoseItem3.setOnCheckedChangeListener(this);
        cbChoseItem4.setOnCheckedChangeListener(this);
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

    public Set<String> getStatusIndexSet(){
        return indexSet;
    }

    public void setStatusSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        switch (compoundButton.getId()){
            case R.id.cb_chose_item1:
                saveChoseStatus(isCheck, "01");
                break;
            case R.id.cb_chose_item2:
                saveChoseStatus(isCheck,"02");
                break;
            case R.id.cb_chose_item3:
                saveChoseStatus(isCheck,"00");
                break;
            case R.id.cb_chose_item4:
                saveChoseStatus(isCheck,"05");
                break;
        }
    }

    //define interface
    public interface OnSearchClickListener {
        void onStatusSearchClick(View view, int position);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manage_status_reset:
                indexSet.clear();
                cbChoseItem1.setChecked(false);
                cbChoseItem2.setChecked(false);
                cbChoseItem3.setChecked(false);
                cbChoseItem4.setChecked(false);
                break;
            case R.id.tv_manage_status_confirm:
                this.dismiss();
                mOnSearchClickListener.onStatusSearchClick(tvSearch, 0);
                break;
        }
    }

    private void saveChoseStatus(boolean isCheck, String tag){
        if(isCheck){
            if(!indexSet.contains(tag)){
                indexSet.add(tag);
            }
        }else{
            if(indexSet.contains(tag)){
                indexSet.remove(tag);
            }
        }
    }
}
