package com.sly.app.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.SlySettingActivity;
import com.sly.app.activity.yunw.machine.MachineManageActivity;
import com.sly.app.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class EmptyFragment extends BaseFragment {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.btn_exits)
    Button btnExits;

    public static String mContent = "???";

    public static EmptyFragment newInstance(String content) {
        EmptyFragment fragment = new EmptyFragment();
        mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        Logcat.e("title - " + mContent);
        if(mContent.equals("3")){
            tvEmpty.setVisibility(View.GONE);
            btnExits.setVisibility(View.VISIBLE);
        }else{
            tvEmpty.setVisibility(View.VISIBLE);
            btnExits.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_empty;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @OnClick({R.id.btn_exits})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_exits:
                AppUtils.goActivity(mContext, SlySettingActivity.class);
                break;
        }
    }
}
