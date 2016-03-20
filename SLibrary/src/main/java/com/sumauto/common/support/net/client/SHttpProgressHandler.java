package com.sumauto.common.support.net.client;

import android.app.Dialog;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sp.lib.BuildConfig;
import com.sumauto.common.util.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public abstract class SHttpProgressHandler extends JsonHttpResponseHandler implements IHttpProgress {
    private Dialog mDialog;

    @Override
    public final void onStart() {
        super.onStart();
        mDialog = onCreateDialog();
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        log("status:" + statusCode + "-->" + responseString);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        log("status:" + statusCode + "-->" + errorResponse);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        log("status:" + statusCode + "-->" + errorResponse);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        log(response);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        log(response);
    }

    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        log(responseString);
    }

    protected void log(Object o) {
        if (!BuildConfig.DEBUG){
            return;
        }
        String log;
        if (o == null) {
            log = "null";
        } else if (o instanceof JSONObject) {
            try {
                log = ((JSONObject) o).toString(4);
            } catch (JSONException e) {
                log = String.valueOf(o);
            }
        } else {
            log = String.valueOf(o);
        }
        SLog.debug(log);
    }

}
