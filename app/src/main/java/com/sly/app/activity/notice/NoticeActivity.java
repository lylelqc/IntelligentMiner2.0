package com.sly.app.activity.notice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.fragment.notice.LogisticsFragment;
import com.sly.app.fragment.notice.NoticeFragment;
import com.sly.app.fragment.notice.PushFragment;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MainBottomBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 作者 by K
 * 时间：on 2017/9/23 16:14
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class NoticeActivity extends BaseActivity {
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
    @BindView(R.id.mb_notice)
    MainBottomBar mMbNotice;
    @BindView(R.id.fl_mag)
    FrameLayout mFlMag;
    private FragmentManager mFragmentManager;
    private int index=0;
    private String Token, MemberCode;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("平台消息");
        initMainButtom();
    }

    @Override
    protected void initData() {
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
        if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
            ToastUtils.showToast("参数异常");
        } else {
            proCleanMessageWarn(MemberCode, Token);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void setListener() {

    }

    private void initMainButtom() {
        mFragmentManager = getSupportFragmentManager();
        mMbNotice.setCallBack(new MainBottomBar.CallBack() {

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
                    ft.add(R.id.fl_mag, currentFragment, "tag" + currentIndex);
                } else {
                    ft.show(currentFragment);
                }
                ft.commitAllowingStateLoss();
            }
        });
        mMbNotice.setSelected(index);
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
                mFragment =new PushFragment();
                break;
            case 1:
                mFragment = new NoticeFragment();
                break;
            case 2:
                mFragment = new LogisticsFragment();
                break;
        }
        return mFragment;
    }

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
    private void proCleanMessageWarn(String memberCode,String token) {
        final Map<String, String> map = new HashMap();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.CleanMessageWarn, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkConstant.CleanMessageWarn, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                } else {
                 //   ToastUtils.showToast(mReturnBean.getMsg());
                    Logcat.i("清除通知："+mReturnBean.getMsg());
                }
            }
        });
    }

}
