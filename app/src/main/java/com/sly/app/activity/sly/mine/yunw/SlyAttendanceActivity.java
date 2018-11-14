package com.sly.app.activity.sly.mine.yunw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.AttendanceBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
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

public class SlyAttendanceActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_begin_work_time)
    TextView tvBeginTime;
    @BindView(R.id.tv_work_yuan)
    TextView tvWorkYuan;
    @BindView(R.id.tv_work_line)
    TextView tvWorkLine;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.btn_begin_work)
    Button btnBeginWork;
    @BindView(R.id.btn_end_work)
    Button btnEndWork;
    @BindView(R.id.ll_end_work)
    LinearLayout llEndWork;

    private String User, LoginType, MineCode, PersonSysCode, Token, Key;
    ICommonRequestPresenter iCommonRequestPresenter;
    private boolean isBeginWork = false;
    private boolean isEndWork = false;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText("上下班打卡");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        toRequest(NetWorkCons.EventTags.GET_BEGIN_WORK_STATUS);
        toRequest(NetWorkCons.EventTags.GET_END_WORK_STATUS);
        toRequest(NetWorkCons.EventTags.GET_WORK_TIME);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);
        if (NetWorkCons.EventTags.GET_BEGIN_WORK_STATUS == eventTag) {
            map.put("Rounter", NetWorkCons.GET_BEGIN_WORK_STATUS);

        } else if (NetWorkCons.EventTags.GET_END_WORK_STATUS == eventTag) {
            map.put("Rounter", NetWorkCons.GET_END_WORK_STATUS);

        } else if (NetWorkCons.EventTags.SET_WORKING == eventTag) {
            map.put("Rounter", NetWorkCons.SET_WORKING);

        } else if (NetWorkCons.EventTags.SET_NO_WORK == eventTag) {
            map.put("Rounter", NetWorkCons.SET_NO_WORK);

        } else if (NetWorkCons.EventTags.GET_WORK_TIME == eventTag) {
            map.put("Rounter", NetWorkCons.GET_WORK_TIME);

        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);

    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("上下班返回数据 - " + result);
        if (NetWorkCons.EventTags.GET_BEGIN_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                if ((returnBean.getData()).equals("true")) {
                    tvWorkYuan.setVisibility(View.VISIBLE);
                    tvWorkLine.setVisibility(View.GONE);
                    btnBeginWork.setVisibility(View.VISIBLE);
                } else {
                    tvTime1.setVisibility(View.GONE);
                    tvBeginTime.setVisibility(View.GONE);
                    tvWorkYuan.setVisibility(View.GONE);
                    tvWorkLine.setVisibility(View.VISIBLE);
                    btnBeginWork.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }

        } else if (NetWorkCons.EventTags.GET_END_WORK_STATUS == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                if ((returnBean.getData()).equals("true")) {
                    tvWorkYuan.setVisibility(View.GONE);
                    tvWorkLine.setVisibility(View.VISIBLE);
                    tvTime1.setVisibility(View.VISIBLE);
                    tvBeginTime.setVisibility(View.VISIBLE);
                    llEndWork.setVisibility(View.VISIBLE);
                } else {
                    tvWorkYuan.setVisibility(View.VISIBLE);
                    tvWorkLine.setVisibility(View.GONE);
                    llEndWork.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }

        } else if (NetWorkCons.EventTags.SET_WORKING == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                toRequest(NetWorkCons.EventTags.GET_WORK_TIME);
                showDialog("上班打卡成功");
                tvWorkYuan.setVisibility(View.GONE);
                tvWorkLine.setVisibility(View.VISIBLE);
                tvTime1.setVisibility(View.VISIBLE);
                tvBeginTime.setVisibility(View.VISIBLE);
                llEndWork.setVisibility(View.VISIBLE);
                btnBeginWork.setVisibility(View.GONE);
            } else {
                ToastUtils.showToast("上班打卡失败");
            }

        } else if (NetWorkCons.EventTags.SET_NO_WORK == eventTag) {
            final ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1")) {
                showDialog("下班打卡成功");
                tvWorkLine.setVisibility(View.GONE);
                tvWorkYuan.setVisibility(View.VISIBLE);
                llEndWork.setVisibility(View.GONE);
                tvTime1.setVisibility(View.GONE);
                tvBeginTime.setVisibility(View.GONE);
                btnBeginWork.setVisibility(View.VISIBLE);
            } else {
                ToastUtils.showToast("下班打卡失败");
            }

        } else if (NetWorkCons.EventTags.GET_WORK_TIME == eventTag) {
            final SlyReturnListBean returnBean = JSON.parseObject(result, SlyReturnListBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getData().getRows().size() > 0) {
                List<AttendanceBean> attendanceBeans = JSON.parseArray(returnBean.getData().getRows().toString(), AttendanceBean.class);
                AttendanceBean bean = attendanceBeans.get(0);
                if (bean.getMine84_BeginTime() != null) {
                    tvBeginTime.setText(bean.getMine84_BeginTime());
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }

        }
    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(SlyAttendanceActivity.this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

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

    @OnClick({R.id.btn_main_back, R.id.btn_begin_work, R.id.btn_end_work})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_begin_work:
                toRequest(NetWorkCons.EventTags.SET_WORKING);
                break;
            case R.id.btn_end_work:
                toRequest(NetWorkCons.EventTags.SET_NO_WORK);
                break;

        }
    }
}
