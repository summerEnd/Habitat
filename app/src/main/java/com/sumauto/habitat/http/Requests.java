package com.sumauto.habitat.http;

import com.loopj.android.http.RequestParams;
import com.sumauto.util.JsonUtil;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.ArticleEntity;
import com.sumauto.habitat.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	所有的接口都在这里
 * History:		2016/04/09 5.6.6 
 */
public class Requests {
    private static final String HOST = "http://120.76.138.41/qixidi/Api/";


    /**
     * 参数说明：
     * commid：用户所在社区的社区ID
     * pageid:分页起始页数（首页已经加载了pageid=0的前3条，所以此时调用pageid从1开始）
     * pagesize：每页显示数据的数量（pagesize默认为3）
     */
    public static HttpRequest<List<ArticleEntity>> getCommunity(String commid, int page, int pageSize) {
        return new SimpleHttpRequest<List<ArticleEntity>>("getCommunity",
                "commid", commid, "pageid", page, "pagesize", pageSize) {

            @Override
            public List<ArticleEntity> parser(String jsonString) throws JSONException {
                return JsonUtil.getArray(new JSONObject(jsonString).getJSONArray("articlelist"), ArticleEntity.class);
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
                User user = HabitatApp.getInstance().geUser();
                user.initWith(new JSONObject(jsonString).getJSONObject("login"));
                return user;
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
    public static HttpRequest<?> getChangePwd(String olduserpwd, String newuserpwd) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("getChangePwd",
                "uid", id, "olduserpwd", olduserpwd, "newuserpwd", newuserpwd) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 参数说明
     * uid:用户编号
     * Headimg:用户头像的base64加密后的密文
     */
    public static HttpRequest<?> setHeadImg(String img) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("setHeadImg",
                "uid", id, "Headimg", img) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要收藏的主体编号
     */
    public static HttpRequest<?> collectSubject(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("collectSubject",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要收藏的主体编号
     */
    public static HttpRequest<?> uncollectSubject(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("uncollectSubject",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要点赞的主体编号
     */
    public static HttpRequest<?> niceSubject(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("niceSubject",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * uid:用户编号
     * tid:需要点赞的主体编号
     */
    public static HttpRequest<?> unniceSubject(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("unniceSubject",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
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
    public static HttpRequest<?> submitComment(String tid, String content, String ruid, String rcid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("submitComment",
                "uid", id, "tid", tid, "content", content, "ruid", ruid, "rcid", rcid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 户查看评论接口：
     * Tid:主体ID
     * Pageid:分页起始页数（首页已经加载了pageid=0的前5条，所以此时调用pageid从1开始）
     * Pagesize：每页显示数据的数量（pagesize默认为5）
     */
    public static HttpRequest<?> getComment(String tid, int pageId, int pageSize) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("getComment",
                "uid", id, "tid", tid, "pageId", pageId, "pageSize", pageSize) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 用户删除自己提交评论
     *
     * @param tid 评论id
     */
    public static HttpRequest<?> delComment(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("delComment",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 获取帖子内容详情接口
     *
     * @param tid 帖子id
     */
    public static HttpRequest<?> getSubjectDetail(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("getSubjectDetail",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 获取帖子被赞用户接口
     *
     * @param tid 帖子id
     */
    public static HttpRequest<?> getSubjectNice(String tid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("getSubjectNice",
                "uid", id, "tid", tid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 用户关注接口
     *
     * @param uid 需要关注的对方用户ID
     */
    public static HttpRequest<?> setFollow(String uid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("setFollow",
                "mid", id, "uid", uid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 用户取消关注接口
     *
     * @param uid 需要关注的对方用户ID
     */
    public static HttpRequest<?> setUnFollow(String uid) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("setUnFollow",
                "mid", id, "uid", uid) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
            }
        };
    }

    /**
     * 用户修改个人信息接口
     *
     * @param nickname:昵称
     * @param sex:性别（男/女）
     * @param birthday:生日
     * @param commid:社区ID
     * @param signature:个性签名
     * @param headimg:用户头像的base64加密后的密文
     */
    public static HttpRequest<?> changeUserInfo(
            String nickname, String sex, String birthday, String commid, String signature, String headimg) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("changeUserInfo",
                "uid", id, "nickname", nickname, "sex", sex, "birthday", birthday, "commid", commid, "signature", signature, "headimg", headimg) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
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
    public static HttpRequest<?> createCommunity(String title,String province,String city,String area,String address) {
        String id = HabitatApp.getInstance().geUser().getId();
        return new SimpleHttpRequest<Object>("createCommunity",
                "uid", id, "title", title, "province", province, "city", city, "area", area, "address", address) {
            @Override
            public Object parser(String jsonString) throws JSONException {
                return null;
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
