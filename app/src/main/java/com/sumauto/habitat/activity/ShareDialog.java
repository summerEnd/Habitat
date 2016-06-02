package com.sumauto.habitat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * Created by Lincoln on 2015/11/12.
 * 分享弹窗
 */
public class ShareDialog extends BaseActivity implements View.OnClickListener {

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);//友盟分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_weixin_pyq).setOnClickListener(this);
        findViewById(R.id.iv_qqFriend).setOnClickListener(this);
        findViewById(R.id.iv_sina).setOnClickListener(this);

        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels;
        lp.height = dm.heightPixels / 3;
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        mWindow.setWindowAnimations(R.style.dialog_anim_style);
        mWindow.setAttributes(lp);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_weixin: {
                UMWXHandler wxHandler = new UMWXHandler(this,
                        BuildConfig.WX_KEY, BuildConfig.WX_SECRET);
                share(wxHandler, SHARE_MEDIA.WEIXIN);
                break;
            }
            case R.id.iv_weixin_pyq: {
                UMWXHandler circleHandler = new UMWXHandler(this
                        , BuildConfig.WX_KEY, BuildConfig.WX_SECRET);
                circleHandler.setToCircle(true);
                share(circleHandler, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            }
            case R.id.iv_qqFriend: {
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                        BuildConfig.QQ_APP_ID, BuildConfig.QQ_APP_Key);

                share(qqSsoHandler, SHARE_MEDIA.QQ);
                break;
            }
            case R.id.iv_sina: {


                mController.getConfig().setSsoHandler(new SinaSsoHandler());
                SinaShareContent content = new SinaShareContent();
                initShareContent(content);

                mController.setShareMedia(content);
                mController.postShare(this, SHARE_MEDIA.SINA, new SocializeListeners.SnsPostListener() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                    }
                });
                break;
            }
            case R.id.btn_cancel: {
                finish();
                break;
            }

        }
    }

    void share(UMSsoHandler handler, final SHARE_MEDIA platform) {
        if (handler.isClientInstalled()) {
            handler.addToSocialSDK();
            BaseShareContent content = new BaseShareContent() {
                @Override
                public SHARE_MEDIA getTargetPlatform() {
                    return platform;
                }
            };
            initShareContent(content);
            mController.getConfig().closeToast();
            mController.postShare(this, platform, new SocializeListeners.SnsPostListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                    if (i == StatusCode.ST_CODE_SUCCESSED) {
                        ToastUtil.toast(HabitatApp.getInstance(), "分享成功");
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
        shareContent.setShareContent("我已加入水木资本股票互动圈子，邀你来看股票实战直播");
        shareContent.setTitle("水木资本");
        shareContent.setShareImage(new UMImage(this, R.mipmap.ic_launcher));
        shareContent.setTargetUrl("");
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, intentData);
        }
    }
}
