package com.sumauto.habitat.bean;

import com.sumauto.util.MathUtil;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/16 5.6.6 
 */
public class AboutBean {

    /**
     * id : 17
     * uid : 816392
     * fid : 879833
     * headimg : http://120.76.138.41/qixidi/upload/headimg/14626782818349.jpg
     * nickname : luke
     * type : 0
     * isfriend : 0
     * tid : 0
     * timg :
     * isread : 0
     * addtime : 6天前
     */

    public String id;
    public int uid;
    public int fid;
    public String headimg;
    public String nickname;
    public String type;
    public String isfriend;//在type=0时，表示在关注时，是否互为好友，0->否，1->是
    public String tid;
    public String timg;
    public String isread;//帖子是否已读：0->否，1->是
    public String addtime;

    //消息类型：0->关注，1->赞，2->评论，3->提醒我看
    public int getType() {
        return MathUtil.getInt(type);
    }

    public String getActionString(){
        int type=getType();
        String str="";
        switch (type){
            case 0:{
                str="关注了你";
                break;
            }
            case 1:{
                str="赞了我的帖子";
                break;
            }
            case 2:{
                str="评论了我的帖子";
                break;
            }
            case 3:{
                str="提醒你看";
                break;
            }
        }
        return str;
    }

    public boolean isFriend() {
        return "1".equals(isfriend);
    }
    public void setIsFriend(boolean isFriend) {
        isfriend= isFriend?"1":"0";
    }



}
