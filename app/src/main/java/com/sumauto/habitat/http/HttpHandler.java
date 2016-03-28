package com.sumauto.habitat.http;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sumauto.common.util.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lincoln on 16/3/25.
 * http请求处理
 */
public class HttpHandler extends JsonHttpResponseHandler{
    HttpResponse mResponse=new HttpResponse();
    HttpRequest mRequest;

    public HttpRequest getRequest() {
        return mRequest;
    }

    public HttpHandler setRequest(HttpRequest mRequest) {
        this.mRequest = mRequest;
        return this;
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        try {
            JSONObject jsonResponse=response.getJSONObject("response");
            mResponse.code=jsonResponse.optString("code");
            mResponse.msg=jsonResponse.optString("msg");
            mResponse.data=jsonResponse.optString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        onSuccess(mResponse,mRequest);
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        onSuccess(mResponse, mRequest);
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure(mResponse, mRequest);
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure(mResponse, mRequest);
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onFailure(mResponse, mRequest);

    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        onFailure(mResponse,mRequest);
    }


    public void onSuccess(HttpResponse response,HttpRequest request){

    }

    public void onFailure(HttpResponse response,HttpRequest request){

    }
}
