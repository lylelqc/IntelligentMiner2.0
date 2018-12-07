package com.sly.app.fragment.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.group.GroupTeamAdapter;
import com.sly.app.base.BaseFragment;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.UserGroupTeam;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/12/5.
 */
public class TeamFragment extends BaseFragment {
    @BindView(R.id.lv_list)
    ListView mLvList;
    private View FooterView;
    private String MemberCode, Token;
    GroupTeamAdapter mAdapter;
    List<UserGroupTeam.RowsBean> mTeamList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        Logcat.i("TeamFragment----------------------------");
        MemberCode = SharedPreferencesUtil.getString(mActivity, "MemberCode");
        Token = SharedPreferencesUtil.getString(mActivity, "Token");
        getData(MemberCode, Token);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_team;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        FooterView = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_list_footer, null, false);
        mAdapter = new GroupTeamAdapter(mActivity, mTeamList, R.layout.item_group_team);
        mLvList.setAdapter(mAdapter);
    }

    private void getData(String memberCode, String token) {
        initProgressDialog("加载中...", true);
        final Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GROUP_TEAM, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.I(NetWorkConstant.USER_GROUP_TEAM, json, statusCode, content);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        UserGroupTeam groupTeamS = JSON.parseObject(jsonObject.optString("data"), UserGroupTeam.class);
                        List<UserGroupTeam.RowsBean> groupTeam = JSON.parseArray(CommonUtils.GsonToJson(groupTeamS.getRows()), UserGroupTeam.RowsBean.class);
                        mTeamList.addAll(groupTeam);
                        mAdapter.notifyDataSetChanged();
                        com.sly.app.view.ViewUtils.addFooterViewForList(mLvList, FooterView);
                        Logcat.i("--------------" + groupTeam.size());
                    } else {
                        ToastUtils.showToast("没有数据哦");
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
        if (mTeamList.size() > 0) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
