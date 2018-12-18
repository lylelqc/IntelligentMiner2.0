package com.sly.app.fragment.sly.hosting;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.master.AuthAccountSetMineActivity;
import com.sly.app.activity.master.MasterAccountExecActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.machine.MachineManageActivity;
import com.sly.app.comm.BusEvent;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.fragment.Sly2MasterFragment;
import com.sly.app.fragment.Sly2MinerFragment;
import com.sly.app.fragment.Sly2YunwFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static de.greenrobot.event.EventBus.TAG;

public class SlyHostingFragment2 extends BaseFragment implements ICommonViewUi {

    @BindView(R.id.rl_user_type)
    RelativeLayout rlUserType;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_user_account)
    TextView tvUserAccount;

    @BindView(R.id.tv_main_right_left)
    TextView tvMainRightLeft;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.fl_hosting_miner)
    FrameLayout flHostingMiner;
    @BindView(R.id.fl_hosting_mine_master)
    FrameLayout flHostingMinerMaster;
    @BindView(R.id.fl_hosting_yunw)
    FrameLayout flHostingYunw;

    public static String mContent = "???";
    private Sly2MinerFragment mMinerFragment;
    private Sly2MasterFragment mMasterFragment;
    private Sly2YunwFragment mYunwFragment;
    private FragmentTransaction ft1;
    private FragmentTransaction ft2;
    private FragmentTransaction ft3;
    private String mineType;

    private String User, LoginType, FrSysCode, FMasterCode, MineCode, PersonSysCode, Token, Key;
    private String ChildAccount;
    private PopupWindow mPopupWindow;
    private CommonRequestPresenterImpl iCommonRequestPresenter;

    public static SlyHostingFragment2 newInstance(String content) {
        SlyHostingFragment2 fragment = new SlyHostingFragment2();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hosting2;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (BusEvent.UPDATE_HOSTING_OPERATION_DATA.equals(postResult.getTag())) {
            showFragment(2, false);
        } else if (BusEvent.UPDATE_HOSTING_MASTER_DATA.equals(postResult.getTag())) {
            showFragment(1, false);
        } else if (BusEvent.UPDATE_HOSTING_MINER_DATA.equals(postResult.getTag())) {
            showFragment(0, false);
        } else if (EventBusTags.CHOOSE_AUTH_MINE_AREA.equals(postResult.getTag())) {
            showFragment(1, true);
        }
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");
        ChildAccount = SharedPreferencesUtil.getString(mContext, "ChildAccount", "None");

        ft1 = getFragmentManager().beginTransaction();
        ft2 = getFragmentManager().beginTransaction();
        ft3 = getFragmentManager().beginTransaction();

        if (mineType != null && !"None".equals(mineType)) {
            if (mineType.equals("Operation")) {
                showFragment(2, false);

            } else if (mineType.equals("MinerMaster")) {
                showFragment(1, false);
            } else if (mineType.equals("Miner")) {
                showFragment(0, false);
            }
        }

        if (!AppUtils.isBlank(ChildAccount) && !("None").equals(ChildAccount)) {
            if (mMasterFragment == null) {
                mMasterFragment = new Sly2MasterFragment();
                ft2.add(R.id.fl_hosting_mine_master, mMasterFragment).commitAllowingStateLoss();
            }
        }

        if (mYunwFragment == null) {
            mYunwFragment = new Sly2YunwFragment();
            ft3.add(R.id.fl_hosting_yunw, mYunwFragment).commitAllowingStateLoss();
        }

        if (!"None".equals(mineType)) {
            if (("Operation").equals(mineType)) {

            }
            else if (("MinerMaster").equals(mineType)) {

            }
            else{

            }
        }
    }

    public void showFragment(int index, boolean isChildAccount) {
        switch (index) {
            case 0:
                if (mMinerFragment == null) {
                    mMinerFragment = new Sly2MinerFragment();
                    ft1.add(R.id.fl_hosting_miner, mMinerFragment).commitAllowingStateLoss();
                }
                flHostingMiner.setVisibility(View.VISIBLE);
                flHostingMinerMaster.setVisibility(View.GONE);
                flHostingYunw.setVisibility(View.GONE);

                setHeaderTitle(0);
                toRequest(NetConstant.EventTags.GET_MINER_NEW_COUNT);
                break;
            case 1:
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */

//                if (mHostingMinerMasterFragment == null) {
//                    mHostingMinerMasterFragment = new HostingMinerMasterFragment();
//                    ft1.add(R.id.fl_hosting_miner_master, mHostingMinerMasterFragment).commitAllowingStateLoss();
//
//                }

                if (mMasterFragment == null) {
                    mMasterFragment = new Sly2MasterFragment();
                    ft2.add(R.id.fl_hosting_mine_master, mMasterFragment).commitAllowingStateLoss();
                }
                flHostingMiner.setVisibility(View.GONE);
                flHostingMinerMaster.setVisibility(View.VISIBLE);
                flHostingYunw.setVisibility(View.GONE);
                if (isChildAccount) {
                    setHeaderTitle(3);
                } else {
                    setHeaderTitle(1);
                    toRequest(NetConstant.EventTags.GET_MASTER_NEW_COUNT);
                }

                break;
            case 2:

                if (mYunwFragment == null) {
                    mYunwFragment = new Sly2YunwFragment();
                    ft3.add(R.id.fl_hosting_yunw, mYunwFragment).commitAllowingStateLoss();
                }
                flHostingMiner.setVisibility(View.GONE);
                flHostingMinerMaster.setVisibility(View.GONE);
                flHostingYunw.setVisibility(View.VISIBLE);
                setHeaderTitle(2);
                toRequest(NetConstant.EventTags.GET_YUNW_NEWS_COUNT);
                break;
        }
    }

    private void setHeaderTitle(int tag) {
        if (tag == 0) {
            tvUserType.setText(mContext.getResources().getString(R.string.user_type_miner));
            tvUserAccount.setVisibility(View.VISIBLE);
            String maskNumber = User.substring(0, 3) + "****" + User.substring(7, User.length());
            tvUserAccount.setText(maskNumber);
            tvMainRightLeft.setVisibility(View.GONE);
        } else if (tag == 1) {
            tvUserType.setText(mContext.getResources().getString(R.string.user_type_master));
            tvUserAccount.setVisibility(View.VISIBLE);
            String maskNumber = User.substring(0, 3) + "****" + User.substring(7, User.length());
            tvUserAccount.setText(maskNumber);
            tvMainRightLeft.setText("");
            tvMainRightLeft.setVisibility(View.VISIBLE);
            tvMainRightLeft.setBackground(mContext.getResources().getDrawable(R.drawable.mine_owner_add));
        } else if (tag == 2) {
            tvUserType.setText(mContext.getResources().getString(R.string.user_type_operation));
            tvUserAccount.setVisibility(View.GONE);
            tvMainRightLeft.setText("");
            tvMainRightLeft.setVisibility(View.VISIBLE);
            tvMainRightLeft.setBackground(null);
            tvMainRightLeft.setText(mContext.getResources().getString(R.string.machine_manage));
        } else if (tag == 3) {
            tvMainRightLeft.setVisibility(View.GONE);
            mMasterFragment.getActivityResult();
        }
    }

    @OnClick({R.id.rl_user_type, R.id.tv_main_right_left, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_type:
                showPopupWindow();
                break;
            case R.id.tv_main_right_left:
                mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");
                if (!"None".equals(mineType)) {
                    if (("Operation").equals(mineType)) {
                        AppUtils.goActivity(mContext, MachineManageActivity.class);
                    } else if (("MinerMaster").equals(mineType)) {
                        mMasterFragment.startActivitys();
                    }
                }
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(mContext, Sly2NoticeActivity.class);
                break;

        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View contentView = inflater.inflate(R.layout.mine_popupwindow, null, false);

            mPopupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAsDropDown(rlUserType, 10, 5);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(00000000));

            TextView miner = contentView.findViewById(R.id.miner);
            TextView mineMaster = contentView.findViewById(R.id.mineMaster);
            TextView operation = contentView.findViewById(R.id.operation);
            TextView authAccount = contentView.findViewById(R.id.auth_account);

            miner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvUserType.setText(mContext.getResources().getString(R.string.user_type_miner));
                    SharedPreferencesUtil.putString(mContext, "LoginType", "Miner");
                    SharedPreferencesUtil.putString(mContext, "mineType", "Miner");
                    if (!LoginType.equals("Miner")) {
                        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
                    }
                    showFragment(0, false);
                    EventBus.getDefault().post(new PostResult(EventBusTags.SET_MINER_INFO));
                    mPopupWindow.dismiss();
                }
            });

            mineMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!AppUtils.isBlank(FMasterCode) && !"None".equals(FMasterCode)) {
                        tvUserType.setText(mContext.getResources().getString(R.string.user_type_master));
                        SharedPreferencesUtil.putString(mContext, "LoginType", "MinerMaster");
                        SharedPreferencesUtil.putString(mContext, "mineType", "MinerMaster");
                        if (!LoginType.equals("MinerMaster")) {
                            LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
                        }
                        showFragment(1, false);
                        EventBus.getDefault().post(new PostResult(EventBusTags.CLICK_MINE_MASTER));
                    } else {
                        ToastUtils.showToast(mContext.getResources().getString(R.string.no_permission));
                    }
                    mPopupWindow.dismiss();
                }
            });

            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!AppUtils.isBlank(PersonSysCode) && !"None".equals(PersonSysCode)) {
                        tvUserType.setText(mContext.getResources().getString(R.string.user_type_operation));
                        SharedPreferencesUtil.putString(mContext, "mineType", "Operation");
                        EventBus.getDefault().post(new PostResult(EventBusTags.SET_OPERATION_INFO));
                        showFragment(2, false);
                    } else {
                        ToastUtils.showToast(mContext.getResources().getString(R.string.no_permission));
                    }
                    mPopupWindow.dismiss();
                }
            });

            authAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!AppUtils.isBlank(ChildAccount) && !"None".equals(ChildAccount)) {
                        AppUtils.goActivity(mContext, AuthAccountSetMineActivity.class);
                        tvUserType.setText(mContext.getResources().getString(R.string.master_auth_account));
                    } else {
                        ToastUtils.showToast(mContext.getResources().getString(R.string.no_permission));
                    }
                    mPopupWindow.dismiss();
                }
            });
        } else {
            mPopupWindow.showAsDropDown(rlUserType, 10, 5);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if(eventTag == NetConstant.EventTags.GET_YUNW_NEWS_COUNT){
            map.put("Rounter", NetConstant.GET_YUNW_NEWS_COUNT);
            map.put("personSysCode", PersonSysCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_MASTER_NEW_COUNT){
            map.put("Rounter", NetConstant.GET_MASTER_NEW_COUNT);
            map.put("masterSysCode", FMasterCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_NEW_COUNT){
            map.put("Rounter", NetConstant.GET_MINER_NEW_COUNT);
            map.put("minerSysCode", FrSysCode);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
        if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {

            int count = Integer.parseInt(returnBean.getData());
            if (count == 0) {
                tvRedNum.setVisibility(View.GONE);
            } else if (count > 99) {
                tvRedNum.setText("99+");
                tvRedNum.setVisibility(View.VISIBLE);
            } else {
                tvRedNum.setText(returnBean.getData());
                tvRedNum.setVisibility(View.VISIBLE);
            }
            SharedPreferencesUtil.putString(mContext, "NewsCount", returnBean.getData());
        }
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
}
