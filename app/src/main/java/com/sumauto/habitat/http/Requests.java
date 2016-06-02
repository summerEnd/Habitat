package com.sumauto.habitat.http;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.AboutBean;
import com.sumauto.habitat.bean.AttentionBean;
import com.sumauto.habitat.bean.ImageBean;
import com.sumauto.habitat.bean.CommentBean;
import com.sumauto.habitat.bean.CommitBean;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.FeedDetailBean;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.util.JsonUtil;
import com.sumauto.util.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	所有的接口都在这里
 * History:		2016/04/09 5.6.6 
 */
public class Requests {
    private static final String HOST = "http://120.76.138.41/qixidi/Api/";
    public static final String nickname = "nickname";//用户头像的base64加密后的密文
    public static final String sex = "sex";
    public static final String birthday = "birthday";
    @SuppressWarnings("unused") public static final String commid = "commid";
    public static final String signature = "signature";
    public static final String headimg = "headimg";

    private static String getUid() {
        try {
            return HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
        } catch (NotLoginException e) {
            return "";
        }
    }

    public static String getShareUrl(String tid){
        return "http://120.76.138.41/qixidi/Index/article/id/"+tid+".html";
    }

    public static HttpRequest<String> getUploadUrl(final File file) {
        return new SimpleHttpRequest<String>("uploadImg") {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }

            @Override
            public RequestParams getRequestParams() {

                RequestParams params = new RequestParams();
                params.put("uid", getUid());
                try {
                    params.put("image", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
    }


    /**
     * 参数说明：
     * commid：用户所在社区的社区ID
     * pageid:分页起始页数（首页已经加载了pageid=0的前3条，所以此时调用pageid从1开始）
     * pagesize：每页显示数据的数量（pagesize默认为3）
     */
    public static HttpRequest<List<FeedBean>> getCommunity(String commid, int page, int pageSize) {

        return new SimpleHttpRequest<List<FeedBean>>("getCommunity",
                "commid", commid, "pageid", page, "pagesize", pageSize) {

            @Override
            public List<FeedBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONObject(jsonString).getJSONArray("articlelist"), FeedBean.class);
            }
        };
    }

    /**
     * 参数说明：
     * phone:请求发送验证码的手机号码
     * action:获取验证码的操作页面，注册时action=reg，其他不做要求
     */
    public static HttpRequest<String> getValidateCode(String phone, String action) {
        return new SimpleHttpRequest<String>("getValidateCode",
                "phone", phone, "action", action) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 参数说明
     * phone:用户注册的手机号码
     * code:用户填写的短信验证码
     * pwd:用户填写的登录密码
     */
    public static HttpRequest<String> getRegister(String phone, String code, String pwd) {
        return new SimpleHttpRequest<String>("getRegister",
                "phone", phone, "code", code, "pwd", pwd) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 参数说明
     * phone:用户注册的手机号码
     * pwd:用户填写的登录密码
     */
    public static HttpRequest<User> getLogin(String phone, String pwd) {
        return new SimpleHttpRequest<User>("getLogin",
                "phone", phone, "pwd", pwd) {
            @Override
            public User parser(String jsonString) throws JSONException {
                return JsonUtil.get(new JSONObject(jsonString).getJSONObject("login"), User.class);
            }
        };
    }

    /**
     * 参数说明
     * nickname:昵称
     * headimg:头像
     * snsaccount:第三方账号
     */
    public static HttpRequest<User> getThirdLogin(String nickname, String headimg, String snsaccount) {
        return new SimpleHttpRequest<User>("getThirdLogin",
                "nickname", nickname, "headimg", headimg, "snsaccount", snsaccount) {
            @Override
            public User parser(String jsonString) throws JSONException {

                return JsonUtil.get(new JSONObject(jsonString), User.class);
            }
        };
    }

    /**
     * 参数说明
     * phone:用户的手机号码
     * code:用户获取到的短信验证码
     * newuserpwd:用户设置的新密码
     */
    public static HttpRequest<User> setNewPwd(String phone, String code, String newuserpwd) {
        return new SimpleHttpRequest<User>("setNewPwd",
                "phone", phone, "code", code, "newuserpwd", newuserpwd) {
            @Override
            public User parser(String jsonString) throws JSONException {
                return JsonUtil.get(jsonString, User.class);
            }
        };
    }

    /**
     * uid:用户注册后的用户编码，登录或注册时可取得
     * olduserpwd:用户填写的原登录密码
     * newuserpwd:用户填写的新的登录密码
     */
    public static HttpRequest<String> getChangePwd(String olduserpwd, String newuserpwd) {
        String id = getUid();
        return new SimpleHttpRequest<String>("getChangePwd",
                "uid", id, "olduserpwd", olduserpwd, "newuserpwd", newuserpwd) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return "";
            }
        };
    }

    //    /**
    //     * 参数说明
    //     * uid:用户编号
    //     * Headimg:用户头像的base64加密后的密文
    //     */
    //    public static HttpRequest<String> setHeadImg(String img) {
    //        String id = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
    //        return new SimpleHttpRequest<String>("setHeadImg",
    //                "uid", id, "Headimg", img) {
    //            @Override
    //            public String parser(String jsonString) throws JSONException {
    //                return jsonString;
    //            }
    //        };
    //    }

    /**
     * uid:用户编号
     * tid:需要收藏的主体编号
     */
    public static HttpRequest<String> collectSubject(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("collectSubject",
                "uid", id, "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要收藏的主体编号
     */
    public static HttpRequest<String> uncollectSubject(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("uncollectSubject",
                "uid", id, "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要点赞的主体编号
     */
    public static HttpRequest<String> niceSubject(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("niceSubject",
                "uid", id, "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return "";
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要点赞的主体编号
     */
    public static HttpRequest<String> unniceSubject(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("unniceSubject",
                "uid", id, "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * uid:用户编号
     * Tid:主体ID
     * Content：评论内容，不能为空
     * ruid:回复的用户ID
     * rcid:回复的评论ID
     */
    public static HttpRequest<Object> submitComment(String tid, String content, String ruid, String rcid) {
        String id = getUid();
        return new SimpleHttpRequest<Object>("submitComment",
                "uid", id, "tid", tid, "content", content, "ruid", ruid, "rcid", rcid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 户查看评论接口：
     * Tid:主体ID
     * Pageid:分页起始页数（首页已经加载了pageid=0的前5条，所以此时调用pageid从1开始）
     * Pagesize：每页显示数据的数量（pagesize默认为5）
     */
    public static HttpRequest<List<CommentBean>> getComment(String tid, int pageId, int pageSize) {
        String id = getUid();
        return new SimpleHttpRequest<List<CommentBean>>("getComment",
                "uid", id, "tid", tid, "pageid", pageId, "pagesize", pageSize) {
            @Override
            public List<CommentBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), CommentBean.class);
            }
        };
    }

    /**
     * 用户删除自己提交评论
     *
     * @param tid 评论id
     */
    @SuppressWarnings("unused")
    public static HttpRequest<String> delComment(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("delComment",
                "uid", id, "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 获取帖子内容详情接口
     *
     * @param tid 帖子id
     */
    public static HttpRequest<FeedDetailBean> getSubjectDetail(String tid) {
        String id = getUid();
        return new SimpleHttpRequest<FeedDetailBean>("getSubjectDetail",
                "uid", id, "tid", tid) {
            @Override
            public FeedDetailBean parser(String jsonString) throws JSONException {
                return JsonUtil.get(jsonString, FeedDetailBean.class);
            }
        };
    }

    /**
     * 获取帖子被赞用户接口
     *
     * @param tid 帖子id
     */
    public static HttpRequest<List<UserInfoBean>> getSubjectNice(String tid, int pageid) {
        String id = getUid();
        return new SimpleHttpRequest<List<UserInfoBean>>("getSubjectNice",
                "uid", id, "tid", tid, "pageid", pageid, "pagesize", 12) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), UserInfoBean.class);
            }
        };
    }

    /**
     * 用户关注接口
     *
     * @param uid 需要关注的对方用户ID
     */
    public static HttpRequest<String> setFollow(String uid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("setFollow",
                "mid", id, "uid", uid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 用户取消关注接口
     *
     * @param uid 需要关注的对方用户ID
     */
    public static HttpRequest<String> setUnFollow(String uid) {
        String id = getUid();
        return new SimpleHttpRequest<String>("setUnFollow",
                "mid", id, "uid", uid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 用户修改个人信息接口
     * <p/>
     * nickname:昵称
     * sex:性别（男/女）
     * birthday:生日
     * commid:社区ID
     * signature:个性签名
     * headimg:用户头像的base64加密后的密文
     */
    public static HttpRequest<User> changeUserInfo(Bundle changes) throws FileNotFoundException {
        final RequestParams params = new RequestParams();
        if (changes.containsKey(headimg)) {
            File file = new File(changes.getString(headimg));
            params.put(headimg, file);
            changes.remove(headimg);//从bundle中移除
        }

        String id = getUid();
        params.put("uid", id);
        Set<String> keySet = changes.keySet();
        for (String key : keySet) {
            params.put(key, changes.getString(key));
        }
        return new SimpleHttpRequest<User>("changeUserInfo") {
            @Override
            public User parser(String jsonString) throws JSONException {
                return JsonUtil.get(jsonString, User.class);
            }

            @Override
            public RequestParams getRequestParams() {
                return params;
            }
        };
    }


    /**
     * @param title:社区名称
     * @param province:省份
     * @param city:城市
     * @param area:区/县
     * @param address:具体地址
     */
    public static HttpRequest<String> createCommunity(String title, String province, String city, String area, String address) {
        String id = getUid();
        return new SimpleHttpRequest<String>("createCommunity",
                "uid", id, "title", title, "province", province, "city", city, "area", area, "address", address) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * @param content:帖子内容
     * @param openlevel:谁可以看（默认为0，表示所有人可见；仅自己可见时，改项为自己的uid；部分不见时，改项为选择的好友用户ID）
     * @param remind:提醒谁看（选择的好友用户ID）
     * @param ids：上传的图片ID列表，如1,2,3,4,5字符串
     */
    public static HttpRequest<String> publishArticle(String content, String openlevel, String remind, String ids) {
        String id = getUid();
        return new SimpleHttpRequest<String>("publishArticle",
                "uid", id, "content", content, "openlevel", openlevel, "remind", remind, "ids", ids) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * @param keyword:搜索的关键词
     * @param type:搜索类型，共4种：0->搜索全部（默认值）,1->搜索用户，2->搜索圈子，3->搜索帖子，用于单个类型分页查询
     * @param pageid:分页ID，默认为0
     * @param pagesize:每页数量，默认8条
     */
    public static HttpRequest<ArrayList<Object>> getSearch(String keyword, String type, int pageid, int pagesize) {
        String id = getUid();
        return new SimpleHttpRequest<ArrayList<Object>>("getSearch",
                "uid", id, "keyword", keyword, "type", type, "pageid", pageid, "pagesize", pagesize) {
            @Override
            public ArrayList<Object> parser(String jsonString) throws JSONException {
                JSONObject result = new JSONObject(jsonString);
                ArrayList<Object> data = new ArrayList<>();

                JSONArray tempArray = result.optJSONArray("userinfo");
                if (tempArray != null) {
                    data.addAll(JsonUtil.getArray(tempArray, UserInfoBean.class));
                }

                tempArray = result.optJSONArray("community");
                if (tempArray != null) {
                    data.addAll(JsonUtil.getArray(tempArray, CommitBean.class));
                }

                tempArray = result.optJSONArray("article");
                if (tempArray != null) {
                    data.addAll(JsonUtil.getArray(tempArray, FeedBean.class));
                }
                return data;
            }
        };
    }

    /**
     * 首页搜索页面接口
     * 返回一个长度为3的Object数组
     * 第一个是banner，{@code ArrayList<ImageBean>}
     * 第二个是推荐用户列表{@code ArrayList<UserInfoBean>}
     * 第三个是动态列表{@code ArrayList<FeedBean>}
     */
    public static HttpRequest<Object[]> searchInfo(int pageid, int pagesize) {
        String id = getUid();
        return new SimpleHttpRequest<Object[]>("searchInfo",
                "uid", id, "pageid", pageid, "pagesize", pagesize) {
            @Override
            public Object[] parser(String jsonString) throws JSONException {
                Object[] objects = new Object[3];
                JSONObject object = new JSONObject(jsonString);
                JSONArray banner = object.optJSONArray("banner");
                JSONArray recommendUser = object.optJSONArray("userinfo");
                JSONArray article = object.optJSONArray("article");

                if (banner != null) {
                    objects[0] = JsonUtil.getArray(banner, ImageBean.class);
                }

                if (recommendUser != null) {
                    objects[1] = JsonUtil.getArray(recommendUser, UserInfoBean.class);
                }

                if (article != null) {
                    objects[2] = JsonUtil.getArray(article, FeedBean.class);
                }

                return objects;
            }
        };
    }

    public static HttpRequest<User> getUserInfo() {
        String id = getUid();

        return new SimpleHttpRequest<User>("getUserinfo",
                "uid", id) {
            @Override
            public User parser(String jsonString) throws JSONException {
                JSONObject data = new JSONObject(jsonString);
                User user = new User();
                user.articlecount = data.optString("articlecount");
                user.followcount = data.optString("followcount");
                user.fanscount = data.optString("fanscount");

                return user;
            }
        };
    }

    public static HttpRequest<List<FeedBean>> getCollect(String uid) {
        return new SimpleHttpRequest<List<FeedBean>>("getCollect",
                "uid", uid) {
            @Override
            public List<FeedBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), FeedBean.class);
            }
        };
    }

    public static HttpRequest<UserInfoBean> getFriendInfo(String uid) {
        return new SimpleHttpRequest<UserInfoBean>("getFriendinfo",
                "uid", getUid(), "fid", uid) {
            @Override
            public UserInfoBean parser(String jsonString) throws JSONException {
                return JsonUtil.get(new JSONObject(jsonString), UserInfoBean.class);
            }
        };
    }

    /**
     * 29.	获取获取好友动态接口
     */
    public static HttpRequest<List<AttentionBean>> getActiveInfo(int page) {
        return new SimpleHttpRequest<List<AttentionBean>>("getActiveInfo",
                "uid", getUid(), "pageid", page, "pagesize", 10) {
            @Override
            public List<AttentionBean> parser(String jsonString) throws JSONException {
                List<AttentionBean> attentionBeanList = new ArrayList<>();

                if (JsonUtil.isJsonArray(jsonString)){
                    JSONArray data = new JSONArray(jsonString);
                    int length = data.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject opt = data.optJSONObject(i);
                        if (opt != null) {

                            AttentionBean bean = JsonUtil.get(
                                    opt.getJSONObject("userinfo"),
                                    AttentionBean.class);

                            JSONArray articlelist = opt.optJSONArray("articlelist");
                            bean.articleList = new ArrayList<>();
                            if (articlelist != null) {
                                int articleNum = articlelist.length();
                                for (int j = 0; j < articleNum; j++) {
                                    ImageBean imageBean = new ImageBean();
                                    JSONObject article = articlelist.getJSONObject(i);
                                    imageBean.id = article.optString("tid");
                                    imageBean.img = article.optString("timg");
                                    bean.articleList.add(imageBean);
                                }
                            } else {
                                SLog.e("erro", "articlelist is " + opt.optString("articlelist"));
                            }


                            attentionBeanList.add(bean);
                        }
                    }
                }

                return attentionBeanList;
            }
        };
    }

    /**
     * 30.	获取关于我的消息列表接口：
     * pageid
     */
    public static HttpRequest<List<AboutBean>> getUserMessage(int pageid,String fid) {
        return new SimpleHttpRequest<List<AboutBean>>("getUserMessage",
                "uid", getUid(),"fid",fid, "pageid", pageid, "pagesize", 10) {
            @Override
            public List<AboutBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), AboutBean.class);
            }
        };
    }

    /**
     * 31.	获取用户微博流图片列表接口：
     */
    public static HttpRequest<List<JSONObject>> getImgList(String uid, int pageid, int pagesize) {
        return new SimpleHttpRequest<List<JSONObject>>("getImgList",
                "uid", getUid(), "fid", uid, "pageid", pageid, "pagesize", pagesize) {
            @Override
            public List<JSONObject> parser(String jsonString) throws JSONException {
                ArrayList<JSONObject> arrayMap = new ArrayList<>();
                JSONArray array = new JSONArray(jsonString);
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    arrayMap.add(jsonObject);
                }
                return arrayMap;
            }
        };
    }

    /**
     * 32.	获取用户好友通讯录接口
     */
    public static HttpRequest<List<UserInfoBean>> getUserFriends() {
        return new SimpleHttpRequest<List<UserInfoBean>>("getUserFriends",
                "uid", getUid()) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), UserInfoBean.class);
            }
        };
    }

