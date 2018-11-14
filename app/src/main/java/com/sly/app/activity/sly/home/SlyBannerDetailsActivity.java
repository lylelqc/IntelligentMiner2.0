package com.sly.app.activity.sly.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SlyBannerDetailsActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.iv_banner_details)
    TextView ivBannerDetails;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_banner_details;
    }

    @Override
    protected void initViewsAndEvents() {
        String bannerType = getIntent().getStringExtra("bannerType");
        if(bannerType.equals("MineMaster")){
            tvMainTitle.setText("如何成为矿场主");
            ivBannerDetails.setBackground(getResources().getDrawable(R.drawable.became_tiaozhuan_link));
        }
        else if(bannerType.equals("Equity")){
            tvMainTitle.setText("矿场主和矿工权益");
            ivBannerDetails.setBackground(getResources().getDrawable(R.drawable.quanyi_tiaozhuan_link));
        }else{
            tvMainTitle.setText("如何发布机位信息");
            ivBannerDetails.setBackground(getResources().getDrawable(R.drawable.fabu_tiaozhuan_link));
        }
    }

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
