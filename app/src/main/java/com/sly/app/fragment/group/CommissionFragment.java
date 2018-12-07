package com.sly.app.fragment.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.group.GroupCommissionAdapter;
import com.sly.app.base.BaseFragment;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.UserGroup;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/12/5.
 * <p>
 * 我的佣金
 */
public class CommissionFragment extends BaseFragment {
    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.tv_sum)
    TextView mTvSum;

    private String MemberCode, Token;
    private String nowDate, endDate;
    private int PageIndex = 1;
    private boolean isContinue = true;
    private View FooterView;
    GroupCommissionAdapter mAdapter;
    List<UserGroup.QunBean> mBeanList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        MemberCode = SharedPreferencesUtil.getString(mActivity, "MemberCode");
        Token = SharedPreferencesUtil.getString(mActivity, "Token");
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endDate = sdf.format(ca.getTime());
        ca.add(Calendar.MONTH, -1);// 月份减1
        Date resultDate = ca.getTime(); // 结果
        nowDate = sdf.format(resultDate);
        getData(MemberCode, Token, PageIndex, nowDate, endDate);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_group_2;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        FooterView = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_list_footer, null, false);
        mAdapter = new GroupCommissionAdapter(mActivity, mBeanList, R.layout.item_group_2);
        mLvList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setListener() {
        super.setListener();
        mLvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    View lastVisibleItemView = mLvList.getChildAt(mLvList.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mLvList.getHeight()) {
                        Logcat.i("##### 滚动到底部 #####");
                        if (isContinue == true) {
                            getData(MemberCode, Token, ++PageIndex, nowDate, endDate);
                        }
                    }
                }
            }
        });
    }

    private void getData(String memberCode, String token, final int pageIndex, String nowDate, String endDate) {
        initProgressDialog("加载中...", true);
        final Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("BeginDate", nowDate + "");
        map.put("EndDate", endDate);
        map.put("PageNo", pageIndex + "");
        map.put("PageSize", "10");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GROUP_NEXUS_2, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.I(NetWorkConstant.USER_GROUP_NEXUS_2, json, statusCode, content);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        UserGroup userGroup = JSON.parseObject(jsonObject.optString("data"), UserGroup.class);
                        if (!CommonUtils.isBlank((userGroup.getSum()))){
                            mTvSum.setText(CommonUtils.getDoubleStr(Double.valueOf(userGroup.getSum())));
                        }
                        List<UserGroup.QunBean> groupTeam = JSON.parseArray(CommonUtils.GsonToJson(userGroup.getQun()), UserGroup.QunBean.class);
                        mBeanList.addAll(groupTeam);
                        mAdapter.notifyDataSetChanged();
                        if (pageIndex==1&&groupTeam.size()>0){
                            mLvList.setVisibility(View.VISIBLE);
                        }else {

                        }
                        if (groupTeam.size()<10){
                            isContinue = false;
                            com.sly.app.view.ViewUtils.addFooterViewForList(mLvList, FooterView);
                        }
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
        dismissProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBeanList.size() > 0) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
