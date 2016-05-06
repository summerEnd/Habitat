package com.sumauto.habitat.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.sumauto.common.Md5;
import com.sumauto.util.SLog;
import com.sumauto.util.SUtils;

/**
 * Created by Lincoln on 16/4/2.
 * http接口
 */
public class HttpManager {
    public static final String TAG = HttpManager.class.getSimpleName();


    private static HttpManager mHttpClient;
    private boolean async = true;//是否异步
    AsyncHttpClient client = new AsyncHttpClient();
    SyncHttpClient syncHttpClient = new SyncHttpClient();

    public static HttpManager getInstance() {
        if (mHttpClient == null) {
            mHttpClient = new HttpManager();
        }
        return mHttpClient;
    }

    private HttpManager() {
        client.setTimeout(20 * 1000);
        client.setLoggingEnabled(true);
        syncHttpClient.setTimeout(20 * 1000);
    }

    public HttpManager setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public void post(Context context, JsonHttpHandler httpHandler) {
        post(context, httpHandler.getUrl(), httpHandler.getRequestParams(), httpHandler,true);
    }
    public void postSync(Context context,SyncHttpHandler httpHandler){
        post(context, httpHandler.getUrl(), httpHandler.getRequestParams(), httpHandler,false);
    }

    private void post(Context context, String url, RequestParams params, HttpHandler httpHandler,boolean async) {

        String time = String.valueOf(System.currentTimeMillis());
        String uuid = SUtils.getUUID(context);

        params.put("time", time);
        params.put("uuid", uuid);
        params.put("secret", Md5.md5(uuid + time + "JM9m07ufm2lj43GFva"));
        httpHandler.setRequestUrl(url);
        SLog.d(TAG, "请求开始--->" + url + ":" + params.toString());
        if (async) {
            client.post(context, url, params, httpHandler);
        } else {
            syncHttpClient.post(context, url, params, httpHandler);
        }
    }

    public AsyncHttpClient getClient() {
        return client;
    }
}
