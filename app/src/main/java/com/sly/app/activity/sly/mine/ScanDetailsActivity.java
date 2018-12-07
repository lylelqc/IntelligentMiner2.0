package com.sly.app.activity.sly.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.ScanDetailsRecyclerViewAdapter;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.ScanDetailsBean;
import com.sly.app.model.sly.UserNameBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class ScanDetailsActivity extends BaseActivity implements ICommonViewUi, ScanDetailsRecyclerViewAdapter.OnItemClickListener{

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.scan_details_rv)
    RecyclerView mMachineListRv;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private static final int GETED_USER_NAME = 0x001;

    private String User,LoginType, MineCode, PersonSysCode, MinerSysCode, Token, Key, PlanID;
    ICommonRequestPresenter iCommonRequestPresenter;
    private List<ScanDetailsBean> mResultList = new ArrayList<>();
    private ScanDetailsRecyclerViewAdapter adapter;

    Dialog mDialog;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_scan_details;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        PlanID = getIntent().getStringExtra("PlanID");
        Logcat.e("扫描详情" + PlanID);
        tvMainTitle.setText("扫描详情");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        toRequest(NetWorkCons.EventTags.GET_SIMGLE_PLAN_DETAILS_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        if(NetWorkCons.EventTags.GET_SIMGLE_PLAN_DETAILS_LIST == eventTag){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_SIMGLE_PLAN_DETAILS_LIST);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID",PlanID);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_SIMGLE_PLAN_DETAILS_LIST, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("详情返回参数:" + result);
        if(NetWorkCons.EventTags.GET_SIMGLE_PLAN_DETAILS_LIST == eventTag) {
            List<ScanDetailsBean> resultList = parseResult(result);

            mResultList.clear();
            if (resultList != null && resultList.size() != 0) {
                mResultList.addAll(resultList);
            }
            refreshListView();
        }
    }

    private void refreshListView() {
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf4f4f4);
        mMachineListRv.setLayoutManager(mLayoutManager);
        mMachineListRv.addItemDecoration(lineVertical);
        mMachineListRv.setLayoutManager(mLayoutManager);
        adapter = new ScanDetailsRecyclerViewAdapter(mContext, mResultList);
        mMachineListRv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    private List<ScanDetailsBean> parseResult(String result) {
        JsonHelper<ScanDetailsBean> dataParser = new JsonHelper<ScanDetailsBean>(
                ScanDetailsBean.class);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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

    @OnClick({R.id.btn_main_back, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_commit:
                if(mResultList!=null && mResultList.size()>0){
                    getUserInfo(NetWorkCons.EventTags.COMMIT_SCAN_LIST, "");
                }else{
                    ToastUtils.showToast("暂无数据，不能提交");
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String worker = mResultList.get(position).getWorker();
        switch (view.getId()){
            case R.id.tv_bind_btn:
                showBindDialog(worker);
                break;
            case R.id.tv_machine_list_btn:
                Intent intent = new Intent(this, MachineListActivity.class);
                intent.putExtra("worker", worker);
                intent.putExtra("PlanID", PlanID);
                intent.putExtra("eventType", NetWorkCons.EventTags.GET_SCAN_MACHINE_LIST+"");
                startActivity(intent);
                break;
        }
    }

    private void showBindDialog(final String worker) {
        mDialog = new Dialog(mContext, R.style.AppDialog);
        mDialog.setContentView(R.layout.dialog_bind_vip);
        mDialog.setCanceledOnTouchOutside(false);
        final EditText mEtName = (EditText) mDialog.findViewById(R.id.et_dialog_txt);
//        mDialog.findViewById(R.id.tv_submit_vip_num).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String getStr = mEtName.getText().toString();
//                if (CommonUtils.isBlank(getStr)) {
//                    ToastUtils.showToast("请输入会员号" );
//                } else {
//                    //
//                    getUserInfo(NetWorkCons.EventTags.BIND_USER_VIP_CODE, worker);
//                    mDialog.dismiss();
//                }
//            }
//        });
//        mDialog.findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String getStr = mEtName.getText().toString();
//                getUserInfo(NetWorkCons.EventTags.GET_USER_NAME, getStr);
//            }
//        });
        mDialog.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void getUserInfo(final int eventType, String worker) {
        Map<String, String> map = new HashMap<>();
        map.put("User", User);
        map.put("LoginType", "None");
        map.put("Token", Token);

        if(eventType == NetWorkCons.EventTags.GET_USER_NAME){
            map.put("Rounter", NetWorkCons.GET_USER_NAME);
            map.put("mobile", worker);
        }else if(eventType == NetWorkCons.EventTags.BIND_USER_VIP_CODE){
            if(MinerSysCode == null && TextUtils.isEmpty(MinerSysCode)){
                ToastUtils.showToast("请先搜索，再绑定!");
                return;
            }
            map.put("Rounter", NetWorkCons.BIND_USER_VIP_CODE);
            map.put("personSysCode", PersonSysCode);
            map.put("mineCode", MineCode);
            map.put("planID", PlanID);
            map.put("worker",worker);
            map.put("minerSysCode", MinerSysCode);
        }else if(eventType == NetWorkCons.EventTags.COMMIT_SCAN_LIST){
            map.put("Rounter", NetWorkCons.COMMIT_SCAN_LIST);
            map.put("personSysCode", PersonSysCode);
            map.put("mineCode", MineCode);
            map.put("oper", PersonSysCode);
            map.put("planID", PlanID);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.replace("null", "\"null\"");//替换空字符串
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + content + "提交的内容：" + json + " headers :" + headers);
                        if(eventType == NetWorkCons.EventTags.GET_USER_NAME){
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if(returnBean.getData().getRows().size() > 0){
                                    List<UserNameBean> replaceListBean = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                                    Message msg = handler.obtainMessage();
                                    msg.obj = replaceListBean.get(0);
                                    msg.what = GETED_USER_NAME;   //标志消息的标志
                                    handler.sendMessage(msg);
                                }else{
                                    ToastUtils.showToast("未找到会员名!");
                                }
                            } else {
                                    ToastUtils.showToast(returnBean.getMsg());
                            }
                        } else if(eventType == NetWorkCons.EventTags.BIND_USER_VIP_CODE){
                            final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                new AlertDialog.Builder(ScanDetailsActivity.this)
                                        .setMessage("提交成功!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).show();
                            }else{
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        }else if(eventType == NetWorkCons.EventTags.COMMIT_SCAN_LIST){
                            final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                new AlertDialog.Builder(ScanDetailsActivity.this)
                                        .setMessage("提交成功!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).show();
                            }else{
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        }
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

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GETED_USER_NAME :
                    UserNameBean bean = (UserNameBean)msg.obj;
                    TextView name = mDialog.findViewById(R.id.tv_vip_name);
                    name.setText(bean.getName());
                    MinerSysCode = bean.getCode();
                    break;
            }
        }
    };
}
