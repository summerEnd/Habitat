package com.sumauto.habitat.bean;

import java.util.List;

/**
 * Created by Lincoln on 16/3/28.
 * 首页
 */
public class IndexBean {

    /**
     * id : 23
     * title : 20151207
     */

    private MagazineEntity magazine;
    /**
     * img : http://app.albb666.com/Admin/Uploads/56f350ac8b0cf.jpg
     * id : 73
     * hits : 92
     * title : “小李子”的天赋从何而来？
     * img_banner : http://app.albb666.com/Admin/Uploads/56f350c59f519.jpg
     */

    private List<ActivelistEntity> activelist;

    public MagazineEntity getMagazine() {
        return magazine;
    }

    public void setMagazine(MagazineEntity magazine) {
        this.magazine = magazine;
    }

    public List<ActivelistEntity> getActivelist() {
        return activelist;
    }

    public void setActivelist(List<ActivelistEntity> activelist) {
        this.activelist = activelist;
    }

    public static class MagazineEntity {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ActivelistEntity {
        private String img;
        private String id;
        private String hits;
        private String title;
        private String img_banner;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHits() {
            return hits;
        }

        public void setHits(String hits) {
            this.hits = hits;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_banner() {
            return img_banner;
        }

        public void setImg_banner(String img_banner) {
            this.img_banner = img_banner;
        }
    }
}
