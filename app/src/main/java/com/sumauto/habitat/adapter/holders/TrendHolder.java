package com.sumauto.habitat.adapter.holders;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.LoginActivity;
import com.sumauto.habitat.activity.TrendDetailActivity;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.databinding.ListItemNewsFeedBinding;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.ToastUtil;
import com.sumauto.util.ViewUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

/**
 * Created by Lincoln on 16/3/22.
 * 动态item
 */
public class TrendHolder extends BaseViewHolder implements View.OnClickListener {
    public final ImageView iv_avatar, iv_image, iv_heart, iv_comment, iv_collect, iv_more;
    private FeedBean feedBean;
    private WeakReference<BaseActivity> mActivity;
    private WeakReference<ImageView> iv_heartWeak;
    private WeakReference<ImageView> iv_collectWeak;
    ListItemNewsFeedBinding binding;

    public TrendHolder(ViewGroup parent, BaseActivity activity) {
        super(parent, R.layout.list_item_news_feed);
        binding = DataBindingUtil.bind(itemView);
        mActivity = new WeakReference<>(activity);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        iv_heart = (ImageView) itemView.findViewById(R.id.iv_heart);
        iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
        iv_collect = (ImageView) itemView.findViewById(R.id.iv_collect);
        iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
    }

    public void init(FeedBean bean) {
        this.feedBean = bean;
        iv_heartWeak = new WeakReference<>(iv_heart);
        iv_collectWeak = new WeakReference<>(iv_collect);

        ViewUtil.registerOnClickListener(this, itemView, iv_image, iv_heart, iv_comment, iv_collect, iv_more);
        iv_heart.setImageResource(bean.isNice() ? R.mipmap.ic_heart_checked : R.mipmap.ic_heart);
        iv_collect.setImageResource(bean.isCollection() ? R.mipmap.ic_collect_checked : R.mipmap.ic_collect);
        binding.setBean(bean);
        ImageLoader.getInstance().displayImage(bean.headimg, iv_avatar, ImageOptions.options());
        ImageLoader.getInstance().displayImage(bean.img, iv_image, ImageOptions.options());
    }

    @Override
    public void onClick(View v) {

        if (v == itemView) {
            TrendDetailActivity.start(v.getContext(),feedBean.id);
            //startActivity(intentActivity(TrendDetailActivity.class));
            return;
        }

        int id = v.getId();
        Context context = v.getContext();
        switch (id) {
            case R.id.iv_image: {
                TrendDetailActivity.start(v.getContext(),feedBean.id);
                break;
            }

            case R.id.iv_more: {

                new IosListDialog(context).listener(new IosListDialog.Listener() {
                    @Override
                    public void onClick(IosListDialog dialog, int position) {

                        if (position == 1) {
                            startShare();
                        }

                    }
                }).show("分享给好友", "分享到其他平台", "举报");
                break;
            }

            default: {
                //这里是需要登录的
                if (!HabitatApp.getInstance().isLogin()) {
                    startActivity(new Intent(context, LoginActivity.class));
                    return;
                }

                String tid = feedBean.id;
                switch (id) {
                    case R.id.iv_heart: {
                        HttpRequest<String> request = feedBean.isNice() ? Requests.unniceSubject(tid) :
                                Requests.niceSubject(tid);
                        HttpManager.getInstance().post(context, new JsonHttpHandler<String>(request) {
                            @Override
                            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                                feedBean.setIsNice(!feedBean.isNice());
                                ImageView iv_heart = iv_heartWeak.get();
                                if (iv_heart != null) {
                                    ToastUtil.toast(iv_heart.getContext(),response.msg);

                                    iv_heart.setImageResource(feedBean.isNice() ? R.mipmap.ic_heart_checked :
                                            R.mipmap.ic_heart);
                                }
                            }
                        });


                        break;
                    }
                    case R.id.iv_comment: {
                        break;
                    }
                    case R.id.iv_collect: {
                        HttpRequest<String> request = feedBean.isCollection() ? Requests.uncollectSubject(tid) :
                                Requests.collectSubject(tid);

                        HttpManager.getInstance().post(context, new JsonHttpHandler<String>(request) {
                            @Override
                            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                                feedBean.setIsCollection(!feedBean.isCollection());
                                ImageView iv_collect = iv_collectWeak.get();
                                if (iv_collect != null) {
                                    ToastUtil.toast(iv_collect.getContext(),response.msg);

                                    iv_collect.setImageResource(feedBean.isCollection() ? R.mipmap.ic_collect_checked :
                                            R.mipmap.ic_collect);
                                }
                            }
                        });

                        break;
                    }

                }
            }
        }
    }

    private void startShare() {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN
        };
        BaseActivity activity = mActivity.get();
        if (activity != null) {
            new ShareAction(activity).setDisplayList(displaylist)
                    .withText("呵呵")
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
