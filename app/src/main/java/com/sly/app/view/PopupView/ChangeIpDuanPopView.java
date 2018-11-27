package com.sly.app.view.PopupView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;

public class ChangeIpDuanPopView extends PopupWindow implements View.OnClickListener {

    private View contentView;
    private Context mContext;

    private TextView ip2;
    private TextView ip3;
    private TextView ip4;

    public ChangeIpDuanPopView(final Activity context){
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_change_ip_duan, null);

        ip2 = contentView.findViewById(R.id.tv_ip_2);
        ip3 = contentView.findViewById(R.id.tv_ip_3);
        ip4 = contentView.findViewById(R.id.tv_ip_4);

        ip2.setOnClickListener(this);
        ip3.setOnClickListener(this);
        ip4.setOnClickListener(this);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0 ,5);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public void setSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_ip_2:
                mOnSearchClickListener.onSearchClick(view, 2);
                break;
            case R.id.tv_ip_3:
                mOnSearchClickListener.onSearchClick(view, 3);
                break;
            case R.id.tv_ip_4:
                mOnSearchClickListener.onSearchClick(view, 4);
                break;
        }
    }
}
