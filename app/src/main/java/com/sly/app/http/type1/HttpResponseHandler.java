/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.sly.app.http.type1;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.comm.AppContext;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

public class HttpResponseHandler {
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    private static final String TAG = "HttpResponseHandler:";

    private Handler handler;

    /**
     * Creates a new AsyncHttpResponseHandler
     */
    public HttpResponseHandler() {
        // Set up a handler to post events back to the correct thread if possible
        if (Looper.myLooper() != null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    HttpResponseHandler.this.handleMessage(msg);
                }
            };
        }
    }

    //
    // Callbacks to be overridden, typically anonymously
    //

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param content the body of the HTTP response from the server
     */
    public void onSuccess(String content) {
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *  @param statusCode the status code of the response
     * @param headers    the headers of the HTTP response
     * @param content    the body of the HTTP response from the server
     */
    public boolean onSuccess(int statusCode, Headers headers, String content) {
        onSuccess(statusCode, content);
        return false;
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode the status code of the response
     * @param content    the body of the HTTP response from the server
     */
    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    /**
     * Called when the request could not be executed due to cancellation, a
     * connectivity problem or timeout. Because networks can fail during an
     * exchange, it is possible that the remote server accepted the request
     * before the failure.
     */
    public void onFailure(Request request, IOException e) {
    }


    //
    // 后台线程调用方法，通过Handler sendMessage把结果转到UI主线程
    //

    protected void sendSuccessMessage(Response response) {
        try {
            sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{new Integer(response.code()), response.headers(), response.body().string()}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void sendFailureMessage(Request request, IOException e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, request}));
    }

    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected void handleSuccessMessage(int statusCode, Headers headers, String responseBody) {
        onSuccess(statusCode, headers, responseBody);
    }

    protected void handleFailureMessage(Request request, IOException e) {
        onFailure(request, e);
    }


    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                if(isTokenExpired((String) response[2])){
                    Log.i(TAG,"返回数据 ://// "+ response[2]);
                    ToastUtils.showToast("登录失效，请重新登录！");
                    SharedPreferencesUtil.putString(AppContext.mContext, "Token", null);
                    SharedPreferencesUtil.putString(AppContext.mContext, "MemberCode", null);
                    Intent intent = new Intent(AppContext.mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppContext.mContext.startActivity(intent);
                }else {
                    handleSuccessMessage(((Integer) response[0]).intValue(), (Headers) response[1], (String) response[2]);
                }
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                handleFailureMessage((Request) response[1], (IOException) response[0]);
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }
    private boolean isTokenExpired(String  response) {
        String backlogJsonStr = response;
//        Log.i(TAG,backlogJsonStr);
        return backlogJsonStr.indexOf("token验证失败") != -1;
    }
}
