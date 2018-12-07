package com.sly.app.activity.yunw.repair;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.yunw.repair.RepairFragmentPaperAdapter;
import com.sly.app.comm.EventBusTags;
import com.sly.app.fragment.yunw.repair_bill.TreatedFragment;
import com.sly.app.fragment.yunw.repair_bill.TreatingFragment;
import com.sly.app.fragment.yunw.repair_bill.UntreatedFragment;
import com.sly.app.model.PostResult;
import com.sly.app.utils.AppUtils;
import com.sly.app.view.PopupView.Yunw.RepairCheckPopView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

public class RepairBillActivity extends BaseActivity implements RepairCheckPopView.OnSearchClickListener{

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.heath_tab)
    TabLayout heathTab;
    @BindView(R.id.viewpager_heath_tab)
    ViewPager viewpagerHeathTab;
    @BindView(R.id.rl_repair_check)
    RelativeLayout rlRepairCheck;
    @BindView(R.id.tv_repair_check)
    TextView tvRepairCheck;
    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    private RepairFragmentPaperAdapter mTabAdapter;
    private RepairCheckPopView mRepairCheckView;
    private int count = 1;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly2_repair_bill;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if(EventBusTags.JUMP_REPAIR_BILL_TREATING.equals(postResult.getTag())){
            heathTab.getTabAt(1).select();
        }else if(EventBusTags.JUMP_REPAIR_BILL_TREATED.equals(postResult.getTag())){
            heathTab.getTabAt(2).select();
        }
    }

    @Override
    protected void initViewsAndEvents() {
        initViews();

    }

    private void initViews() {
        tvMainTitle.setText(getString(R.string.repair_bill));
        mTabAdapter = new RepairFragmentPaperAdapter(getSupportFragmentManager());
        mTabAdapter.addTab(getString(R.string.repair_untreated));
        mTabAdapter.addTab(getString(R.string.repair_treating));
        mTabAdapter.addTab(getString(R.string.repair_treated));
        viewpagerHeathTab.setOffscreenPageLimit(3);
        viewpagerHeathTab.setAdapter(mTabAdapter);
        heathTab.setupWithViewPager(viewpagerHeathTab);
        setTabWidth(heathTab, AppUtils.dp2px(this, 10));

        heathTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(getString(R.string.repair_untreated))) {
                    count = 1;
                }
                else if(tab.getText().equals(getString(R.string.repair_treating))) {
                    count = 2;
                }else{
                    count = 3;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 未读消息数量
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
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

    @OnClick({R.id.btn_main_back, R.id.rl_repair_check, R.id.tv_shadow, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_repair_check:
                tvRepairCheck.setTextColor(Color.parseColor("#4789f0"));
                tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuocolor_weixiudan_icon),
                        null, null, null);
                tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(this, 5));

                mRepairCheckView = new RepairCheckPopView(this, count);
                mRepairCheckView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                        tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuo_weixiudan_icon),
                                null, null, null);
                        tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(RepairBillActivity.this, 5));
                        tvRepairCheck.setTextColor(getResources().getColor(R.color.sly_text_244153));
                    }
                });
                mRepairCheckView.showFilterPopup(rlRepairCheck);
                mRepairCheckView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if(mRepairCheckView != null ){
            String[] info = mRepairCheckView.getTextInfo();
            String BillNo = AppUtils.isBlank(info[0]) ? "" : info[0];
            String IP = AppUtils.isBlank(info[1]) ? "" : info[1];
            String beginTime1 = AppUtils.isBlank(info[2]) ? "" : info[2];
            String beginTime2 = AppUtils.isBlank(info[3]) ? "" : info[3];
            String endTime1 = AppUtils.isBlank(info[4]) ? "" : info[4];
            String endTime2 = AppUtils.isBlank(info[5]) ? "" : info[5];

            //状态判断
            String status = "";
            if(count == 2){
                status = info[6].equals("true") ? "02" : "03";
            }else if(count == 3){
                status = info[6].equals("true") ? "04" : "06";
            }

            // fragment更新数据
            if (mTabAdapter != null && viewpagerHeathTab != null)
            {
                Object obj = mTabAdapter.instantiateItem(viewpagerHeathTab, count-1);

                if (obj != null && count == 1)
                {
                    UntreatedFragment fragment = (UntreatedFragment) obj;
                    fragment.modifyCondition(BillNo, IP, beginTime1, beginTime2);
                }
                else if (obj != null && count == 2)
                {
                    TreatingFragment fragment = (TreatingFragment) obj;
                    fragment.modifyCondition(BillNo, IP, beginTime1, beginTime2, status);
                }
                else if (obj != null && count == 3)
                {
                    TreatedFragment fragment = (TreatedFragment) obj;
                    fragment.modifyCondition(BillNo, IP, beginTime1, beginTime2, endTime1, endTime2, status);
                }
            }
        }
    }

}
