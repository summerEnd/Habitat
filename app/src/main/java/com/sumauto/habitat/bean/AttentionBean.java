package com.sumauto.habitat.bean;

import java.util.List;

/**
 * Created by Lincoln on 16/3/25.
 * 关注列表
 */
public class AttentionBean {
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public AttentionBean setImages(List<String> images) {
        this.images = images;
        return this;
    }
}
