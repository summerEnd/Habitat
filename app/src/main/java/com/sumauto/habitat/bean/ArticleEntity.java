package com.sumauto.habitat.bean;

/**
 * Created by Lincoln on 16/4/2.
 */
public class ArticleEntity {

    /**
     * id : 2
     * img : http://120.76.138.41/qixidi/upload/20150626064217.jpg
     * content : 新华社布拉格3月30日电　国家主席习近平30日圆满结束对捷克为期三天的国事访问，乘专机离开布拉格前往美国首都华盛顿，出席在那里举行的第四届核安全峰会。
     * uid : 1
     * position : 恒大帝景
     * addtime : 2分钟前
     * headimg : http://120.76.138.41/qixidi/upload/20150626064217.jpg
     * nickname : 苦咖啡
     */

    private String id;
    private String img;
    private String content;
    private String uid;
    private String position;
    private String addtime;
    private String headimg;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
