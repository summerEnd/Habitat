package com.sumauto.habitat.bean;

import android.os.Parcelable;

import com.sumauto.util.SUtils;

/**
 * Created by Lincoln on 16/3/22.
 * 用户信息
 */
public class UserInfoBean {

    public String articlecount;//动态数量
    public String followcount;//关注用户数
    public String fanscount;//粉丝数

    public String id;
    public String sex;
    public String phone;
    public String nickname;
    public String headimg;
    public String snsaccount;
    public String addtime;
    public String sort;
    public String commid;
    public String isdel;
    public String type;
    public String password;
    public String birthday;
    public String community;
    public String signature;
    public String title;
    public String isfriend;
    private boolean isAttention;
    private String nameFirstLetter;//名称首字母

    public boolean isAttention() {
        return isAttention;
    }

    public UserInfoBean setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
        return this;
    }

    public String getNameFirstLetter() {
        return SUtils.getFirstLetter(nickname);
    }

    public String getNameSort(){
        return SUtils.getShortOfString(nickname);
    }

    public boolean isFriend(){
        return "1".equals(isfriend);
    }

    public boolean isBoy(){
        return "男".equals(sex);
    }

    public String getNick() {
        return nickname;
    }

    public UserInfoBean setNick(String nick) {
        this.nickname = nick;
        return this;
    }
}
