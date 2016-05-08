package com.sumauto.habitat.bean;

import java.util.ArrayList;

/**
 * Created by Lincoln on 16/3/21.
 * 动态
 */
public class FeedDetailBean extends FeedBean {

    private ArrayList<String> allimg;
    private String hits;
    private String commentcount;
    private String nicecount;
    private String ids;

    public String getCover() {
        if (allimg == null || allimg.isEmpty()) {
            return "";
        }
        return allimg.get(0);
    }

    public String getHits() {
        return hits;
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

}
