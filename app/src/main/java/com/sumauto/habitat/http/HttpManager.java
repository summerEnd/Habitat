package com.sumauto.habitat.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.sumauto.common.admin.Md5;
import com.sumauto.common.util.ContextUtil;
import com.sumauto.common.util.SLog;

/**
 * Created by Lincoln on 16/4/2.
 * http接口
 */
public class HttpManager {
    public static final String TAG = HttpManager.class.getSimpleName();


    private static HttpManager mHttpClient;
    private boolean async = true;//是否异步
    AsyncHttpClient client = new AsyncHttpClient();
    SyncHttpClient syncHttpClient = new SyncHttpClient();

    public static HttpManager getInstance() {
        if (mHttpClient == null) {
            mHttpClient = new HttpManager();
        }
        return mHttpClient;
    }

    private HttpManager() {
        client.setTimeout(20 * 1000);
        syncHttpClient.setTimeout(20 * 1000);
    }

    public HttpManager setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public void postApi(Context context, String api, RequestParams params, HttpHandler httpHandler) {
        post(context, HOST + api, params, httpHandler);
    }

    private void post(Context context, String url, RequestParams params, HttpHandler httpHandler) {

        String time = String.valueOf(System.currentTimeMillis());
        String uuid = ContextUtil.getUUID();

        params.put("time", time);
        params.put("uuid", uuid);
        params.put("secret", Md5.md5(uuid + time + "JM9m07ufm2lj43GFva"));
        httpHandler.setRequestUrl(url);
        if (async) {
            client.post(context, url, params, httpHandler);
        } else {
            syncHttpClient.post(context, url, params, httpHandler);
        }
        SLog.log(TAG, "请求开始--->" + url + ":" + params.toString());
    }


    private static String HOST = "http://120.76.138.41/qixidi/Api/";

    /**
     * 参数说明：
     * commid：用户所在社区的社区ID
     * pageid:分页起始页数（首页已经加载了pageid=0的前3条，所以此时调用pageid从1开始）
     * pagesize：每页显示数据的数量（pagesize默认为3）
     */
    public static final String getCommunity = "getCommunity";

    /**
     * 参数说明：
     * phone:请求发送验证码的手机号码
     * action:获取验证码的操作页面，注册时action=reg，其他不做要求
     */
    public static final String getValidateCode = "getValidateCode";

    /**
     * 参数说明
     * phone:用户注册的手机号码
     * code:用户填写的短信验证码
     * pwd:用户填写的登录密码
     */
    public static final String getRegister = "getRegister";

    /**
     * 参数说明
     * phone:用户注册的手机号码
     * pwd:用户填写的登录密码
     */
    public static final String getLogin = "getLogin";
    /**
     * 参数说明
     * phone:用户的手机号码
     * code:用户获取到的短信验证码
     * newuserpwd:用户设置的新密码
     */
    public static final String setNewPwd = "setNewPwd";

}
