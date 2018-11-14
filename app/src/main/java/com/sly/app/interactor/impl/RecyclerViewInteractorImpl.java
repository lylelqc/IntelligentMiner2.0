package com.sly.app.interactor.impl;

import android.app.Activity;
import android.content.Context;

import com.sly.app.interactor.IRecyclerViewInteractor;
import com.sly.app.listener.IRequestListener;
import com.sly.app.utils.HttpHelper;
import com.sly.app.utils.http.OkHttpResponseHandler;

import java.util.Map;

import okhttp3.Request;

public class RecyclerViewInteractorImpl implements IRecyclerViewInteractor {

    public IRequestListener iRequestListener;

    public RecyclerViewInteractorImpl(IRequestListener iRequestListener){
        this.iRequestListener = iRequestListener;
    }

    @Override
    public void loadData(final int eventTag, final Context context, final String url, final Map<String, String> map) {

        HttpHelper.getInstance().post(context, url, map, new OkHttpResponseHandler<String>(context){

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

//                System.out.println("请求参数"+map.toString());

                System.out.println("请求结果》"+ url +"______"+""+ json);

                if(!isActivityEnd(context)) {
                    iRequestListener.onSuccess(eventTag, json);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

                if(!isActivityEnd(context)) {
                    iRequestListener.onError(eventTag, e.getMessage());
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();

                if(!isActivityEnd(context)) {
                    iRequestListener.isRequesting(eventTag, true);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();

                if(!isActivityEnd(context)) {
                    iRequestListener.isRequesting(eventTag, false);
                }
            }
        });
    }

    /**
     * 根据context判断activity是否已经结束
     * @param context
     * @return
     */
    public boolean isActivityEnd(final Context context){

        if(context != null){

            try {
                Activity activity = (Activity)context;

                if(activity == null || activity.isFinishing()){

                    System.out.println("context为null了");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
