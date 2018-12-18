package com.sly.app.activity.miner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.miner.FreeFragmentPagerAdapter;
import com.sly.app.adapter.notice.NoticeFragmentPagerAdapter;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.CommonUtil2;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

public class MinerHistoryFreeActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.heath_tab)
    TabLayout heathTab;
    @BindView(R.id.viewpager_heath_tab)
    ViewPager viewpagerHeathTab;

    private FreeFragmentPagerAdapter mTabAdapter;
    private String machineCode, Model;
    private String machineSysCode;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_history_free;
    }

    @Override
    protected void initViewsAndEvents() {
        initViews();
        intitNewsCount();
    }

    private void initViews() {

        machineSysCode = getIntent().getExtras().getString("machineSysCode");
        machineCode = getIntent().getExtras().getString("machineCode");
        Model = getIntent().getExtras().getString("Model");

        mTabAdapter = new FreeFragmentPagerAdapter(getSupportFragmentManager());
        mTabAdapter.addTab(getString(R.string.master_free_repair));
        mTabAdapter.addTab(getString(R.string.master_free_manage));
        viewpagerHeathTab.setOffscreenPageLimit(2);
        viewpagerHeathTab.setAdapter(mTabAdapter);
        heathTab.setupWithViewPager(viewpagerHeathTab);
//        setTabWidth(heathTab, CommonUtil2.dp2px(this, 43));

        heathTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(getString(R.string.master_free_repair))) {

                }
                else if(tab.getText().equals(getString(R.string.master_free_manage))) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if ("0".equals(count)) {
            tvRedNum.setVisibility(View.GONE);
        } else {
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    public String[] getStringArray(){
        String[] parms = new String[3];
        parms[0] = machineSysCode;
        parms[1] = machineCode;
        parms[2] = Model;
        return parms;
    }

    @OnClick({R.id.btn_main_back, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case  R.id.btn_main_back:
                finish();
                break;
            case  R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
        }
    }


    // 设置tab下划线长度
    public void setTabWidth(final TabLayout tabLayout, final int padding){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);



                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
