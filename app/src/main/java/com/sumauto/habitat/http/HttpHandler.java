package com.sumauto.habitat.http;

import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sumauto.util.SLog;
import com.sumauto.util.ToastUtil;

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
        SLog.d(HttpManager.TAG, "请求结束--->" + requestUrl + ":" + response.toString());
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
        dispatchFail();
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        dispatchFail();
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        dispatchFail();
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        dispatchFail();
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        dispatchFail();
    }

    @Override
    public void onUserException(Throwable error) {
        super.onUserException(error);
        dispatchFail();
    }

    private void dispatchSuccess() {
        try {
            onSuccess(mResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatchFail() {

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
                    "data='" + data + '\'' +
                    ", code='" + code + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
