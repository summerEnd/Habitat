package com.sumauto.habitat.bean;

import org.json.JSONObject;

/**
 * Created by Lincoln on 16/4/2.
 * 用户
 */
public class User {

    /**
     * id : 879833
     * phone : 18652947363
     * nickname :
     * headimg :
     */

    private String id;
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

    public User() {
    }

    public User initWith(JSONObject object) {
        this.id=object.optString("id");
        this.phone=object.optString("phone");
        this.nickname=object.optString("nickname");
        this.headimg=object.optString("headimg");
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getSnsaccount() {
        return snsaccount;
    }

    public void setSnsaccount(String snsaccount) {
        this.snsaccount = snsaccount;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCommid() {
        return commid;
    }

    public void setCommid(String commid) {
        this.commid = commid;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
