package com.sly.app.activity.qrc;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/28 14:21
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ScanResultActivity extends BaseActivity {
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
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_type)
    TextView mTvType;

    private String result = null;
    private String[] data;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("扫码信息");
        if (!CommonUtils.isBlank(result)) {
            data = result.split("&");
            if (data.length != 6) {
                ToastUtils.showToast("请扫码美好地球村用户专属的二维码,谢谢！");
            } else {
                if (!CommonUtils.isBlank(data[3])) {
                    mTvNickname.setText(data[3]);
                } else {
                    mTvNickname.setText(CommonUtils.replaceStr(3, 7, "****", data[1]));
                }
                mTvType.setText(data[4]);
                Logcat.i("--------" + data[2]);
                GlideImgManager.glideLoader(this, data[2].replace("9897","9898"), R.drawable.h_portrait, R.drawable.h_portrait, mIvIcon);
            }
        }
    }

    @Override
    protected void initData() {
        result = getIntent().getExtras().getString("CodeInfo");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scan_result;
    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.btn_main_back, R.id.tv_main_right, R.id.ll_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
