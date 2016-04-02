package com.sumauto.habitat.http;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.sumauto.common.admin.Md5;
import com.sumauto.common.util.ContextUtil;


public class HttpClient {
    AsyncHttpClient client = new AsyncHttpClient();
    SyncHttpClient syncHttpClient = new SyncHttpClient();
    private static HttpClient INSTANCE;

    public static HttpClient getInstance() {
        if (INSTANCE==null){
            INSTANCE=new HttpClient();
        }
        return INSTANCE;
    }



    private HttpClient() {
        client.setTimeout(20*1000);
        syncHttpClient.setTimeout(20 * 1000);
    }



    public void post(Context context,HttpRequest params,HttpHandler httpHandler){

        if (params==null){
            params=new HttpRequest("");
        }
        String time = String.valueOf(System.currentTimeMillis());
        String uuid = ContextUtil.getUUID();

        params.put("time", time);
        params.put("uuid", uuid);
        params.put("secret", Md5.md5(uuid+time+"JM9m07ufm2lj43GFva"));
        client.post(context, params.getUrl(), params, httpHandler);

    }

    public void postSync(Context context,HttpRequest request,HttpHandler httpHandler){
        if (request==null){
            request=new HttpRequest("");
        }
        String time = String.valueOf(System.currentTimeMillis());
        String uuid = ContextUtil.getUUID();

        request.put("time", time);
        request.put("uuid", uuid);
        request.put("secret", Md5.md5(uuid+time+"JM9m07ufm2lj43GFva"));
        syncHttpClient.post(context, request.getUrl(), request, httpHandler);
    }

    /**
     * Created by Lincoln on 16/3/28.
     */
    public static class HttpResponse {
        String data;
        String code;
        String msg;
    }

    /**
     * Created by Lincoln on 16/3/25.
     */
    public static class HttpRequest extends RequestParams {
        private String url;

        public HttpRequest(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public HttpRequest setUrl(String url) {
            this.url = url;
            return this;
        }
    }
}
