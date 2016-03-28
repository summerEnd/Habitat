package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;

/**
 * Created by Lincoln on 16/3/25.
 */
public class HttpRequest extends RequestParams{
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
