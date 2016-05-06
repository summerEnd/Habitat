package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public  class SyncHttpHandler<B> extends JsonHttpHandler<B>{


    public SyncHttpHandler(HttpRequest<B> request) {
        super(request,false);
    }

    @Override
    public void onSuccess(HttpResponse response, HttpRequest<B> request, B bean) {

    }
}
