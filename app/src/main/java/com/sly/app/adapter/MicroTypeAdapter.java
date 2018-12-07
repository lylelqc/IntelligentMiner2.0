package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.LoadingDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.RandomUtils;

public class MicroTypeAdapter extends CommonAdapter<GoodsBean> {
    LoadingDialog loadingDialog;

    public MicroTypeAdapter(Context context, List<GoodsBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, final GoodsBean goodsBean, int i) {
        holder.setText(R.id.tv_m_mall_title, goodsBean.getComTitle()).setText(R.id.tv_m_mall_price, goodsBean.getMartPrice() + "");
        holder.setImageURL(R.id.iv_m_mall_img, goodsBean.getDescript().replace("40-40", "400-400"));
        if (goodsBean.getSales().equals("0")) {
            holder.setText(R.id.tv_pay_sum, RandomUtils.getRandom(1, 200) + "人付款");
        } else {
            holder.setText(R.id.tv_pay_sum,goodsBean.getSales()+ "人付款");
        }
        holder.getView(R.id.tv_add_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MemberCode = SharedPreferencesUtil.getString(mContext, "MemberCode");
                String Token = SharedPreferencesUtil.getString(mContext, "Token");
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    ProCollection(goodsBean.getComID() + "", MemberCode, Token, goodsBean.getMallType());
                }
            }
        });
    }

    /**
     * @param comId
     * @param memberCode
     * @param token
     */
    private void ProCollection(String comId, String memberCode, String token, String mallType) {
        loadingDialog = new LoadingDialog(mContext, R.style.loading_dialog);
        loadingDialog.setText("收藏中...");
        loadingDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("ComID", comId);
        map.put("MallType", mallType);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_COLLECTION, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        loadingDialog.dismiss();
                        NetLogCat.W(NetWorkConstant.COM_COLLECTION, json, statusCode, content);
                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if (mReturnBean.getStatus().equals("1")) {
                            ToastUtils.showToast("收藏成功");
                        } else {
                            ToastUtils.showToast(mReturnBean.getMsg());
                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                    }
                }

        );
        loadingDialog.dismiss();

    }
}
