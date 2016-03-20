package com.sumauto.common.support.net.client;

import com.loopj.android.http.RequestParams;

/**
 * {@link SHttpClient} request。
 */
public class SRequest extends RequestParams {
    String url;

    public SRequest(String url){
        setUrl(url);
    }

    public SRequest() {
    }

    public SRequest(Object... keysAndValues)
    {
        super(keysAndValues);
    }

    public String getUrl() {
        return url;
    }
    public String getUrlWithParams(){
        return url + "?" + this.toString();
    }
    public SRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * change the string to a readable string
     */
    public String toLogString() {
        //将RequestParams转化为字符串
        String[] strings = toString().split("&");
        StringBuilder builder = new StringBuilder();
        builder.append(url).append("{\n");
        for (String string : strings)
        {
            builder.append(string).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
