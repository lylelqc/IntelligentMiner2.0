package com.sly.app.activity.yunw.repair;

import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.fragment.yunw.repair_bill.TreatedFragment;
import com.sly.app.utils.AppUtils;
import com.sly.app.view.PopupView.Yunw.RepairCheckPopView;

import butterknife.BindView;
import butterknife.OnClick;

public class RepairHistoryActivity extends BaseActivity implements RepairCheckPopView.OnSearchClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.rl_repair_history_check)
    RelativeLayout rlRepairHistoryCheck;
    @BindView(R.id.tv_repair_check)
    TextView tvRepairCheck;
    @BindView(R.id.tv_shadow)
    TextView tvShadow;

    private TreatedFragment mTreatedFragment;
    private RepairCheckPopView mRepairCheckView;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repair_history;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.repair_history));
        mTreatedFragment = new TreatedFragment();
        intitNewsCount();
        // 传入矿机编号
        mTreatedFragment.modifyMachineSysCode(getIntent().getExtras().getString("MachineSysCode"));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_repair_history, mTreatedFragment).commit();

    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    @OnClick({R.id.btn_main_back, R.id.rl_repair_history_check, R.id.tv_shadow, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_repair_history_check:
                tvRepairCheck.setTextColor(Color.parseColor("#4789f0"));
                tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuocolor_weixiudan_icon),
                        null, null, null);
                tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(this, 5));

                mRepairCheckView = new RepairCheckPopView(this, 3);
                mRepairCheckView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        tvShadow.setVisibility(View.GONE);
                        tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuo_weixiudan_icon),
                                null, null, null);
                        tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(mContext, 5));
                        tvRepairCheck.setTextColor(getResources().getColor(R.color.sly_text_244153));
                    }
                });
                mRepairCheckView.showFilterPopup(rlRepairHistoryCheck);
                mRepairCheckView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                tvShadow.setAnimation(appearAnimation);
                tvShadow.setVisibility(View.VISIBLE);
                /*tvShadow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvShadow.setVisibility(View.VISIBLE);
                    }
                },170);*/
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        String[] info = mRepairCheckView.getTextInfo();
        String BillNo = AppUtils.isBlank(info[0]) ? "" : info[0];
        String IP = AppUtils.isBlank(info[1]) ? "" : info[1];
        String beginTime1 = AppUtils.isBlank(info[2]) ? "" : info[2];
        String beginTime2 = AppUtils.isBlank(info[3]) ? "" : info[3];
        String endTime1 = AppUtils.isBlank(info[4]) ? "" : info[4];
        String endTime2 = AppUtils.isBlank(info[5]) ? "" : info[5];

        //状态判断
        String status = info[6].equals("true") ? "04" : "06";

        // fragment更新数据
        mTreatedFragment.modifyCondition(BillNo, IP, beginTime1, beginTime2, endTime1, endTime2, status);
    }
}
