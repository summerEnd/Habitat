package com.sumauto.habitat.bean;

import android.text.TextUtils;

/**
 * Created by Lincoln on 16/3/21.
 * 动态
 */
public class FeedBean {
    /**
     * id : 824765
     * img : http://120.76.138.4114618529857573.jpg
     * content : tessssssssssst
     * uid : 808264
     * position : 失乐园
     * addtime : 4天前
     * headimg :
     * nickname :
     */
    public String id;
    public String uid;
    public String img;
    public String content;
    public String position;
    public String addtime;
    public String headimg;
    public String nickname;

    private String iscollect;
    private String isnice;


    public String getFrom() {
        return addtime + (TextUtils.isEmpty(position) ? "" : (" 来自 "+position));
    }
    public boolean isCollection() {
        return "1".equals(iscollect);
    }

    public boolean isNice() {
        return "1".equals(isnice);
    }

    public void setIsNice(boolean isNice) {
        this.isnice=isNice?"1":"0";
    }

    public void setIsCollection(boolean isCollection) {
        this.iscollect = isCollection?"1":"0";
    }

}