    /**
     * 33.	检测通讯录中的好友状态，是否已注册账号，已注册的是否为好友：
     */
    public static HttpRequest<HashMap<String, String>> chargeListStatus(String phones) {
        return new SimpleHttpRequest<HashMap<String, String>>("chargeListStatus",
                "uid", getUid(), "phones", phones) {
            @Override
            public HashMap<String, String> parser(String jsonString) throws JSONException {
                JSONArray statusList = new JSONArray(jsonString);
                HashMap<String, String> map = new HashMap<>();
                int length = statusList.length();
                for (int i = 0; i < length; i++) {
                    JSONObject data = statusList.getJSONObject(i);
                    //（0->未注册，1->已注册未关注，2->已关注）
                    map.put(data.getString("phone"), data.getString("status"));

                }
                return map;
            }
        };
    }

    /**
     * 34.	用户删除帖子接口
     */
    public static HttpRequest<String> delSubject(String tid) {
        return new SimpleHttpRequest<String>("delSubject",
                "uid", getUid(), "tid", tid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    /**
     * 35.	获取我的好友动态接口：
     */
    public static HttpRequest<List<FeedBean>> getMyFriendActive(String uid, int page) {
        return new SimpleHttpRequest<List<FeedBean>>("getFriendActive",
                "uid", uid, "pageid", page, "pagesize", 5) {
            @Override
            public List<FeedBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(
                        new JSONObject(jsonString).getJSONArray("articlelist"), FeedBean.class);
            }
        };
    }

    /**
     * 36.	把好友加入到黑名单接口：
     */
    public static HttpRequest<List<UserInfoBean>> joinFriendToBlack() {
        return new SimpleHttpRequest<List<UserInfoBean>>("joinFriendToBlack",
                "uid", getUid()) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 36.	38.	获取好友的用户中心个人信息接口：：
     */
    public static HttpRequest<UserInfoBean> getFriendUserinfo(String fid) {
        return new SimpleHttpRequest<UserInfoBean>("getFriendUserinfo",
                "uid", getUid(), "fid", fid) {
            @Override
            public UserInfoBean parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 39.	获取获取黑名单列表接口：：
     * 可以查看别人的黑名单
     */
    public static HttpRequest<List<UserInfoBean>> getBlackList(String fid) {
        return new SimpleHttpRequest<List<UserInfoBean>>("getBlackList",
                "uid", getUid(), "fid", fid) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString),UserInfoBean.class);
            }
        };
    }

