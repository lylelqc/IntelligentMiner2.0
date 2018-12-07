package com.sly.app.fragment.sly.hosting;

import android.support.v4.app.FragmentTransaction;

import com.sly.app.R;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.fragment.Sly2YunwFragment;

public class HostingYunwFragment extends BaseFragment {

    private Sly2YunwFragment mYunwHomeFragment;

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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mYunwHomeFragment == null) {
            mYunwHomeFragment = new Sly2YunwFragment();
            ft.add(R.id.fl_yunw_home, mYunwHomeFragment).commitAllowingStateLoss();

        } else {
            ft.show(mYunwHomeFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hosting_yunw;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}
