package com.sly.app.fragment.sly;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.device.MinersDeviceActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.activity.sly.mine.IdCardActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.sly.mine.yunw.SlyAttendanceActivity;
import com.sly.app.activity.sly.mine.yunw.SlyMachineManagerActivity;
import com.sly.app.activity.sly.mine.yunw.SlyMachineMonitorActivity;
import com.sly.app.activity.sly.mine.yunw.SlyMachineOnlineActivity;
import com.sly.app.activity.sly.mine.SlyMineBalanceActivity;
import com.sly.app.activity.sly.mine.notice.SlyNoticeActivity;
import com.sly.app.activity.sly.mine.yunw.SlyRepairDocActivity;
import com.sly.app.activity.sly.mine.SlySettingActivity;
import com.sly.app.activity.sly.mine.UserInfoEditActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.AllDMinerMasterTableBean;
import com.sly.app.model.sly.AllDMinerTableBean;
import com.sly.app.model.sly.KgFullInfoBean;
import com.sly.app.model.sly.MinerMasterInfoBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.model.sly.YwFullInfoBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;


/**
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class SlyMineFragment2 extends BaseFragment {
    Unbinder unbinder;
    public static String mContent = "???";
    @BindView(R.id.mine)
    TextView mine;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.shezhi)
    TextView shezhi;
    @BindView(R.id.iv_head_icon)
    ImageView ivHeadIcon;
    @BindView(R.id.tv_main_username)
    TextView tvMainUsername;
    @BindView(R.id.tv_main_msg)
    TextView tvMainMsg;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.ll_huiyuan)
    LinearLayout llHuiyuan;
    @BindView(R.id.tv_account)
    ImageView tvAccount;
    @BindView(R.id.rl_member)
    RelativeLayout rlMember;
    @BindView(R.id.rl_layout)
    LinearLayout rlLayout;
    @BindView(R.id.rl_balance)
    RelativeLayout rlBalance;
    @BindView(R.id.rl_weixiu)
    RelativeLayout rlWeixiu;
    @BindView(R.id.rl_tongji)
    RelativeLayout rlTongji;
    @BindView(R.id.rl_smrenzheng)
    RelativeLayout rlSmrenzheng;
    @BindView(R.id.rl_tobe_manager)
    RelativeLayout rlTobeManager;
    @BindView(R.id.tv_shebei_count)
    TextView tvShebeiCount;
    @BindView(R.id.rl_shebeicount)
    RelativeLayout rlShebeicount;
    @BindView(R.id.tv_frag_main_balance)
    TextView tvFragMainBalance;
    @BindView(R.id.ll_main_balance)
    LinearLayout llMainBalance;
    @BindView(R.id.ll_ev)
    LinearLayout llEv;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    @BindView(R.id.rl_my_shebei)
    RelativeLayout rlMyShebei;
    @BindView(R.id.rl_online)
    RelativeLayout rlOnline;
    @BindView(R.id.rl_offline)
    RelativeLayout rlOffline;
    @BindView(R.id.rl_shebeiManager)
    RelativeLayout rlShebeiManager;
    @BindView(R.id.rl_weixiudanManager)
    RelativeLayout rlWeixiudangManager;
    @BindView(R.id.rl_monitor)
    RelativeLayout rlMonitor;
    @BindView(R.id.rl_spinner)
    RelativeLayout rlSpinner;
    @BindView(R.id.rl_attendance)
    RelativeLayout rlAttendance;
    @BindView(R.id.tv_xiala)
    TextView tvXiala;
    @BindView(R.id.tv_new_msg_status)
    TextView tvNewsStatus;


//    private String User, SysCode, CusCode, Token, Key;
    private String User, FrSysCode, FMasterCode, PersonSysCode, Token, Key;
    private String LoginType = "None";
    private String mRounter = "";

    private static final String TAG = "SLYMineFragment2";
    private String mineType = "None";
    private int i = 0;
    private String eventType = "";

    private PopupWindow mPopupWindow;

    public static SlyMineFragment2 newInstance(String content) {
        SlyMineFragment2 fragment = new SlyMineFragment2();
        mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {
        if (CommonUtils.isBlank(User) || CommonUtils.isBlank(Token)) {

        } else {
//            String SysCode = LoginType.equals("Miner") ? FrSysCode : FMasterCode;
//            getUserInfo(mContext, LoginType, User, SysCode, Key, Token);//更新数据
        }
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(mContext, "User");
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode");
        PersonSysCode = SharedPreferencesUtil.getString(mContext,"PersonSysCode");
        Key = SharedPreferencesUtil.getString(mContext, "Key");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        SharedPreferencesUtil.putString(getActivity(), "MainFlag", "01");
        mineType = SharedPreferencesUtil.getString(mContext, "mineType");

        Logcat.i("FrSysCode:" + FrSysCode + "FMasterCode:" + FMasterCode + "PersonSysCode:" + PersonSysCode +
                "\n\t" + "Token:" + Token + "\n\t" + "UserInfo:");
        if(LoginType != null && !LoginType.equals("None")){

        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_sly_mine;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.LOGIN_SUCCESS.equals(postResult.getTag())) {
            if(CommonUtils.isBlank(eventType)){
                sendRegistrationID();
                eventType = postResult.getTag();
            }
        }
    }

    @OnClick({R.id.shezhi, R.id.msg, R.id.rl_member, R.id.rl_balance,
            R.id.rl_weixiu, R.id.rl_tongji, R.id.rl_smrenzheng, R.id.rl_shebeicount,
            R.id.rl_my_shebei, R.id.rl_tobe_manager,R.id.mine, R.id.rl_online, R.id.rl_offline,
            R.id.rl_shebeiManager, R.id.rl_weixiudanManager, R.id.rl_monitor, R.id.rl_spinner, R.id.rl_attendance})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        if (StringUtils.isEmpty(Token) || StringUtils.isEmpty(User)) {
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            switch (view.getId()) {
                case R.id.mine:
                case R.id.rl_spinner:
                    showPopupWindow();
                    break;
                case R.id.shezhi:
                    intent.setClass(getActivity(), SlySettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_my_shebei:
//                    intent.setClass(getActivity(), MyMachineActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.msg:
                    CommonUtil2.goActivity(mContext,Sly2NoticeActivity.class);
                    break;
                case R.id.rl_member:
                    //startActivityWithoutExtras(ShareRecordActivity.class);
                    intent.setClass(getActivity(), UserInfoEditActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_balance:
                    intent.setClass(getActivity(), SlyMineBalanceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_weixiu:
//                    intent.setClass(getActivity(), ReplareTaketActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.rl_tongji:
                    intent.setClass(getActivity(), MinersDeviceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_smrenzheng:
                    checkRzStatus(02);
//                    intent.setClass(getActivity(), IdCardActivity.class);
//                    /**实名认证**/
//                    intent.putExtra("tag",02);
//                    startActivity(intent);
                    break;
                case R.id.rl_tobe_manager:
                    toRzIdStatus();
