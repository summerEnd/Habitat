package com.sumauto.habitat.bean;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Lincoln on 16/3/21.
 * 动态
 */
public class FeedDetailBean {
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
    private ArrayList<String> allimg;
    public String content;
    public String position;
    public String addtime;
    public String headimg;
    public String nickname;

    private boolean isNice;
    private boolean isCollection;
    /**
     * hits : 62
     * commentcount : 10
     * nicecount : 1
     * ids : 1,6,3,5,7,4,8,2
     * img : http://120.76.138.41/qixidi/upload/articleimg/14621954409523.jpg
     */

    private String hits;
    private String commentcount;
    private String nicecount;
    private String ids;
    private String img;

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isNice() {
        return isNice;
    }

    public FeedDetailBean setIsNice(boolean isNice) {
        this.isNice = isNice;
        return this;
    }

    public FeedDetailBean setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
        return this;
    }

    public String getFrom() {
        return addtime + (TextUtils.isEmpty(position) ? "" : (" 来自 " + position));
    }

    public String getCover() {
        if (allimg==null||allimg.isEmpty()){
            return "";
        }
        return allimg.get(0);
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public int getCommentcount() {
        try {
            return Integer.parseInt(commentcount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public String getNicecount() {
        return nicecount;
    }


    public String getIds() {
        return ids;
    }


    public String getImg() {
        return img;
    }

}
