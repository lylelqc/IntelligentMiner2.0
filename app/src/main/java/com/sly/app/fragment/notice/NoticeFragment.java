package com.sly.app.fragment.notice;

import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.notice.AllNoticeAdapter;
import com.sly.app.base.BaseFragment;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.notice.AllNoticeBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.SwipeListLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 作者 by K
 * 时间：on 2017/8/25 11:33
 * 邮箱 by  vip@devkit.vip
 * <p>
 * 类用途：
 * 最后修改：
 */
public class NoticeFragment extends BaseFragment implements AllNoticeAdapter.OnItemDeleteListener{


    @BindView(R.id.layout_notice_null)
    LinearLayout mLayoutNoticeNull;
    @BindView(R.id.lv_msg)
    ListView mLvMsg;
    private String Token,MemberCode;
    private AllNoticeAdapter mAdapter;
    private List<AllNoticeBean> beanList;
    @Override
    protected void initView(View view) {
        super.initView(view);
        mLvMsg.setVisibility(View.GONE);
        mLayoutNoticeNull.setVisibility(View.VISIBLE);
    }
    @Override
    protected void initData() {
        super.initData();
        Token = SharedPreferencesUtil.getString(getActivity(), "Token");
        MemberCode = SharedPreferencesUtil.getString(getActivity(), "MemberCode");
        if (CommonUtils.isBlank(MemberCode)|| CommonUtils.isBlank(Token)){
            ToastUtils.showToast("参数异常，自动重启");
            mActivity.finish();
        }else {
            getMessage(MemberCode, Token);
        }
    }
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_notice;
    }
    @Override
    protected void setListener() {
        super.setListener();
        mLvMsg.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (mAdapter.SwipeListLayoutCount().size() > 0) {
                            for (SwipeListLayout s : mAdapter.SwipeListLayoutCount()) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                mAdapter.SwipeListLayoutCount().remove(s);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    @Override
    public void onItemDeleteClick(int position) {
        if (beanList.get(position).getId()!=null){
            delNotice(beanList.get(position).getId(),position);
        }
    }
    private void getMessage(String memberCode,String token) {
        final Map<String, String> map = new HashMap();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.UserAllMessage, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkConstant.UserAllMessage, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    beanList = JSON.parseArray(mReturnBean.getData(), AllNoticeBean.class);
                    if (beanList.size() > 0) {
                        mLvMsg.setVisibility(View.VISIBLE);
                        mLayoutNoticeNull.setVisibility(View.GONE);
                        mAdapter = new AllNoticeAdapter(mActivity, beanList, R.layout.item_notice_b);
                        mAdapter.setItemDeleteListener(NoticeFragment.this);
                        mLvMsg.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            }
        });
    }

    private void delNotice(String msgId, final int position) {
        final Map<String, String> map = new HashMap();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("ID", msgId);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.UpdateUserAllMessage, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkConstant.UpdateUserAllMessage, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")&&mReturnBean.getData().equals("删除成功!")) {
                    beanList.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            }
        });
    }
}