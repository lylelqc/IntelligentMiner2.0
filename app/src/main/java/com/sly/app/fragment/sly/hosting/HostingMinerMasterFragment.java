package com.sly.app.fragment.sly.hosting;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.sly.app.R;
import com.sly.app.adapter.hosting.HostingFragmentPagerAdapter;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.utils.CommonUtils;

import butterknife.BindView;

public class HostingMinerMasterFragment extends BaseFragment {

    @BindView(R.id.heath_tab)
    TabLayout heathTab;
    @BindView(R.id.viewpager_heath_tab)
    ViewPager viewpagerHeathTab;
    @BindView(R.id.top)
    LinearLayout top;

    private HostingFragmentPagerAdapter mTabAdapter;

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
        mTabAdapter = new HostingFragmentPagerAdapter(getFragmentManager());
        mTabAdapter.addTab(getString(R.string.hosting_equipment));
        mTabAdapter.addTab(getString(R.string.hosting_repair_bill));
        viewpagerHeathTab.setOffscreenPageLimit(2);
        viewpagerHeathTab.setAdapter(mTabAdapter);

        //设置分割线
        LinearLayout linearLayout = (LinearLayout) heathTab.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(mContext, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(CommonUtils.dip2px(mContext, 13));

        heathTab.setupWithViewPager(viewpagerHeathTab);
        heathTab.setTabMode(TabLayout.MODE_FIXED);


        heathTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hosting_miner_master;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}
