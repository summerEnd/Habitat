package com.sumauto.habitat.http;

import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.util.SLog;
import com.sumauto.util.ToastUtil;
import com.umeng.socialize.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lincoln on 16/3/25.
 * http请求处理
 */
public abstract class HttpHandler extends JsonHttpResponseHandler {
    HttpResponse mResponse = new HttpResponse();
    String requestUrl;

    private boolean showErrorMessage;

    public HttpHandler() {
        this(true);
    }

    public HttpHandler(boolean showErrorMessage) {
        this.showErrorMessage = showErrorMessage;
    }

    public HttpHandler setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        try {
            JSONObject jsonResponse = response.getJSONObject("response");
            mResponse.code = jsonResponse.optString("code");
            mResponse.msg = jsonResponse.optString("msg");
            mResponse.data = jsonResponse.optString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("100".equals(mResponse.code)) {
            dispatchSuccess();
        } else {
            if (showErrorMessage) {
                onShowMessage(mResponse);
            }
            dispatchFail();
        }
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        buildErrorMsg(new Throwable("JsonArray is not Accepted!!"),response);
        dispatchFail();
    }

    /**
     * FAIL_01
     */
    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        buildErrorMsg(throwable,"FAIL_01:"+errorResponse);
        dispatchFail();
    }
    /**
     * FAIL_02
     */
    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        buildErrorMsg(throwable,"FAIL_02:"+errorResponse);
        dispatchFail();
    }

    /**
     * FAIL_03
     */
    @Override
    public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        buildErrorMsg(throwable,"FAIL_03:"+responseString);
        dispatchFail();
    }

    /**
     * SUCCESS_01
     */
    @Override
    public final void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        buildErrorMsg(new Throwable(),"SUCCESS_01:"+responseString);
        dispatchFail();
    }

    @Override
    public void onUserException(Throwable error) {
        super.onUserException(error);
        buildErrorMsg(error,"onUserException（）is called");
        dispatchFail();
    }

    private void buildErrorMsg(Throwable throwable,Object response){
        if (throwable!=null){
           // SLog.e(HttpManager.TAG,"",throwable);
        }

        if (response!=null){
            mResponse.data=response.toString();
        }
    }

    private void dispatchSuccess() {
        SLog.d(HttpManager.TAG, "请求结束--->" + requestUrl + ":" + mResponse.toString());
        try {
            onSuccess(mResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatchFail() {
        String msg = String.format("请求结束--->%s:%s", requestUrl, mResponse.toString().replace("\r\n","\n"));
        SLog.d(HttpManager.TAG,  msg);
        ToastUtil.toast(HabitatApp.getInstance(),msg);
        onFailure(mResponse);
    }

    public void onShowMessage(HttpResponse response) {
        if (!TextUtils.isEmpty(response.msg)) {
            ToastUtil.toast(response.msg);
        }
    }

    public abstract void onSuccess(HttpResponse response) throws JSONException;

    public void onFailure(HttpResponse response) {
    }

    /**
     * Created by Lincoln on 16/3/28.
     */
    public static class HttpResponse {
        public String data;
        public String code;
        public String msg;

        @Override
        public String toString() {
            return "HttpResponse{" +
                    "data=" + data  +
                    ", code=" + code +
                    ", msg=" + msg  +
                    '}';
        }
    }
}
