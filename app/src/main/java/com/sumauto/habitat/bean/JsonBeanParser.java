package com.sumauto.habitat.bean;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonBeanParser<T> {
    T parse(JSONObject object) throws JSONException;
}
