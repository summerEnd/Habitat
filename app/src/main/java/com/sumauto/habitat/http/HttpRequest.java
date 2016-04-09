package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public interface HttpRequest <B>{
    String getUrl();
    RequestParams getRequestParams();
    B parser(String jsonString) throws JSONException;
}
