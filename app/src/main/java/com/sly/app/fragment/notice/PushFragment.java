package com.sly.app.fragment.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.notice.MsgInfoActivity;
import com.sly.app.adapter.notice.MsgAdapter;
import com.sly.app.base.BaseFragment;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.MsgBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/8/25 11:33
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class PushFragment extends BaseFragment {


    @BindView(R.id.layout_notice_null)
    LinearLayout mLayoutNoticeNull;
    @BindView(R.id.lv_msg)
    ListView mLvMsg;
    private MsgAdapter mAdapter;
    private String Token, MemberCode;
    private List<MsgBean> mBeanList = new ArrayList<>();
    private int PageIndex = 1;
    private boolean FLAG = true;

    @Override
    protected void initData() {
        super.initData();
        Token = SharedPreferencesUtil.getString(getActivity(), "Token");
        MemberCode = SharedPreferencesUtil.getString(getActivity(), "MemberCode");
        if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
            ToastUtils.showToast("参数异常，自动重启");
            mActivity.finish();
        } else {
            getMessage(MemberCode, Token, PageIndex);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAdapter = new MsgAdapter(mActivity, mBeanList, R.layout.item_notice_c);
        mLvMsg.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mLvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logcat.i("------------------------------------------asa:"+position);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("MsgInfo",mBeanList.get(position));
                startActivityWithExtras(MsgInfoActivity.class,mBundle);
            }
        });
        mLvMsg.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mLvMsg.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Logcat.i("ListView", "##### 滚动到顶部 #####");
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mLvMsg.getChildAt(mLvMsg.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mLvMsg.getHeight()) {
                        Logcat.i("ListView", "##### 滚动到底部 ######");
                        if (FLAG == true) {
                            getMessage(MemberCode, Token, PageIndex++);
                        }
                    }
                }
            }
        });

    }

    private void getMessage(String memberCode, String token, final int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", "10");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.MSG_GET_ALL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.MSG_GET_ALL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<MsgBean> msgBeans = JSON.parseArray(mReturnBean.getData(), MsgBean.class);
                    mBeanList.addAll(msgBeans);
                    mAdapter.notifyDataSetChanged();
                    if (mLvMsg!=null){
                        if (pageIndex==1&&msgBeans.size() > 0) {
                            mLvMsg.setVisibility(View.VISIBLE);
                            mLayoutNoticeNull.setVisibility(View.GONE);
                        }else {
                            mLvMsg.setVisibility(View.GONE);
                            mLayoutNoticeNull.setVisibility(View.VISIBLE);
                        }
                        if (msgBeans.size()<10){
                            FLAG = false;
                        }
                    }
                } else {
                    mLvMsg.setVisibility(View.GONE);
                    mLayoutNoticeNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
    }


}