    /**

     */
    public static HttpRequest<UserInfoBean> updateUserLocation(String fid) {
        return new SimpleHttpRequest<UserInfoBean>("updateUserLocation",
                "uid", getUid(), "fid", fid) {
            @Override
            public UserInfoBean parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    public static HttpRequest<List<FeedBean>> getNearBy(int page, double latitude, double longitude) {
        return new SimpleHttpRequest<List<FeedBean>>("getNearBy",
                "uid", getUid(), "pageid", page, "latitude", latitude, "longitude", longitude) {
            @Override
            public List<FeedBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONObject(jsonString).getJSONArray("articlelist"),FeedBean.class);
            }
        };
    }

    public static HttpRequest<List<UserInfoBean>> getMyFriends(int page, String fid) {
        return new SimpleHttpRequest<List<UserInfoBean>>("getMyFriends",
                "uid", getUid(), "pageid", page, "fid", fid) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), UserInfoBean.class);
            }
        };
    }

    public static HttpRequest<List<UserInfoBean>> getMyFans(int page, String fid) {
        // FIXME: 16/5/31 万恶的接口写反了
        String uid = getUid();
        //fid = getUid();
        return new SimpleHttpRequest<List<UserInfoBean>>("getMyFans",
                "uid", uid, "fid", fid, "pageid", page) {
            @Override
            public List<UserInfoBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONArray(jsonString), UserInfoBean.class);
            }
        };
    }

    public static HttpRequest<List<FeedBean>> getMyActive(int page, String fid) {
        return new SimpleHttpRequest<List<FeedBean>>("getMyActive",
                "uid", getUid(), "fid", fid, "pageid", page) {
            @Override
            public List<FeedBean> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONObject(jsonString).getJSONArray("articlelist"), FeedBean.class);
            }
        };
    }

    public static HttpRequest<String> removeFriendFromBlack(String fid) {
        return new SimpleHttpRequest<String>("removeFriendFromBlack",
                "uid", getUid(), "fid", fid) {
            @Override
            public String parser(String jsonString) throws JSONException {
                return jsonString;
            }
        };
    }

    private static abstract class SimpleHttpRequest<B> implements HttpRequest<B> {
        private String url;
        private Object[] params;

        public SimpleHttpRequest(String url, Object... params) {
            this.url = url;
            this.params = params;
        }

        @Override
        public RequestParams getRequestParams() {
            return new RequestParams(params);
        }

        @Override
        public String getUrl() {
            return HOST + url;
        }

        @Override
        public abstract B parser(String jsonString) throws JSONException;
    }

}
