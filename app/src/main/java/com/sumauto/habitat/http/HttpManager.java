package com.sumauto.habitat.http;

/**
 * Created by Lincoln on 16/3/25.
 * http接口
 */
public class HttpManager {


    private static String get(String api) {
        String HOST = "http://121.40.123.222";
        return HOST+api;
    }

    public static final String GET_INDEX = "/Api/getIndex";

    public static HttpClient.HttpRequest getIndex(){
        return new HttpClient.HttpRequest(get(GET_INDEX));
    }


}
