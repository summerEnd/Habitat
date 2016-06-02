package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.sumauto.habitat.BuildConfig;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.util.ToastUtil;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareActivity extends BaseActivity {
    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);//友盟分享
    String content;
    String url;
    public static void start(Context context, String content, String url) {
        Intent starter = new Intent(context, ShareActivity.class);
        starter.putExtra("content", content);
        starter.putExtra("url", url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);

        content=getIntent().getStringExtra("content");
        url=getIntent().getStringExtra("url");

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_weixin_pyq).setOnClickListener(this);
        findViewById(R.id.iv_qqFriend).setOnClickListener(this);
        findViewById(R.id.iv_sina).setOnClickListener(this);
        findViewById(R.id.bgView).setOnClickListener(this);
        View viewById = findViewById(R.id.contentView);
        viewById.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_in));
    }

    @Override
    public void onClick(View v) {
        UMImage image = new UMImage(this, R.mipmap.ic_launcher);
        switch (v.getId()) {
            case R.id.iv_weixin: {
                UMWXHandler wxHandler = new UMWXHandler(this,
                        BuildConfig.WX_KEY, BuildConfig.WX_SECRET);
                WeiXinShareContent shareContent=new WeiXinShareContent(image);
                share(wxHandler, shareContent,SHARE_MEDIA.WEIXIN);
                break;
            }
            case R.id.iv_weixin_pyq: {
                UMWXHandler circleHandler = new UMWXHandler(this
                        , BuildConfig.WX_KEY, BuildConfig.WX_SECRET);
                circleHandler.setToCircle(true);
                WeiXinShareContent shareContent=new WeiXinShareContent(image);

                share(circleHandler,shareContent, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            }
            case R.id.iv_qqFriend: {
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                        BuildConfig.QQ_APP_ID, BuildConfig.QQ_APP_Key);
                QQShareContent shareContent=new QQShareContent(image);
                share(qqSsoHandler,shareContent, SHARE_MEDIA.QQ);
                break;
            }
            case R.id.iv_sina: {
                SinaSsoHandler handler = new SinaSsoHandler(){
                    @Override
                    public boolean isClientInstalled() {
                        //微博不用安装也可以分享
                        return true;
                    }
                };
                handler.addToSocialSDK();
                SinaShareContent content = new SinaShareContent();
                share(handler,content,SHARE_MEDIA.SINA);
                break;
            }
            case R.id.bgView:
            case R.id.btn_cancel: {
                finish();
                break;
            }

        }
    }

    void share(UMSsoHandler handler, BaseShareContent content,final SHARE_MEDIA platform) {
        if (handler.isClientInstalled()) {
            handler.addToSocialSDK();

            initShareContent(content);
            mController.getConfig().closeToast();
            mController.setShareMedia(content);
            mController.postShare(this,platform, new SocializeListeners.SnsPostListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                    if (i == StatusCode.ST_CODE_SUCCESSED) {
                        ToastUtil.toast(HabitatApp.getInstance(), "分享成功");
                        finish();
                    } else {
                        String eMsg = "分享失败";
                        if (i == -101) {
                            eMsg += " 没有授权";
                        }
                        toast(eMsg);

                    }
                }
            });
        }
    }


    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void initShareContent(BaseShareContent shareContent) {
        shareContent.setShareContent(content);
        shareContent.setTitle(getString(R.string.app_name));
        shareContent.setShareImage(new UMImage(this, R.mipmap.ic_launcher));
        shareContent.setTargetUrl(url);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, intentData);
        }
    }
}
