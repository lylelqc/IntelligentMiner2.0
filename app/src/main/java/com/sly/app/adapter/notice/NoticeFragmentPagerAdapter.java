package com.sly.app.adapter.notice;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sly.app.fragment.notice.NoticeNewFragment;
import com.sly.app.fragment.notice.NoticeOldFragment;
import com.sly.app.fragment.sly.SlyHomeFragment;
import com.sly.app.fragment.sly.SlyMineFragment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 6/3/16.
 */
public class NoticeFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int MAX_TAB_COUNT = 2;
    private List<CharSequence> mTitleList = new ArrayList<>();

    public NoticeFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addTab(String title) {
        mTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NoticeNewFragment();
            case 1:
                return new NoticeOldFragment();
        }
        return null;
    }

    @Override
    public int getCount() {

        return MAX_TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    /**
     * @deprecated Use {@link #setPageTitle(TabLayout, int, CharSequence)} instead.
     */
    public void setPageTitle(int position, CharSequence title) {
        mTitleList.set(position, title);
    }

    public void setPageTitle(TabLayout tabLayout, int position, CharSequence title) {
        //noinspection deprecation
        setPageTitle(position, title);
        if (position < tabLayout.getTabCount()) {
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (tab != null) {
                tab.setText(title);
            }
        }
    }
}
