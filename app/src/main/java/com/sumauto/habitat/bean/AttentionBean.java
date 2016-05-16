package com.sumauto.habitat.bean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/25.
 * 关注列表
 */
public class AttentionBean {
    private List<String> images;
    /**
     * uid : 1
     * nickname : 苦咖啡
     * uimg : http://120.76.138.41/qixidi/upload/headimg/14626824215777.jpg
     * addtime : 5小时前
     * count : 5
     */
    public String uid;
    public String nickname;
    public String uimg;
    public String addtime;
    public String count;
    /**
     * 每个元素 是一个JsonObject
     * 有两个字段 tid,timg
     */
    public ArrayList<ImageBean> articleList;

    public List<String> getImages() {
        return images;
    }


}
