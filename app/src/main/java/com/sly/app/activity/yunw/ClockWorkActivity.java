package com.sly.app.activity.yunw;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.ClockWorkBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class ClockWorkActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.tv_work_yuan)
    TextView tvWorkYuan;
    @BindView(R.id.tv_work_line)
    TextView tvWorkLine;

    @BindView(R.id.ll_clock_in_time)
    LinearLayout llClockInTime;
    @BindView(R.id.tv_begin_work_time)
    TextView tvBeginTime;
    @BindView(R.id.btn_begin_work)
    Button btnBeginWork;

    @BindView(R.id.ll_end_work)
    LinearLayout llEndWork;
    @BindView(R.id.btn_end_work)
    Button btnEndWork;

    private String User, LoginType, MineCode, PersonSysCode, Token, Key;
    ICommonRequestPresenter iCommonRequestPresenter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_clock_work;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.machine_clock_work));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_YUNW_BEGIN_WORK_STATUS);
//        toRequest(NetConstant.EventTags.GET_YUNW_END_WORK_STATUS);
        toRequest(NetConstant.EventTags.GET_YUNW_WORK_TIME);
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

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);
        if (NetConstant.EventTags.GET_YUNW_BEGIN_WORK_STATUS == eventTag) {
            map.put("Rounter", NetConstant.GET_YUNW_BEGIN_WORK_STATUS);

        } /*else if (NetConstant.EventTags.GET_YUNW_END_WORK_STATUS == eventTag) {
            map.put("Rounter", NetConstant.GET_YUNW_END_WORK_STATUS);

        }*/ else if (NetConstant.EventTags.GET_YUNW_SET_WORK_STATUS == eventTag) {
            map.put("Rounter", NetConstant.GET_YUNW_SET_WORK_STATUS);

        } else if (NetConstant.EventTags.GET_YUNW_SET_NO_WORK_STATUS == eventTag) {
            map.put("Rounter", NetConstant.GET_YUNW_SET_NO_WORK_STATUS);

        } else if (NetConstant.EventTags.GET_YUNW_WORK_TIME == eventTag) {
            map.put("Rounter", NetConstant.GET_YUNW_WORK_TIME);

        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);

    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("上下班返回数据 - " + result);
        if (NetConstant.EventTags.GET_YUNW_BEGIN_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                if ((returnBean.getData()).equals("true")) {
                    showIsWorkView(false);
                } else {
                    showIsWorkView(true);
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }

        } /*else if (NetConstant.EventTags.GET_YUNW_END_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                if ((returnBean.getData()).equals("true")) {
                    tvWorkYuan.setVisibility(View.GONE);
                    tvWorkLine.setVisibility(View.VISIBLE);
                    llClockInTime.setVisibility(View.VISIBLE);
                    llEndWork.setVisibility(View.VISIBLE);
                } else {
                    tvWorkYuan.setVisibility(View.VISIBLE);
                    tvWorkLine.setVisibility(View.GONE);
                    llEndWork.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }

        }*/ else if (NetConstant.EventTags.GET_YUNW_SET_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                toRequest(NetConstant.EventTags.GET_YUNW_WORK_TIME);
                showIsWorkView(true);
            }

        } else if (NetConstant.EventTags.GET_YUNW_SET_NO_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                showIsWorkView(false);
            }
        } else if (NetConstant.EventTags.GET_YUNW_WORK_TIME == eventTag) {
            List<ClockWorkBean> beanList =
                    (List<ClockWorkBean>) AppUtils.parseRowsResult(result, ClockWorkBean.class);
            if (beanList != null && beanList.size() > 0) {
                ClockWorkBean bean = beanList.get(0);
                if (bean.getMine84_BeginTime() != null) {
                    tvBeginTime.setText(bean.getMine84_BeginTime());
                }
            }
        }
    }

    private void showIsWorkView(boolean isWork) {
        tvWorkYuan.setVisibility(isWork ? View.GONE : View.VISIBLE);
        tvWorkLine.setVisibility(isWork ? View.VISIBLE : View.GONE);
        llClockInTime.setVisibility(isWork ? View.VISIBLE : View.GONE);
        llEndWork.setVisibility(isWork ? View.VISIBLE : View.GONE);
        btnBeginWork.setVisibility(isWork ? View.GONE : View.VISIBLE);
        btnEndWork.setVisibility(isWork ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    @OnClick({R.id.btn_main_back, R.id.btn_begin_work, R.id.btn_end_work, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.btn_begin_work:
                toRequest(NetConstant.EventTags.GET_YUNW_SET_WORK_STATUS);
                break;
            case R.id.btn_end_work:
                toRequest(NetConstant.EventTags.GET_YUNW_SET_NO_WORK_STATUS);
                break;

        }
    }
}
