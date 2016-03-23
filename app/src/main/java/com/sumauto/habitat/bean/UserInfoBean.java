package com.sumauto.habitat.bean;

/**
 * Created by Lincoln on 16/3/22.
 * 用户信息
 */
public class UserInfoBean {
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
        return nameFirstLetter;
    }

    public UserInfoBean setNameFirstLetter(String nameFirstLetter) {
        this.nameFirstLetter = nameFirstLetter;
        return this;
    }
}
