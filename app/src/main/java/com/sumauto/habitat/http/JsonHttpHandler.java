package com.sumauto.habitat.http;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public abstract class JsonHttpHandler<B> extends HttpHandler{

    private HttpRequest<B> mHttpRequest;

    public JsonHttpHandler(HttpRequest<B> request) {
        this(request,true);
    }

    public JsonHttpHandler(HttpRequest<B> request,boolean showErrorMessage) {
        super(showErrorMessage);
        this.mHttpRequest = request;
    }

    @Override
    public final void onSuccess(HttpResponse response) throws JSONException {
        onSuccess(response,mHttpRequest,mHttpRequest.parser(response.data));
    }

    public abstract void onSuccess(HttpResponse response,HttpRequest<B> request,B b) ;

    public String getUrl() {
        return mHttpRequest.getUrl();
    }

    public RequestParams getRequestParams() {
        return mHttpRequest.getRequestParams();
    }
}