//                    checkRzStatus(01);
//                    intent.setClass(getActivity(), IdCardActivity.class);
//                    /**成为矿场主**/
//                    intent.putExtra("tag",01);
//                    startActivity(intent);
                    break;
                case R.id.rl_shebeicount:
                    break;
                case R.id.rl_online:
                    intent.setClass(getActivity(), SlyMachineOnlineActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_offline:
                    break;
                case R.id.rl_shebeiManager:
                    intent.setClass(getActivity(), SlyMachineManagerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_weixiudanManager:
                    intent.setClass(getActivity(), SlyRepairDocActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_monitor:
                    intent.setClass(getActivity(), SlyMachineMonitorActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_attendance:
                    intent.setClass(getActivity(), SlyAttendanceActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    }

    // 获取未读消息数量
    private void getNewsCount() {
        if(CommonUtils.isBlank(mineType) || "None".equals(mineType)){
            tvNewsStatus.setVisibility(View.GONE);
            return;
        }

        Map map = new HashMap();
        if (mineType.equals("Miner")) {
            map.put("minerCode", FrSysCode);
            map.put("Rounter", "Sly.035");
        } else  if (mineType.equals("MinerMaster")){
            map.put("minerMasterCode", FMasterCode);
            map.put("Rounter", "Sly.036");
        } else {
            map.put("personSysCode", PersonSysCode);
            map.put("Rounter", "Sly.038");
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(getActivity()).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    Logcat.e( "提交参数 - "+json + "  状态码 - " + statusCode + " 返回内容 - "+content);
                    ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);

                    if (mReturnBean.getStatus().equals("1")) {
                        if(mReturnBean.getData().equals("0")){
                            tvNewsStatus.setVisibility(View.GONE);
                        }else{
                            tvNewsStatus.setVisibility(View.VISIBLE);
                            int count = Integer.parseInt(mReturnBean.getData());
                            if(count > 99){
                                tvNewsStatus.setText("99+");
                            }else{
                                tvNewsStatus.setText(mReturnBean.getData());
                            }
                        }
                    } else {
//                            ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(e.toString());
                }
            }


        });
    }

    //是否身份认证
    private void toRzIdStatus(){
        Map map = new HashMap();
        if (LoginType.equals("MinerMaster")) {
            map.put("sysCode", FMasterCode);
        } else {
            map.put("sysCode", FrSysCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.031");
        map.put("User", User);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                    ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                    if (mReturnBean.getStatus().equals("1")) {
                        if(mReturnBean.getData().equals("false")){
                            ToastUtils.showToast("您还未进行身份认证!");
                        }else{
                            checkRzStatus(01);
                        }
                    }
                } catch (Exception e) {
                    Logcat.e("Exception", e.toString());
                }
            }
        });
    }

    // 获取设备总数
    private void getMachineCount() {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        if (LoginType.equals("Miner")) {
            map.put("minerSysCode", FrSysCode);
            map.put("Rounter", "Miner.011");
        } else {
            map.put("mineMasterCode", FMasterCode);
            map.put("Rounter", "MineMaster.015");
        }
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(getActivity()).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    LogCatW(NetWorkCons.BASE_URL + this, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);

                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                        if (LoginType.equals("Miner")) {
                            List<AllDMinerTableBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), AllDMinerTableBean.class);
                            Logcat.e(chartList.toString());
                            AllDMinerTableBean allDataTableBean = chartList.get(0);
                            tvShebeiCount.setText(allDataTableBean.getMachineCount() + "台");

                        } else {
                            List<AllDMinerMasterTableBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), AllDMinerMasterTableBean.class);
                            Logcat.e(chartList.toString());
                            AllDMinerMasterTableBean allDataTableBean = chartList.get(0);
                            tvShebeiCount.setText(allDataTableBean.getMachineCount() + "台");
                        }

                    } else {
//                            ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(e.toString());
                }
            }


        });
    }

    // 查看身份认证状态
    private void checkRzStatus(final int tag) {
        Map map = new HashMap();

        map.put("Token", Token);

        map.put("LoginType", LoginType);
        if (LoginType.equals("Miner") && tag == 01) {
            map.put("Rounter", "Sly.006");
            map.put("sys", FrSysCode);
        } else if (LoginType.equals("Miner")) {
            map.put("Rounter", "Sly.034");
            map.put("minerCode", FrSysCode);
        } else {
            map.put("Rounter", "Sly.034");
            map.put("minerCode", FrSysCode);
        }
        map.put("User", User);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(CommonUtils.proStr(content));
                            if (jsonObject.optString("status").equals("1")) {
                                String jsonStr = CommonUtils.proStr(content);
                                String data = jsonObject.optString("data");
                                Intent intent = new Intent();
                                if (LoginType.equals("Miner") && tag == 01) {
                                    if("false".equals(data)){
                                        ToastUtils.showToast("成为矿场主认证审核中，请耐心等候");
                                    }else{
                                        intent.setClass(mContext, IdCardActivity.class);
                                        intent.putExtra("tag", tag);
                                        startActivity(intent);
                                    }
                                    /*switch (Integer.valueOf(data)) {
                                        case 0:
                                            if ("00".equals(data)) {
                                                ToastUtils.showToast("您已认证成为矿场主，请重新登录");
                                                SharedPreferencesUtil.clearString(mContext, "Discount");
                                                SharedPreferencesUtil.putString(mContext, "Token", null);
                                                SharedPreferencesUtil.putString(mContext, "MemberCode", null);
                                                CommonUtil2.goActivity(mContext, LoginActivity.class);
                                            } else {
                                                intent.setClass(mContext, IdCardActivity.class);
                                                intent.putExtra("tag", tag);
                                                startActivity(intent);
                                            }
                                            break;
                                        case 01:
                                            ToastUtils.showToast("成为矿场主认证审核中，请耐心等候");
                                            break;
                                        case 02:
                                            intent.setClass(mContext, IdCardActivity.class);
                                            intent.putExtra("tag", tag);
                                            startActivity(intent);
                                            break;
                                        case 03:
                                            intent.setClass(mContext, IdCardActivity.class);
                                            intent.putExtra("tag", tag);
                                            startActivity(intent);
                                            break;
                                    }*/
                                } else if (LoginType.equals("Miner") || LoginType.equals("MinerMaster")) {
                                    ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                                    if (returnBean.getStatus().equals("1")) {
                                        if(returnBean.getData().equals("true")){
                                            intent.setClass(mContext, IdCardActivity.class);
                                            intent.putExtra("tag", tag);
                                            startActivity(intent);
                                        }else{
                                            ToastUtils.showToast("您的实名认证还在审核中哦，请耐心等候");
                                        }
                                    }
//                                        status = managerRzStatusBeans.get(0).getMine72_AuditStatusCode();
//                                        SharedPreferencesUtil.putString(mContext, "IdCardName", managerRzStatusBeans.get(0).getMine72_Name());
//
//                                        if ("Waitting".equals(status.trim())) {
//                                            ToastUtils.showToast("您的实名认证还在审核中哦，请耐心等候");
//                                        } else if ("Pass".equals(status.trim())) {
//                                            ToastUtils.showToast("您的实名认证已经审核通过，无需重复认证");
////                                            CommonUtil2.goActivity(mContext,WithdrawActivity.class);
//                                        } else if ("Back".equals(status.trim()) || "Refuse".equals(status.trim())) {
//                                            ToastUtils.showToast("您的实名认证没有通过哦，请重新申请");
//                                            intent.setClass(mContext, IdCardActivity.class);
//                                            intent.putExtra("tag", tag);
//                                            startActivity(intent);
//                                        }
                                } /*else if (LoginType.equals("MinerMaster")) {
                                    SlyReturnListBean slyReturnListBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                                    List<ManagerRzStatusBean> managerRzStatusBeans = JSON.parseArray(slyReturnListBean.getData().getRows().toString(), ManagerRzStatusBean.class);
                                    String statusName = "";
                                    if (managerRzStatusBeans.size() == 0) {
                                        intent.setClass(mContext, IdCardActivity.class);
                                        intent.putExtra("tag", tag);
                                        startActivity(intent);
                                    } else {
                                        statusName = managerRzStatusBeans.get(0).getMine60_AuditStatusName();
                                        SharedPreferencesUtil.putString(mContext, "IdCardName", managerRzStatusBeans.get(0).getMine58_Name());

                                        if ("等待审核".equals(statusName)) {
                                            ToastUtils.showToast("您的实名认证还在审核中哦，请耐心等待");
                                        } else if ("审核通过".equals(statusName)) {
                                            ToastUtils.showToast("您的实名认证已经审核通过，无需重复认证");
//                                            startActivityWithoutExtras(WithdrawActivity.class);
                                        } else if ("审核拒绝".equals(statusName) || "发回修改".equals(statusName)) {
                                            ToastUtils.showToast("您的实名认证没有通过哦，请重新申请");
                                            intent.setClass(mContext, IdCardActivity.class);
                                            intent.putExtra("tag", tag);
                                            startActivity(intent);
                                        }
                                    }
                                }*/
                            } else {
                                ToastUtils.showToast(jsonObject.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

    //登录发送极光注册ID
    private void sendRegistrationID() {
        Map map = new HashMap();
        String User = SharedPreferencesUtil.getString(mContext, "User");
        String Token = SharedPreferencesUtil.getString(mContext, "Token");
        String FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode");
        String FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode");
        String Key = SharedPreferencesUtil.getString(mContext, "Key");
        String LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        String regID = JPushInterface.getRegistrationID(mContext);
        Logcat.e("极光注册ID - " + regID);

        map.put("User", User);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", NetWorkCons.GET_JPUSH_REGISTRATION_ID);
        if (LoginType.equals("Miner")) {
            map.put("sysCode", FrSysCode);
        } else {
            map.put("sysCode", FMasterCode);
        }
        map.put("RegID", regID);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("返回码:" + statusCode + "\n返回参数:" + content + "\n提交的内容：" + json);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.e("参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                        try {
                            ReturnBean returnPushBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (returnPushBean.getStatus().equals("1") && returnPushBean.getMsg().equals("成功")) {

                            } else {
                                ToastUtils.showToast(returnPushBean.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

    // 更新用户信息
    private void getUserInfo(final Context mContext, final String loginType, final String user, final String sysCode, final String userKey, String token) {
        Map map = new HashMap();
        if (loginType.equals("MinerMaster")) {
            mRounter = "MineMaster.002";
            map.put("mineMasterCode", sysCode);
        } else {
            mRounter = "Miner.002";
            map.put("minerSysCode", sysCode);
        }

        mineType = SharedPreferencesUtil.getString(mContext, "mineType","None");
        if(mineType != null && mineType.equals("Operation")){
            mRounter = "Sly.003";
            map.put("personSysCode", PersonSysCode);
        }

        map.put("Token", token);
        map.put("LoginType", loginType);
        map.put("Rounter", mRounter);
        map.put("User", user);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, userKey)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.replace("null", "\"null\"");//替换空字符串
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                        final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                        if (returnBean.getMsg().equals("会话过期或非法访问")) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            return false;
                        }
                        if (returnBean.getStatus().equals("1") && returnBean.getData() != null) {
                            if(mineType != null && mineType.equals("Operation")){
                                YwFullInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), YwFullInfoBean.class);
                                SharedPreferencesUtil.putString(mContext, "MinePersonCode", userInfoBean.getF23_MinePersonCode());
                                SharedPreferencesUtil.putString(mContext, "MineName", userInfoBean.getF23_MineName());
                                SharedPreferencesUtil.putString(mContext, "Notes", userInfoBean.getF23_Optime());
                                SharedPreferencesUtil.putString(mContext, "PersonSysClassCode", userInfoBean.getF23_PersonSysClassCode());
                                SharedPreferencesUtil.putString(mContext, "PersonStatusCode", userInfoBean.getF23_PersonStatusCode());
                                SharedPreferencesUtil.putString(mContext, "PersonClassCode", userInfoBean.getF23_PersonSysClassCode());
                                SharedPreferencesUtil.putString(mContext, "BeginHour    ", userInfoBean.getF23_BeginHour());
                                SharedPreferencesUtil.putString(mContext, "EndHour", userInfoBean.getF23_EndHour());
                                SharedPreferencesUtil.putString(mContext, "IsSetPermission", userInfoBean.getF23_IsSetPermission());
                                SharedPreferencesUtil.putString(mContext, "PermissionUserSysCode", userInfoBean.getF23_PermissionUserSysCode());
                                SharedPreferencesUtil.putString(mContext, "MineCode", userInfoBean.getF23_MineCode());
                                SharedPreferencesUtil.putString(mContext, "Oper", userInfoBean.getF23_Oper());
                                SharedPreferencesUtil.putString(mContext, "Optime", userInfoBean.getF23_Optime());
                                setUserInfo("None", userInfoBean.getF23_MinePersonSysCode(), userInfoBean.getF23_MineName(), "Operation");

//                                EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_OPERATION_DATA));
                            }
                            else{
                                if (loginType.equals("Miner")) {
                                    KgFullInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), KgFullInfoBean.class);
                                    SharedPreferencesUtil.putString(mContext, "UserHeadImg", userInfoBean.getF18_ImageURl());
                                    SharedPreferencesUtil.putString(mContext, "NickName", userInfoBean.getF18_NickName());
                                    SharedPreferencesUtil.putString(mContext, "Name", userInfoBean.getF20_Name());
                                    SharedPreferencesUtil.putString(mContext, "Phone", userInfoBean.getF20_Mobile());
                                    SharedPreferencesUtil.putString(mContext, "Email", userInfoBean.getF20_Email());
                                    setUserInfo(userInfoBean.getF18_ImageURl(), userInfoBean.getF18_MinerSysCode(), userInfoBean.getF18_NickName(), "Miner");

//                                    EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_MINER_DATA));
                                } else {
                                    MinerMasterInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), MinerMasterInfoBean.class);
                                    SharedPreferencesUtil.putString(mContext, "UserHeadImg", userInfoBean.getF35_Pic());
                                    SharedPreferencesUtil.putString(mContext, "NickName", userInfoBean.getF35_NickName());
                                    SharedPreferencesUtil.putString(mContext, "Name", userInfoBean.getF36_Name());
                                    SharedPreferencesUtil.putString(mContext, "Phone", userInfoBean.getF36_Mobile());
                                    SharedPreferencesUtil.putString(mContext, "Email", userInfoBean.getF36_Email());
                                    setUserInfo(userInfoBean.getF35_Pic(), userInfoBean.getF35_MineMasterCode(), userInfoBean.getF35_NickName(), "MinerMaster");

//                                    EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_MASTER_DATA));
                                }
                            }

                        } else {
                            SharedPreferencesUtil.putString(mContext, "UserHeadImg", null);
                            SharedPreferencesUtil.putString(mContext, "NickName", null);
                            SharedPreferencesUtil.putString(mContext, "Name", null);
                            SharedPreferencesUtil.putString(mContext, "Phone", null);
                            SharedPreferencesUtil.putString(mContext, "Email", null);
                        }
