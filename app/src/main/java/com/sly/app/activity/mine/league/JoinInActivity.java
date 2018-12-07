package com.sly.app.activity.mine.league;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.TimeUtils;
import com.sly.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2017/12/7.
 */
public class JoinInActivity extends BaseActivity {
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
    @BindView(R.id.TLtitle)
    TabLayout mTLtitle;
    @BindView(R.id.VPcontent)
    ViewPager mVPcontent;
    private View JoinIn, Promote;
    private LayoutInflater mInflater;
    private List<View> mViewList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private String MemberCode, Token;
    private TimeUtils timeUtils;
    MyPagerAdapter mAdapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("会员商城");

    }

    @Override
    protected void initData() {
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
        showView();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_joinin;
    }

    @Override
    protected void setListener() {

    }

    private void showView() {
        mInflater = LayoutInflater.from(this);
        JoinIn = mInflater.inflate(R.layout.item_joinin, null);
        Promote = mInflater.inflate(R.layout.item_promote, null);
        initJoin(JoinIn);
        initPromote(Promote);
        mViewList.add(JoinIn);
        mViewList.add(Promote);
        mTitleList.add("我要加盟");
        mTitleList.add("我要晋升");
        mTLtitle.addTab(mTLtitle.newTab().setText(mTitleList.get(0)));
        mTLtitle.addTab(mTLtitle.newTab().setText(mTitleList.get(1)));
        mAdapter = new MyPagerAdapter(mViewList);
        mVPcontent.setAdapter(mAdapter);
        mTLtitle.setupWithViewPager(mVPcontent);
        mTLtitle.setTabsFromPagerAdapter(mAdapter);
    }

    String type = null;

    private void initJoin(View joinIn) {
        final ViewHolder holder = new ViewHolder(joinIn);
        holder.mBtnSendCode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeUtils = new TimeUtils(holder.mBtnSendCode1, "发送验证码");
                timeUtils.RunTimer();
                sendMsm(MemberCode);
            }
        });
        holder.mBtnNextStepOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regJoinData(holder);
            }
        });
    }

    private void initPromote(View promote) {
        final ViewHolder2 viewHolder2 = new ViewHolder2(promote);
        viewHolder2.mBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeUtils = new TimeUtils(viewHolder2.mBtnSendCode, "发送验证码");
                timeUtils.RunTimer();
                sendMsm(MemberCode);
            }
        });

        // 300 1500 7500 15000  100000
        viewHolder2.mBtnNextStepTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regPromoteData(viewHolder2);
            }
        });
    }

    /**
     * 校验参数
     *
     * @param holder2
     */
    private void regPromoteData(ViewHolder2 holder2) {
        String id = holder2.mEtJoin2Member.getText().toString();
        String sbm = holder2.mEtJoin2Sbm.getText().toString();
        String code = holder2.mEtJoin2Code.getText().toString();
        holder2.mSpJoin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] join = getResources().getStringArray(R.array.sp_join_2);
                Logcat.i("你点击的是:" + join[position]);
                String level = join[position];
                if (level.equals("VIP会员")) {
                    type = "04";
                } else if (level.equals("品牌大使")) {
                    type = "05";
                } else if (level.equals("金牌代理")) {
                    type = "06";
                } else if (level.equals("钻石代理")) {
                    type = "07";
                } else if (level.equals("合伙人")) {
                    type = "08";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (CommonUtils.isBlank(type) || CommonUtils.isBlank(id) || CommonUtils.isBlank(code) || CommonUtils.isBlank(sbm)) {
            ToastUtils.showToast("加盟信息不能为空");
        } else {
            ProPromote(type, id, sbm, code);
        }
    }

    /**
     * 晋级
     *
     * @param type
     * @param id
     * @param sbm
     * @param code
     */
    private void ProPromote(String type, String id, String sbm, String code) {

    }

    /**
     * 校验参数
     *
     * @param holder
     */
    private void regJoinData(ViewHolder holder) {
        Logcat.i("------join---");
        String member = holder.mEtJoin1Member.getText().toString();
        String name = holder.mEtJoin1Name.getText().toString();
        String code = holder.mEtJoin1Code.getText().toString();
        String sbm1 = holder.mEtJoin1Sbm1.getText().toString();
        String sbm2 = holder.mEtJoin1Sbm2.getText().toString();
        holder.mSpJoin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] join = getResources().getStringArray(R.array.sp_join_2);
                Logcat.i("你点击的是:" + join[position]);
                String level = join[position];
                if (level.equals("VIP会员")) {
                    type = "04";
                } else if (level.equals("品牌大使")) {
                    type = "05";
                } else if (level.equals("金牌代理")) {
                    type = "06";
                } else if (level.equals("钻石代理")) {
                    type = "07";
                } else if (level.equals("合伙人")) {
                    type = "08";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Logcat.i("-----"+type+"/"+name+"/"+code+"/"+sbm1+"/"+sbm2);
        if (CommonUtils.isBlank(type) || CommonUtils.isBlank(name) || CommonUtils.isBlank(code) || CommonUtils.isBlank(sbm1) || CommonUtils.isBlank(sbm2) || CommonUtils.isBlank(member)) {
            ToastUtils.showToast("加盟信息不能为空");
        } else {
            ProJoin(type, member, name, code, sbm1, sbm2);
        }
    }

    /**
     * 加盟
     *
     * @param type
     * @param member
     * @param name
     * @param code
     * @param sbm1
     * @param sbm2
     */
    private void ProJoin(String type, String member, String name, String code, String sbm1, String sbm2) {
        //{JoinMember:"V8955979",JoinLevel:"06",NodeParent:"JW865655",Name:"测试",Token:"16672cb5-7ff8-4642-870c-6d7481fe9c1a"}
        initProgressDialog("处理中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("JoinMember", member);
        map.put("NodeParent", sbm1);
        map.put("Name", name);
        map.put("JoinLevel", type);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_JOIN_ORDER, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_JOIN_ORDER, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param MemberCode
     */
    private void sendMsm(String MemberCode) {
        initProgressDialog("发送验证码中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", "");

        //{JoinMember:"V8955979",JoinLevel:"06",NodeParent:"JW865655",Name:"测试",Token:"16672cb5-7ff8-4642-870c-6d7481fe9c1a"}
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_CODE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_GET_CODE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getMsg());
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_main_title:
                break;
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }

    static class ViewHolder {
        @BindView(R.id.sp_join_1)
        Spinner mSpJoin1;
        @BindView(R.id.et_join_1_member)
        EditText mEtJoin1Member;
        @BindView(R.id.et_join_1_name)
        EditText mEtJoin1Name;
        @BindView(R.id.et_join_1_code)
        EditText mEtJoin1Code;
        @BindView(R.id.btn_send_code_1)
        Button mBtnSendCode1;
        @BindView(R.id.et_join_1_sbm_1)
        EditText mEtJoin1Sbm1;
        @BindView(R.id.et_join_1_sbm_2)
        EditText mEtJoin1Sbm2;
        @BindView(R.id.btn_NextStepOne)
        Button mBtnNextStepOne;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder2 {
        @BindView(R.id.sp_join_2)
        Spinner mSpJoin2;
        @BindView(R.id.et_join_2_member)
        EditText mEtJoin2Member;
        @BindView(R.id.et_join_2_code)
        EditText mEtJoin2Code;
        @BindView(R.id.btn_send_code)
        Button mBtnSendCode;
        @BindView(R.id.et_join_2_sbm)
        EditText mEtJoin2Sbm;
        @BindView(R.id.btn_NextStepTwo)
        Button mBtnNextStepTwo;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
