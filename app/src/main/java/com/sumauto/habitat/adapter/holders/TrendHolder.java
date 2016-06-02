package com.sumauto.habitat.adapter.holders;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.LoginActivity;
import com.sumauto.habitat.activity.ShareActivity;
import com.sumauto.habitat.activity.TrendDetailActivity;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.databinding.ListItemNewsFeedBinding;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.utils.Navigator;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.ToastUtil;
import com.sumauto.util.ViewUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Lincoln on 16/3/22.
 * 动态item
 */
public class TrendHolder extends BaseViewHolder implements View.OnClickListener {
    public final ImageView iv_avatar, iv_image, iv_heart, iv_comment, iv_collect, iv_more;
    private Callback mCallback;
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

    @Override
    protected void onInit(Object data) {
        this.feedBean = (FeedBean) data;
        iv_heartWeak = new WeakReference<>(iv_heart);
        iv_collectWeak = new WeakReference<>(iv_collect);

        ViewUtil.registerOnClickListener(this,
                iv_image, iv_heart, iv_comment, iv_collect, iv_more, iv_avatar);
        iv_heart.setImageResource(feedBean.isNice() ? R.mipmap.ic_heart_checked : R.mipmap.ic_heart);
        iv_collect.setImageResource(feedBean.isCollection() ? R.mipmap.ic_collect_checked : R.mipmap.ic_collect);
        binding.setBean(feedBean);
        ImageLoader.getInstance().displayImage(feedBean.headimg, iv_avatar, ImageOptions.optionsAvatar());
        ImageLoader.getInstance().displayImage(feedBean.img, iv_image, ImageOptions.options());
    }


    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();
        final Context context = v.getContext();
        //这里是不需要登录的事件
        switch (id) {
            case R.id.iv_image: {
                TrendDetailActivity.start(v.getContext(), feedBean.id,false);
                break;
            }

            case R.id.iv_avatar: {

                Navigator.viewUserHome(v.getContext(), feedBean.uid, feedBean.headimg);
                break;
            }

            case R.id.iv_comment:{
                TrendDetailActivity.start(context,feedBean.id,true);
                break;
            }

            case R.id.iv_more: {
                String loginId = null;

                try {
                    loginId = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
                } catch (NotLoginException e) {
                    e.printStackTrace();
                }

                final CharSequence items[];
                if (loginId != null && TextUtils.equals(loginId, feedBean.uid)) {

                    SpannableStringBuilder sb = new SpannableStringBuilder("删除");
                    sb.setSpan(new CharacterStyle() {
                        @Override
                        public void updateDrawState(TextPaint tp) {
                            tp.setColor(Color.RED);
                        }
                    }, 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    items = new CharSequence[]{sb, "分享给好友", "分享到其他平台", "举报"};

                } else {
                    items = new CharSequence[]{"分享给好友", "分享到其他平台", "举报"};
                }
                IosListDialog dialog = new IosListDialog(context);
                dialog.listener(new IosListDialog.Listener() {
                    @Override
                    public void onClick(IosListDialog dialog, int position) {

                        CharSequence item = items[position];
                        Context ctx = itemView.getContext();
                        if ("分享给好友".equals(item)) {
                            ShareActivity.start(ctx, feedBean.content, Requests.getShareUrl(feedBean.id));
                        } else if ("分享到其他平台".equals(item)) {
                            ShareActivity.start(ctx, feedBean.content, Requests.getShareUrl(feedBean.id));
                        } else if ("举报".equals(item)) {
                            // TODO: 16/5/25 举报
                        } else if (item instanceof SpannableStringBuilder) {
                            //删除
                            HttpRequest<String> request = Requests.delSubject(feedBean.id);

                            HttpManager.getInstance().post(context, new JsonHttpHandler<String>(request) {
                                @Override
                                public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                                    if (mCallback != null) {
                                        mCallback.onDelete(TrendHolder.this);
                                    }
                                }
                            });
                        }
                    }
                }).show(items);
                break;
            }
        }


        if (!HabitatApp.getInstance().isLogin()) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        String tid = feedBean.id;
        //这里是需要登录的
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
                            ToastUtil.toast(iv_heart.getContext(), response.msg);
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
                            ToastUtil.toast(iv_collect.getContext(), response.msg);

                            iv_collect.setImageResource(feedBean.isCollection() ? R.mipmap.ic_collect_checked :
                                    R.mipmap.ic_collect);
                        }
                    }
                });

                break;
            }

        }
    }

    public interface Callback {
        void onDelete(TrendHolder holder);
    }

}
