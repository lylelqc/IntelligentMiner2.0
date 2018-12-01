package com.sly.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.comm.AppContext;
import com.sly.app.fragment.EmptyFragment;
import com.sly.app.fragment.Sly2YunwFragment;
import com.sly.app.fragment.sly.SlyHomeFragment;
import com.sly.app.fragment.sly.SlyMineFragment2;
import com.sly.app.listener.OnPopupItemOnClickListener;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.LoginMsgHelper;
import com.sly.app.view.NoScrollViewpager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnPopupItemOnClickListener {

    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_tuoguan)
    LinearLayout llTuoguan;
    @BindView(R.id.ll_jiwei)
    LinearLayout llJiwei;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    @BindView(R.id.main_bottomtab_layout)
    LinearLayout mainBottomtabLayout;
    @BindView(R.id.vpager)
    NoScrollViewpager vpager;
    private MainFragmentAdapter mAdapter;
    public final String[] tabCount = new String[]{"首页", "托管", "机位信息", "我的"};

    private static final String STATUS_KEY = "status";

    public static final String HOME ="1";
    public static final String HOSTING ="2";
    public static final String MACHINE ="3";
    public static final String MINE ="4";
    private static final String[] STATUS={HOME,HOSTING,MACHINE,MINE};
    private static final int REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE=0x123;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_main;
    }

    @Override
    protected void initViewsAndEvents() {
        setupViews();
        /**检测app版本**/
//        Beta.checkUpgrade(false, true);
        requestPermission();

        ActivityHelper.getInstance().pushOneActivity(this);

    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= 23){//判断当前系统的版本
            int readPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);//获取系统是否被授予该种权限
            if(readPhonePermission != PackageManager.PERMISSION_GRANTED){//如果没有被授予
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);
                return;//请求获取该种权限
            }
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);//获取系统是否被授予该种权限
            if(checkWriteStoragePermission != PackageManager.PERMISSION_GRANTED){//如果没有被授予
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);
                return;//请求获取该种权限
            }
            int checkReadStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);//获取系统是否被授予该种权限
            if(checkReadStoragePermission != PackageManager.PERMISSION_GRANTED){//如果没有被授予
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);
                return;//请求获取该种权限
            }
            int fineLoocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//获取系统是否被授予该种权限
            if(fineLoocationPermission != PackageManager.PERMISSION_GRANTED){//如果没有被授予
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);
                return;//请求获取该种权限
            }
        }
    }


    private void setupViews() {
        vpager.setNoScroll(true);
        vpager.setOffscreenPageLimit(5);
        mAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        vpager.setAdapter(mAdapter);
        vpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (getIntent().hasExtra(STATUS_KEY)){
            for (int i=0;i<STATUS.length;i++){
                if (getIntent().getStringExtra(STATUS_KEY).equals(STATUS[1])){
                    if (LoginMsgHelper.isLogin(mContext)) {
                        vpager.setCurrentItem(1);
                        tabSelected(llTuoguan);
                    } else {
                        CommonUtil2.goActivity(mContext, LoginActivity.class);
                    }
                }
            }
        }else{
            vpager.setCurrentItem(0);
            tabSelected(llHome);
        }

    }

    @OnClick({R.id.ll_home, R.id.ll_tuoguan, R.id.ll_jiwei, R.id.ll_mine})
    public void onClcik(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
//                toolbarRightBtn.setVisibility(View.GONE);
                vpager.setCurrentItem(0);
                tabSelected(llHome);
//                toolbarLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_tuoguan:
                if (LoginMsgHelper.isLogin(mContext)) {
                    vpager.setCurrentItem(1);
                    tabSelected(llTuoguan);
                } else {
                    CommonUtil2.goActivity(mContext, LoginActivity.class);
                }
//                vpager.setCurrentItem(1);
//                tabSelected(llTuoguan);
//                toolbarLayout.setVisibility(View.VISIBLE);
//                toolbarRightBtn.setVisibility(View.GONE);
                break;
            case R.id.ll_jiwei:
                vpager.setCurrentItem(2);
                tabSelected(llJiwei);
//                toolbarLayout.setVisibility(View.VISIBLE);
//                if (LoginMsgHelper.isLogin(mContext) && LoginMsgHelper.getResult(mContext).getStore() != null && !StringHelper.isEmpty(LoginMsgHelper.getResult(mContext).getStore().getS_id())) {
//                    toolbarRightBtn.setVisibility(View.VISIBLE);
//                } else {
//                    toolbarRightBtn.setVisibility(View.GONE);
//                }
                break;
            case R.id.ll_mine:
//                if (LoginMsgHelper.isLogin(mContext)) {
//
//                } else {
//                    CommonUtil2.goActivity(mContext, LoginActivity.class);
//                }
                vpager.setCurrentItem(3);
                tabSelected(llMine);
//                toolbarLayout.setVisibility(View.VISIBLE);
//                toolbarRightBtn.setVisibility(View.GONE);
                break;
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        llHome.setSelected(false);
        llTuoguan.setSelected(false);
        llJiwei.setSelected(false);
        llMine.setSelected(false);
        linearLayout.setSelected(true);
    }

    @Override
    public void onPupoItemClick(int position) {
        if (position == 0) {

        } else if (position == 1) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class MainFragmentAdapter extends FragmentPagerAdapter {


        private int mCount = tabCount.length;

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return Sly2YunwFragment.newInstance(tabCount[position % tabCount.length]);
            } else if (position == 1) {
                return EmptyFragment.newInstance("1");
            } else if (position == 2) {
                return EmptyFragment.newInstance("2");
            } else {
                return EmptyFragment.newInstance("3");
            }

        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabCount[position % tabCount.length];
        }

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {
                ((AppContext) getApplication()).exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
