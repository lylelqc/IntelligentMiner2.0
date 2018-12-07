package com.sly.app.activity.mine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.fragment.group.AchievementFragment;
import com.sly.app.fragment.group.CommissionFragment;
import com.sly.app.fragment.group.TeamFragment;
import com.sly.app.view.MainBottomBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class AssociationActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.mb_group)
    MainBottomBar mMbGroup;
    @BindView(R.id.fl_group)
    FrameLayout mFlGroup;
    private FragmentManager mFragmentManager;
    private int index=0;
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("消费社群");
        initMainButtom();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.acctivity_association;
    }

    @Override
    protected void setListener() {

    }


    private void initMainButtom() {
        mFragmentManager = getSupportFragmentManager();
        mMbGroup.setCallBack(new MainBottomBar.CallBack() {

            @Override
            public void call(int prevIndex, int currentIndex) {
                // TODO Auto-generated method stub
                // 获取当前fragment和切换到的fragment
                Fragment perFragment = mFragmentManager.findFragmentByTag("tag" + prevIndex);
                Fragment currentFragment = mFragmentManager.findFragmentByTag("tag" + currentIndex);
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (perFragment != null) {
                    ft.hide(perFragment);
                }
                if (currentFragment == null) {
                    currentFragment = getBottomTabFragment(currentIndex);
                    ft.add(R.id.fl_group, currentFragment, "tag" + currentIndex);
                } else {
                    ft.show(currentFragment);
                }
                ft.commitAllowingStateLoss();
            }
        });
        mMbGroup.setSelected(index);
    }

    /**
     * 返回bottomTabFragment
     *
     * @param index
     * @return
     */

    private Fragment getBottomTabFragment(int index) {
        Fragment mFragment = null;
        switch (index) {
            case 0:
                mFragment = new TeamFragment();
                break;
            case 1:
                mFragment = new AchievementFragment();
                break;
            case 2:
                mFragment = new CommissionFragment();
                break;
        }
        return mFragment;
    }

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
