package com.sumauto.habitat.bean;

import android.os.Bundle;
import android.text.TextUtils;

import com.sumauto.habitat.HabitatApp;

import org.json.JSONObject;

/**
 * Created by Lincoln on 16/4/2.
 * 用户
 */
@SuppressWarnings("unused")
public class User {

    /**
     * id : 879833
     * phone : 18652947363
     * nickname :
     * headimg :
     */

    private String id;
    private String sex;
    private String phone;
    private String nickname;
    private String headimg;
    private String snsaccount;
    private String addtime;
    private String sort;
    private String commid;
    private String isdel;
    private String type;
    private String password;
    private String birthday;
    private String community;
    private String signature;

    public String articlecount;
    public String followcount;
    public String fanscount;

    public User() {
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(HabitatApp.ACCOUNT_PHONE, phone);
        b.putString(HabitatApp.ACCOUNT_OPENID, snsaccount);
        b.putString(HabitatApp.ACCOUNT_UID, id);
        b.putString(HabitatApp.ACCOUNT_NICK, nickname);
        b.putString(HabitatApp.ACCOUNT_COMMID, commid);
        b.putString(HabitatApp.ACCOUNT_AVATAR, headimg);
        b.putString(HabitatApp.ACCOUNT_BIRTHDAY, birthday);
        b.putString(HabitatApp.ACCOUNT_GENDER, sex);
        b.putString(HabitatApp.ACCOUNT_SIGNATURE, signature);
        b.putString(HabitatApp.ACCOUNT_COMM_NAME, community);
        return b;
    }

}