//                        ToastUtils.showToast(returnBean.getMsg());
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );
    }

    //设置用户信息
    private void setUserInfo(String headIconUrl, String userSysCodeId, String name, String loginType) {
        GlideImgManager.glideLoader(mContext, NetWorkCons.IMG_URL + headIconUrl, R.drawable.my_photo_no, R.drawable.my_photo_no, ivHeadIcon);
        if (CommonUtil2.isEmpty(name)) {
            tvMainUsername.setText("未设置昵称");
        } else {
            tvMainUsername.setText(name);
        }
        tvMainMsg.setText("ID:" + userSysCodeId);
        if (loginType.equals("Miner")) {
//            rlMyShebei.setVisibility(View.VISIBLE);
            rlBalance.setVisibility(View.VISIBLE);
//            rlWeixiu.setVisibility(View.VISIBLE);
            rlTongji.setVisibility(View.VISIBLE);
//            rlShebeicount.setVisibility(View.GONE);
            rlSmrenzheng.setVisibility(View.VISIBLE);
            rlTobeManager.setVisibility(View.VISIBLE);
        } else if (loginType.equals("MinerMaster")){
//            rlMyShebei.setVisibility(View.GONE);
            rlBalance.setVisibility(View.VISIBLE);
//            rlWeixiu.setVisibility(View.VISIBLE);
            rlTongji.setVisibility(View.VISIBLE);
//            rlShebeicount.setVisibility(View.VISIBLE);
            rlSmrenzheng.setVisibility(View.VISIBLE);
            rlTobeManager.setVisibility(View.GONE);
        }else{
            showMenuForRole();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mineType = SharedPreferencesUtil.getString(mContext,"mineType","None");
        if (CommonUtils.isBlank(User) || CommonUtils.isBlank(Token)) {
            mine.setText("我的");
            tvXiala.setVisibility(View.GONE);
        } else {
            tvXiala.setVisibility(View.VISIBLE);
            String SysCode = "";
            if(mineType.equals("Miner")){
                SysCode = FrSysCode;
                mine.setText("矿工");
            }else if(mineType.equals("MinerMaster")){
                SysCode = FMasterCode;
                mine.setText("矿场主");
            }else{
                mine.setText("运维");
            }
            getUserInfo(mContext, LoginType, User, SysCode, Key, Token);//更新数据
        }
        if(mineType != null && !"None".equals(mineType)){
            showMenuForRole();
        }else{
//            rlMyShebei.setVisibility(View.GONE);
            rlBalance.setVisibility(View.VISIBLE);
//            rlWeixiu.setVisibility(View.VISIBLE);
            rlTongji.setVisibility(View.VISIBLE);
//            rlShebeicount.setVisibility(View.VISIBLE);
            rlSmrenzheng.setVisibility(View.VISIBLE);
            rlTobeManager.setVisibility(View.GONE);
            rlOnline.setVisibility(View.GONE);
            rlOffline.setVisibility(View.GONE);
            rlShebeiManager.setVisibility(View.GONE);
            rlWeixiudangManager.setVisibility(View.GONE);
            rlAttendance.setVisibility(View.GONE);
//            rlMonitor.setVisibility(View.GONE);
        }
        getNewsCount();
    }

    private void showPopupWindow(){
            if(mPopupWindow == null){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View contentView = inflater.inflate(R.layout.mine_popupwindow, null,false);

            mPopupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.showAsDropDown(rlSpinner, 10, 0);
            TextView miner = contentView.findViewById(R.id.miner);
            TextView mineMaster = contentView.findViewById(R.id.mineMaster);
            TextView operation = contentView.findViewById(R.id.operation);

            miner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "FrSysCode: ");
                    mine.setText("矿工");
                    SharedPreferencesUtil.putString(mContext, "LoginType", "Miner");
                    SharedPreferencesUtil.putString(mContext, "mineType", "Miner");
                    if(!LoginType.equals("Miner")){
                        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType","None");
                    }
                    getUserInfo(mContext, LoginType, User, FrSysCode, Key, Token);//更新数据
                    EventBus.getDefault().post(new PostResult(EventBusTags.UPDATE_HOSTING_MINER_DATA));
                    showMenuForRole();
                    getNewsCount();
                }
            });

            mineMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "FMasterCode: ");
                    if(FMasterCode != null && !FMasterCode.isEmpty()){
                        mine.setText("矿场主");
                        SharedPreferencesUtil.putString(mContext, "LoginType", "MinerMaster");
                        SharedPreferencesUtil.putString(mContext, "mineType", "MinerMaster");
                        if(!LoginType.equals("MinerMaster")){
                            LoginType = SharedPreferencesUtil.getString(mContext, "LoginType","None");
                        }
                        getUserInfo(mContext, LoginType, User, FMasterCode, Key, Token);//更新数据
                        EventBus.getDefault().post(new PostResult(EventBusTags.UPDATE_HOSTING_MASTER_DATA));
                        showMenuForRole();
                        getNewsCount();
                    }
                    else{
                        ToastUtils.showToast("抱歉！您当前没有此权限");
                    }
                }
            });

            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "PersonSysCode: ");
                    if(PersonSysCode != null && !PersonSysCode.isEmpty()){
                        mine.setText("运维");
                        SharedPreferencesUtil.putString(mContext, "mineType", "Operation");
                        String SysCode = LoginType.equals("Miner") ? FrSysCode : FMasterCode;
                        getUserInfo(mContext, LoginType, User, SysCode, Key, Token);//更新数据
                        EventBus.getDefault().post(new PostResult(EventBusTags.UPDATE_HOSTING_OPERATION_DATA));
                        showMenuForRole();
                        getNewsCount();
                    }
                    else{
                        ToastUtils.showToast("抱歉！您当前没有此权限");
                    }
                }
            });
        }else{
            mPopupWindow.showAsDropDown(rlSpinner, 10, 0);
        }
    }

    private void showMenuForRole(){
        if(mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
//        rlMyShebei.setVisibility(View.GONE);
        rlBalance.setVisibility(View.GONE);
//        rlWeixiu.setVisibility(View.GONE);
        rlTongji.setVisibility(View.GONE);
//        rlShebeicount.setVisibility(View.GONE);
        rlSmrenzheng.setVisibility(View.GONE);
        rlTobeManager.setVisibility(View.GONE);
        rlOnline.setVisibility(View.GONE);
        rlOffline.setVisibility(View.GONE);
        rlShebeiManager.setVisibility(View.GONE);
        rlWeixiudangManager.setVisibility(View.GONE);
        rlAttendance.setVisibility(View.GONE);
//        rlMonitor.setVisibility(View.GONE);

        mineType = SharedPreferencesUtil.getString(mContext, "mineType","None");
        if (mineType.equals("Miner")) {
//            rlMyShebei.setVisibility(View.VISIBLE);
            rlBalance.setVisibility(View.VISIBLE);
//            rlWeixiu.setVisibility(View.VISIBLE);
            rlTongji.setVisibility(View.VISIBLE);
            rlSmrenzheng.setVisibility(View.VISIBLE);
            rlTobeManager.setVisibility(View.VISIBLE);
        } else if (mineType.equals("MinerMaster")) {
//            rlMyShebei.setVisibility(View.GONE);
            rlBalance.setVisibility(View.VISIBLE);
//            rlWeixiu.setVisibility(View.VISIBLE);
            rlTongji.setVisibility(View.VISIBLE);
//            rlShebeicount.setVisibility(View.VISIBLE);
            rlSmrenzheng.setVisibility(View.VISIBLE);

        } else if (mineType.equals("Operation")) {
            rlOnline.setVisibility(View.VISIBLE);
            rlOffline.setVisibility(View.VISIBLE);
            rlShebeiManager.setVisibility(View.VISIBLE);
            rlWeixiudangManager.setVisibility(View.VISIBLE);
            rlAttendance.setVisibility(View.VISIBLE);
//            rlMonitor.setVisibility(View.VISIBLE);
        }

    }
}
