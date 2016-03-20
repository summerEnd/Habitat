package com.sumauto.habitat.bean;

/**
 * Created by Lincoln on 2016/3/14.
 */
public class DemoBean implements Bean{
    String name;
    String content;



    public DemoBean(String name, String content) {
        this.name = name;
        this.content = content;
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return name;
    }
}
