package com.sumauto.habitat.bean;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.http.HttpRequest;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public class LoginParser implements HttpRequest<User> {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public RequestParams getRequestParams() {
        return null;
    }

    @Override
    public User parser(String jsonString) {
        return null;
    }
}
