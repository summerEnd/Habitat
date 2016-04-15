package com.sumauto.habitat;

import android.test.AndroidTestCase;
import android.util.Log;

import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;

@SuppressWarnings("unchecked")
public class HttpTest extends AndroidTestCase {
    public static final String TAG = "HttpManager";
    HttpManager mHttpManager = HttpManager.getInstance();

    {
        mHttpManager.setAsync(false);
    }

    public void testGetIndex() {
        //http(Requests.getCommunity("1",0,5));
        //http(Requests.getValidateCode("18761844602",""));
        //http(Requests.getRegister("18761844602","","123456"));
        http(Requests.getLogin("18761844602", "123456"));
        //http(Requests.setNewPwd("18761844602", "123456","123456"));
        http(Requests.getChangePwd( "123456","123456"));
        //http(Requests.setHeadImg("123456"));
    }

    public void http(HttpRequest<?> request) {
        mHttpManager.post(getContext(), new JsonHttpHandler(request) {

            @Override
            public void onSuccess(HttpResponse response, HttpRequest request, Object bean) {
                log(response.data);
            }
        });
    }

    private void log(String result) {
        Log.d(TAG, "log: " + result);
    }
}
