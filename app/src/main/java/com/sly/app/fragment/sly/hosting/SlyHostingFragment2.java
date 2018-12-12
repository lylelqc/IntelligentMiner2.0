package com.sly.app.fragment.sly.hosting;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.sly.app.R;
import com.sly.app.comm.BusEvent;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.fragment.Sly2MasterFragment;
import com.sly.app.fragment.sly.SlyHostingFragment;
import com.sly.app.model.PostResult;
import com.sly.app.utils.SharedPreferencesUtil;

import butterknife.BindView;
import vip.devkit.library.Logcat;

public class SlyHostingFragment2 extends BaseFragment{

    @BindView(R.id.fl_hosting_miner_master)
    FrameLayout flHostingMinerMaster;
    @BindView(R.id.fl_hosting_yunw)
    FrameLayout flHostingYunw;

    public static String mContent = "???";
    private HostingMinerMasterFragment mHostingMinerMasterFragment;
    private Sly2MasterFragment mMasterFragment;
    private HostingYunwFragment mHostingYunwFragment;
    private FragmentTransaction ft1;
    private FragmentTransaction ft2;
    private String mineType;


    public static SlyHostingFragment2 newInstance(String content) {
        SlyHostingFragment2 fragment = new SlyHostingFragment2();
        fragment.mContent = content;
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
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hosting2;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (BusEvent.UPDATE_HOSTING_OPERATION_DATA.equals(postResult.getTag())) {
            showFragment(2);
        }
        else if (BusEvent.UPDATE_HOSTING_MASTER_DATA.equals(postResult.getTag())
                || BusEvent.UPDATE_HOSTING_MINER_DATA.equals(postResult.getTag())) {
            showFragment(1);
        }
    }

    @Override
    protected void initViewsAndEvents() {

        mineType = SharedPreferencesUtil.getString(mContext,"mineType","None");

        ft1 = getFragmentManager().beginTransaction();
        ft2 = getFragmentManager().beginTransaction();
        if(mineType != null && !"None".equals(mineType)){
            if(mineType.equals("Operation")){
                showFragment(2);
            }else{
                showFragment(1);
            }
        }
    }

    public void showFragment(int index) {
        switch (index) {
            case 1:
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */

//                if (mHostingMinerMasterFragment == null) {
//                    mHostingMinerMasterFragment = new HostingMinerMasterFragment();
//                    ft1.add(R.id.fl_hosting_miner_master, mHostingMinerMasterFragment).commitAllowingStateLoss();
//
//                }

                if (mMasterFragment == null) {
                    mMasterFragment = new Sly2MasterFragment();
                    ft1.add(R.id.fl_hosting_miner_master, mMasterFragment).commitAllowingStateLoss();
                }
                flHostingMinerMaster.setVisibility(View.VISIBLE);
                flHostingYunw.setVisibility(View.GONE);
                break;
            case 2:

                if (mHostingYunwFragment == null) {
                    mHostingYunwFragment = new HostingYunwFragment();
                    ft2.add(R.id.fl_hosting_yunw, mHostingYunwFragment).commitAllowingStateLoss();
                }
                flHostingMinerMaster.setVisibility(View.GONE);
                flHostingYunw.setVisibility(View.VISIBLE);
                break;
        }
    }
}
