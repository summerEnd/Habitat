package com.sumauto.habitat.bean;

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
    public int uid;
    public String img;
    public String content;
    public String position;
    public String addtime;
    public String headimg;
    public String nickname;

    private boolean isNice;
    private boolean isCollection;

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isNice() {
        return isNice;
    }

    public FeedBean setIsNice(boolean isNice) {
        this.isNice = isNice;
        return this;
    }

    public FeedBean setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
        return this;
    }
}
