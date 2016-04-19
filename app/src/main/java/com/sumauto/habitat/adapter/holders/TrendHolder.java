package com.sumauto.habitat.adapter.holders;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.callback.OnActivityResultCallback;
import com.sumauto.habitat.callback.SimpleUMListener;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.SLog;
import com.sumauto.util.ToastUtil;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.TrendDetailActivity;
import com.sumauto.habitat.bean.FeedBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Lincoln on 16/3/22.
 * 动态item
 */
public class TrendHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_content;
    public final ImageView iv_avatar, iv_image, iv_heart, iv_comment, iv_collect, iv_more;
    private FeedBean feedBean;
    private WeakReference<BaseActivity> mActivity;

    public TrendHolder(ViewGroup parent, BaseActivity activity) {
        super(parent, R.layout.list_item_news_feed);
        mActivity = new WeakReference<>(activity);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        iv_heart = (ImageView) itemView.findViewById(R.id.iv_heart);
        iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
        iv_collect = (ImageView) itemView.findViewById(R.id.iv_collect);
        iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
    }

    public void init(FeedBean bean) {
        this.feedBean = bean;
        ViewUtil.registerOnClickListener(this, itemView, iv_image, iv_heart, iv_comment, iv_collect, iv_more);
        iv_heart.setImageResource(bean.isAttention() ? R.mipmap.ic_heart_checked : R.mipmap.ic_heart);
        iv_collect.setImageResource(bean.isCollection() ? R.mipmap.ic_collect_checked : R.mipmap.ic_collect);
    }

    @Override
    public void onClick(View v) {
        ToastUtil.toast(v.getContentDescription());
        if (v == itemView) {
            startActivity(intentActivity(TrendDetailActivity.class));
            return;
        }
        switch (v.getId()) {
            case R.id.iv_image: {

                break;
            }
            case R.id.iv_heart: {
                if (feedBean.isAttention()) {
                    iv_heart.setImageResource(R.mipmap.ic_heart);
                    feedBean.setIsAttention(false);
                } else {
                    iv_heart.setImageResource(R.mipmap.ic_heart_checked);
                    feedBean.setIsAttention(true);
                }
                break;
            }
            case R.id.iv_comment: {
                break;
            }
            case R.id.iv_collect: {
                if (feedBean.isCollection()) {
                    iv_collect.setImageResource(R.mipmap.ic_collect);
                    feedBean.setIsCollection(false);
                } else {
                    iv_collect.setImageResource(R.mipmap.ic_collect_checked);
                    feedBean.setIsCollection(true);
                }
                break;
            }
            case R.id.iv_more: {

                new IosListDialog(v.getContext()).listener(new IosListDialog.Listener() {
                    @Override
                    public void onClick(IosListDialog dialog, int position) {

                        if (position == 1) {
                            startShare();
                        }


                    }
                }).show("分享给好友", "分享到其他平台", "举报");
                break;
            }

        }
    }

    private void startShare(){
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.DOUBAN
                };
        BaseActivity activity = mActivity.get();
        if (activity!=null){
            new ShareAction(activity).setDisplayList( displaylist )
                    .withText( "呵呵" )
                    .withTitle("title")
                    .withTargetUrl("http://www.baidu.com")
                    //.withMedia( image )
                    .setListenerList(new UMShareListener() {
                        @Override
                        public void onResult(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {

                        }
                    })
                    .open();
        }

    }


}
