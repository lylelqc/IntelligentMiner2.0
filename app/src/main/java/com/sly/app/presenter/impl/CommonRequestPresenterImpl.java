package com.sly.app.presenter.impl;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sly.app.comm.EventBusTags;
import com.sly.app.http.NetWorkCons;
import com.sly.app.interactor.ICommonRequestInteractor;
import com.sly.app.interactor.impl.CommonRequestInteractorImpl;
import com.sly.app.listener.IRequestListener;
import com.sly.app.model.PostResult;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.utils.LoginMsgHelper;
import com.sly.app.utils.ToastUtils;
import com.sly.app.utils.http.HttpStatusUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONObject;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 */
public class CommonRequestPresenterImpl implements ICommonRequestPresenter, IRequestListener {

    public Context context;

    public ICommonViewUi iCommonViewUi;

    public ICommonRequestInteractor iCommonRequestInteractor;

    public CommonRequestPresenterImpl(Context context, ICommonViewUi iCommonViewUi) {

        this.context = context;
        this.iCommonViewUi = iCommonViewUi;
        iCommonRequestInteractor = new CommonRequestInteractorImpl(this);
    }

    @Override
    public void request(int eventTag, Context context, String url, Map<String, String> params) {

        iCommonRequestInteractor.request(eventTag, context, url, params);
    }

    @Override
    public void requestGet(int eventTag, Context context, String url, Map<String, String> params) {

        iCommonRequestInteractor.requestGet(eventTag, context, url, params);
    }


    @Override
    public void onSuccess(int eventTag, String data) {
        if (eventTag == NetWorkCons.EventTags.REDEEMCODE){
            iCommonViewUi.getRequestData(eventTag, data);
        }

        if (HttpStatusUtil.getStatus(data) || eventTag < 0) {
            iCommonViewUi.getRequestData(eventTag, data);
        } else {
            if(HttpStatusUtil.isRelogin(data)){
                try {
                    JSONObject object=new JSONObject(data);
                    String msg=object.getString("msg");
//                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
                LoginMsgHelper.exitLogin(context);
                new MaterialDialog.Builder(context)
                        .title("重新登录")
                        .titleColor(Color.parseColor("#2F3F61"))
                        .content("登录过期，请重新登录")
                        .positiveText("确定")
                        .positiveColor(Color.parseColor("#2F3F61"))
                        .negativeText("取消")
                        .negativeColor(Color.parseColor("#999999"))
                        .cancelable(false)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                LoginMsgHelper.reLogin(context);
                            }
                        })
                        .show();
                EventBus.getDefault().post(new PostResult(EventBusTags.LOGOUT));
            }else if("会话过期或非法访问".equals(HttpStatusUtil.getStatusMsg(data))){
                LoginMsgHelper.reLogin(context); // 重启到登录页面
                ToastUtils.showToast("会话过期或非法访问");
                EventBus.getDefault().post(new PostResult(EventBusTags.LOGOUT));
            }else{
                iCommonViewUi.onRequestSuccessException(eventTag, HttpStatusUtil.getStatusMsg(data));
            }

        }
    }

    @Override
    public void onError(int eventTag, String msg) {
//        msg = Contants.NetStatus.NETWORK_MAYBE_DISABLE;
        //系统异常
        iCommonViewUi.onRequestFailureException(eventTag, msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        iCommonViewUi.isRequesting(eventTag, status);
    }
}
