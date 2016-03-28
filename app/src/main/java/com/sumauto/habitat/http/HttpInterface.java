package com.sumauto.habitat.http;

/**
 * Created by Lincoln on 16/3/25.
 */
public class HttpInterface {


    private static String get(String api) {
        String HOST = "http://121.40.123.222";
        return HOST+api;
    }

    public static final String GET_INDEX = "/Api/getIndex";

    public static HttpRequest getIndex(){
        return new HttpRequest(get(GET_INDEX));
    }
}
