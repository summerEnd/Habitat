package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public abstract class JsonHttpHandler<B> extends HttpHandler{

    private HttpRequest<B> mHttpRequest;
    private B mResult;
    public  JsonHttpHandler(HttpRequest<B> request) {
        this(request,true);
    }

    public JsonHttpHandler(HttpRequest<B> request,boolean showErrorMessage) {
        super(showErrorMessage);
        this.mHttpRequest = request;
    }

    @Override
    public final void onSuccess(HttpResponse response) throws JSONException {
        mResult=mHttpRequest.parser(response.data);
        onSuccess(response,mHttpRequest,mResult);
    }

    public B getResult() {
        return mResult;
    }

    public abstract void onSuccess(HttpResponse response, HttpRequest<B> request, B bean) ;

    public String getUrl() {
        return mHttpRequest.getUrl();
    }

    public RequestParams getRequestParams() {
        return mHttpRequest.getRequestParams();
    }
}
