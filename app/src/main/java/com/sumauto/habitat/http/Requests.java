package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;
import com.sumauto.common.util.JsonUtil;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.ArticleEntity;
import com.sumauto.habitat.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/09 5.6.6 
 */
public class Requests {
    private static String HOST = "http://120.76.138.41/qixidi/Api/";

    /**
     * 参数说明：
     * commid：用户所在社区的社区ID
     * pageid:分页起始页数（首页已经加载了pageid=0的前3条，所以此时调用pageid从1开始）
     * pagesize：每页显示数据的数量（pagesize默认为3）
     */
    public static class GetCommunity implements HttpRequest<List<ArticleEntity>>{
        String commid;
        int page;
        int pageSize;

        public GetCommunity(String commid, int page) {
            this(commid,page,5);
        }

        public GetCommunity(String commid, int page, int pageSize) {
            this.commid = commid;
            this.page = page;
            this.pageSize = pageSize;
        }

        @Override
        public String getUrl() {
            return HOST+"getCommunity";
        }

        @Override
        public RequestParams getRequestParams() {
            return new RequestParams("commid",commid,"pageid",page,"pagesize",pageSize);
        }

        @Override
        public List<ArticleEntity> parser(String jsonString) throws JSONException {
            return JsonUtil.getArray(new JSONObject(jsonString).getJSONArray("articlelist"), ArticleEntity.class);
        }
    }

    /**
     * 参数说明：
     * phone:请求发送验证码的手机号码
     * action:获取验证码的操作页面，注册时action=reg，其他不做要求
     */
    public static class GetValidateCode implements HttpRequest{

        @Override
        public String getUrl() {
            return HOST+"getValidateCode";
        }

        @Override
        public RequestParams getRequestParams() {
            return null;
        }

        @Override
        public Object parser(String jsonString) {
            return null;
        }
    }

    /**
     * 参数说明
     * phone:用户注册的手机号码
     * code:用户填写的短信验证码
     * pwd:用户填写的登录密码
     */
    public static class GetRegister implements HttpRequest{

        @Override
        public String getUrl() {
            return HOST+"getRegister";
        }

        @Override
        public RequestParams getRequestParams() {
            return null;
        }

        @Override
        public Object parser(String jsonString) {
            return null;
        }
    }


    public static class GetLogin implements HttpRequest<User>{
        private String phone;
        private String pwd;

        /**
         * 参数说明
         * phone:用户注册的手机号码
         * pwd:用户填写的登录密码
         */
        public GetLogin(String phone, String pwd) {
            this.phone = phone;
            this.pwd = pwd;
        }

        @Override
        public String getUrl() {
            return HOST+"getLogin";
        }

        @Override
        public RequestParams getRequestParams() {
            return new RequestParams("phone",phone,"pwd",pwd);
        }

        @Override
        public User parser(String jsonString) throws JSONException {
            User user = new User().initWith(new JSONObject(jsonString).getJSONObject("login"));
            HabitatApp.getInstance().setUser(user);
            return user;
        }
    }

    /**
     * 参数说明
     * phone:用户的手机号码
     * code:用户获取到的短信验证码
     * newuserpwd:用户设置的新密码
     */
    public static class SetNewPwd implements HttpRequest{

        @Override
        public String getUrl() {
            return HOST+"setNewPwd";
        }

        @Override
        public RequestParams getRequestParams() {
            return null;
        }

        @Override
        public Object parser(String jsonString) {
            return null;
        }
    }
}
