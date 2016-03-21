package com.sumauto.habitat.bean;

/**
 * Created by Lincoln on 16/3/21.
 * 动态
 */
public class FeedBean {

    private boolean isAttention;
    private boolean isCollection;

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isAttention(){
        return isAttention;
    }

    public FeedBean setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
        return this;
    }

    public FeedBean setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
        return this;
    }
}
