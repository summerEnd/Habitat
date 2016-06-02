package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.CommentListFragment;
import com.sumauto.habitat.activity.fragment.LikeListFragment;
import com.sumauto.habitat.bean.FeedDetailBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.databinding.ActivityDetailBinding;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ToastUtil;

public class TrendDetailActivity extends BaseActivity {

    private boolean initialTouch = true;
    private ViewPager viewPager;
    @ViewId ImageView iv_user_avatar;//用户头像
    @ViewId ImageView iv_avatar;//贴主头像
    @ViewId ImageView iv_image;
    @ViewId ImageView iv_like;
    @ViewId ImageView iv_collect;
    @ViewId TextView tv_heat;
    private FeedDetailBean mDetailBean;
    private ActivityDetailBinding binder;
    private CommentWindow commentWindow;

    public static void start(Context context, String tid,boolean showComment) {
        Intent starter = new Intent(context, TrendDetailActivity.class);
        starter.putExtra("tid", tid);
        starter.putExtra("showComment", showComment);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        findViewById(R.id.iv_avatar).setOnClickListener(this);
        findViewById(R.id.v_comment).setOnClickListener(this);
        ImageLoader.getInstance().displayImage(
                getUserData(HabitatApp.ACCOUNT_AVATAR), iv_user_avatar, ImageOptions.options());
        if (getIntent().getBooleanExtra("showComment",false)){
            showComment();
        }
        getDetail();
    }

    public void getDetail() {
        showLoadingDialog();
        HttpRequest<FeedDetailBean> request = Requests.getSubjectDetail(getIntent().getStringExtra("tid"));

        HttpManager.getInstance().post(this, new JsonHttpHandler<FeedDetailBean>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<FeedDetailBean> request, FeedDetailBean bean) {
                dismissLoadingDialog();
                setUpPage(bean);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    void setUpPage(final FeedDetailBean bean) {
        this.mDetailBean = bean;
        tv_heat.setText("热度" + bean.getHits());
        setCollectStatus();
        setLikeStatus();
        binder.setBean(bean);
        findViewById(R.id.coordinatorLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(bean.headimg, iv_avatar, ImageOptions.options());
        ImageLoader.getInstance().displayImage(bean.getCover(), iv_image, ImageOptions.options());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "评论 " + mDetailBean.getCommentcount();
                } else {
                    return "被赞 " + mDetailBean.getNicecount();
                }
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return CommentListFragment.newInstance(mDetailBean.id, bean.getCommentcount());
                } else {
                    return LikeListFragment.newInstance(mDetailBean.id, bean.getCommentcount());
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.v_comment: {
                showComment();
                break;
            }
        }
    }

    private void showComment() {
        if (commentWindow == null) {
            commentWindow = new CommentWindow(this) {
                @Override
                protected void onCommit(String text) {
                    HttpRequest<Object> request = Requests.submitComment(mDetailBean.id, text, "", "");

                    HttpManager.getInstance().post(getContext(), new JsonHttpHandler<Object>(request) {
                        @Override
                        public void onSuccess(HttpResponse response, HttpRequest<Object> request, Object bean) {
                            ToastUtil.toast(TrendDetailActivity.this,response.msg);
                            getDetail();
                            dismiss();

                        }
                    });
                }
            };
        }
        commentWindow.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (initialTouch) {
            //第一次滑动时如果没有触摸ViewPager不会联动
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                //所以在这里模拟触摸ViewPager
                viewPager.dispatchTouchEvent(ev);
                ev.setAction(MotionEvent.ACTION_UP);
                viewPager.dispatchTouchEvent(ev);
                initialTouch = false;
                ev.setAction(MotionEvent.ACTION_DOWN);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public void onCollect(View view) {

        HttpRequest<String> request = mDetailBean.isCollection() ? Requests.uncollectSubject(mDetailBean.id) :
                Requests.collectSubject(mDetailBean.id);
        showLoadingDialog();
        HttpManager.getInstance().post(this, new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                mDetailBean.setIsCollection(!mDetailBean.isCollection());
                ToastUtil.toast(iv_collect.getContext(), response.msg);
                setCollectStatus();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    private void setCollectStatus() {
        iv_collect.setImageResource(mDetailBean.isCollection() ? R.mipmap.ic_collect_checked :
                R.mipmap.ic_collect);
    }

    public void onNiceClick(View view) {
        HttpRequest<String> request = mDetailBean.isNice() ? Requests.unniceSubject(mDetailBean.id) :
                Requests.niceSubject(mDetailBean.id);
        showLoadingDialog();
        HttpManager.getInstance().post(this, new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                mDetailBean.setIsNice(!mDetailBean.isNice());
                ToastUtil.toast(iv_like.getContext(), response.msg);
                setLikeStatus();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    private void setLikeStatus() {
        iv_like.setImageResource(mDetailBean.isNice() ? R.mipmap.ic_heart_checked :
                R.mipmap.ic_heart);
    }
}
