package com.sumauto.habitat;

import android.test.AndroidTestCase;

import com.loopj.android.http.SyncHttpClient;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpInterface;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lincoln on 16/3/28.
 */
public class HttpTest extends AndroidTestCase{

    public void testGetIndex(){
        HttpRequest request=HttpInterface.getIndex();
        HttpManager.getInstance().postSync(getContext(), request,new HttpHandler(){
            @Override
            public void onSuccess(HttpResponse response, HttpRequest request) {
                super.onSuccess(response, request);
            }
        });
    }
}
