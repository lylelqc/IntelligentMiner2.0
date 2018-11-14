package com.sly.app.presenter.impl;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.interactor.IRecyclerViewInteractor;
import com.sly.app.interactor.impl.RecyclerViewInteractorImpl;
import com.sly.app.listener.IRequestListener;
import com.sly.app.model.PostResult;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.LoginMsgHelper;
import com.sly.app.utils.http.HttpStatusUtil;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 */
public class RecyclerViewPresenterImpl implements IRecyclerViewPresenter, IRequestListener {

    public Context context;
    public IRecyclerViewUi iRecyclerViewUi;
    public IRecyclerViewInteractor iRecyclerViewInteractor;

    public RecyclerViewPresenterImpl(Context context, IRecyclerViewUi iRecyclerViewUi){

        this.context = context;
        this.iRecyclerViewUi = iRecyclerViewUi;
        this.iRecyclerViewInteractor = new RecyclerViewInteractorImpl(this);
    }

    @Override
    public void loadData(int eventTag, Context context, String url, Map<String, String> map) {
        iRecyclerViewInteractor.loadData(eventTag, context, url, map);
    }

    @Override
    public void onSuccess(int eventTag, String data) {

        if(HttpStatusUtil.getStatus(CommonUtils.proStrs(data))){
            if (eventTag == Contants.HttpStatus.refresh_data) {
                iRecyclerViewUi.getRefreshData(eventTag, CommonUtils.proStrs(data));

            } else if (eventTag == Contants.HttpStatus.loadmore_data) {
                iRecyclerViewUi.getLoadMoreData(eventTag, CommonUtils.proStrs(data));
            }
        }else{
            if(HttpStatusUtil.isRelogin(CommonUtils.proStrs(data))){

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
            }else if(HttpStatusUtil.isLoginUser(CommonUtils.proStrs(data))){

            }else{
                iRecyclerViewUi.onRequestSuccessException(eventTag, HttpStatusUtil.getStatusMsg(CommonUtils.proStrs(data)));
            }
        }
    }

    @Override
    public void onError(int eventTag, String msg) {

        msg = Contants.NetStatus.NETWORK_MAYBE_DISABLE;
        iRecyclerViewUi.onRequestFailureException(eventTag, msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        iRecyclerViewUi.isRequesting(eventTag, status);

    }
}

