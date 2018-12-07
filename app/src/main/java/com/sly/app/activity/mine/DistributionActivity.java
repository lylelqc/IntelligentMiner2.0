/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.activity.mine;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;
import vip.devkit.view.common.suklib.view.FlowLayout.FlowLayout;
import vip.devkit.view.common.suklib.view.FlowLayout.TagAdapter;
import vip.devkit.view.common.suklib.view.FlowLayout.TagFlowLayout;

/**
 * 文 件 名: DistributionActivity
 * 创 建 人: By k
 * 创建日期: 2017/9/29 14:53
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class DistributionActivity extends BaseActivity {

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
    @BindView(R.id.iv_marks)
    ImageView mIvMarks;
    @BindView(R.id.rl_rank)
    RelativeLayout mRlRank;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.ll_layout_a)
    LinearLayout mLlLayoutA;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_rank)
    TextView mTvRank;
    @BindView(R.id.tag_view)
    TagFlowLayout mTagView;
    @BindView(R.id.textView4)
    TextView mTextView4;
    @BindView(R.id.ll_rank)
    LinearLayout mLlRank;
    private String MemberCode, Token;
    private String Level, ToMember, PWD;
    private String[] rankTag = {"VIP 会员", "品牌大使", "铜牌代理", "金牌代理"};
    private int flag = 0;
    private vip.devkit.view.common.suklib.view.FlowLayout.TagAdapter<String> TagAdapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("分销");
        initTAG();
    }

    private void initTAG() {
        TagAdapter = new TagAdapter<String>(rankTag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                LayoutInflater mInflater;
                mInflater = LayoutInflater.from(DistributionActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.view_option_tag,
                        mTagView, false);
                tv.setText(s);
                return tv;
            }
        };
        mTagView.setAdapter(TagAdapter);
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_distribution;
    }

    @Override
    protected void setListener() {
        mTagView.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Logcat.i("setOnSelectListener : "+selectPosSet);
            }
        });
        mTagView.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mTvRank.setText(rankTag[position]);
                return false;
            }
        });
    }

    private void SelectRank(Context mContext, final TextView textView) {
        final Dialog mDialog = new Dialog(this, R.style.Translucent_NoTitle);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_disribution);
        mDialog.setCanceledOnTouchOutside(true);//点击外部区域不关闭
        mDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        mDialog.findViewById(R.id.ll_layout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(((TextView) mDialog.findViewById(R.id.tv_rank_1)).getText().toString());
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.ll_layout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(((TextView) mDialog.findViewById(R.id.tv_rank_2)).getText().toString());
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.ll_layout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(((TextView) mDialog.findViewById(R.id.tv_rank_3)).getText().toString());
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.ll_layout4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(((TextView) mDialog.findViewById(R.id.tv_rank_4)).getText().toString());
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    @OnClick({R.id.btn_main_back, R.id.rl_rank, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_rank:
                if (flag == 0) {
                    mLlRank.setVisibility(View.VISIBLE);
                    mIvMarks.setImageResource(R.drawable.ws_upward);
                } else if (flag == 1) {
                    mLlRank.setVisibility(View.GONE);
                    mIvMarks.setImageResource(R.drawable.ws_down);
                }
                flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
            case R.id.tv_submit:
                Level = mTvRank.getText().toString();
                ToMember = mEtAccount.getText().toString();
                PWD = mEtPwd.getText().toString();
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    if (RegInfo(Level, ToMember, PWD) != false) {
                        ProJoinFx(MemberCode, Token, Level, ToMember, PWD);
                    }
                }
                break;
        }
    }

    /**
     * @param memberCode
     * @param token
     * @param level
     * @param toMember
     * @param pwd
     */
    private void ProJoinFx(String memberCode, String token, String level, String toMember, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("FromMember", memberCode);
        map.put("Level", level);
        map.put("ToMember", toMember);
        map.put("PayPassword", EncryptUtil.MD5(pwd));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_JOIN_FX, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.USER_JOIN_FX, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
                return false;
            }
        });
    }


    /**
     * @param level
     * @param toMember
     * @param pwd
     * @return
     */
    private boolean RegInfo(String level, String toMember, String pwd) {
        if (CommonUtils.isBlank(level)) {
            ToastUtils.showToast("请选择分销等级");
            return false;
        }
        if (CommonUtils.isBlank(toMember)) {
            ToastUtils.showToast("请选择分销对象");
            return false;
        }
        if (CommonUtils.isBlank(pwd)) {
            ToastUtils.showToast("支付密码为空");
            return false;
        }
        return true;
    }

}
