package com.sumauto.habitat.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sumauto.habitat.activity.fragment.UserTrendFragment;
import com.sumauto.habitat.activity.fragment.UserPictureListFragment;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.utils.Navigator;
import com.sumauto.util.DisplayUtil;
import com.sumauto.util.MathUtil;
import com.sumauto.util.SLog;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;

import jp.wasabeef.blurry.Blurry;

/**
 * 用户主页
 */
public class UserCenterActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {


    @ViewId ImageView iv_mine_bg;
    @ViewId ImageView iv_back_icon;
    @ViewId ImageView iv_logo;
    @ViewId ImageView iv_avatar;
    @ViewId ImageView iv_attention_state;
    @ViewId Toolbar toolBar;
    @ViewId TextView tv_back_text;
    @ViewId TextView tv_check_detail;
    @ViewId TextView tv_nick;
    @ViewId TextView tv_from;
    @ViewId TextView tv_trend_count;
    @ViewId TextView tv_fans_count;
    @ViewId TextView tv_attention_count;
    @ViewId TextView tv_attention_state;
    @ViewId TabLayout tabLayout;
    @ViewId ViewPager viewPager;

    private String uid;
    private String avatar;
    private Fragment fragments[];
    private UserInfoBean infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        uid = getIntent().getData().getQueryParameter("uid");
        avatar = getIntent().getData().getQueryParameter("avatar");

        fragments = new Fragment[]{
                UserTrendFragment.newInstance(uid),
                UserPictureListFragment.newInstance(uid)};

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        appBarLayout.addOnOffsetChangedListener(this);
        appBarLayout.setExpanded(true, true);


        int screenWidth = DisplayUtil.getScreenWidth(this);
        //iv_mine_bg.getLayoutParams().height = screenWidth * 520 / 750;
        getUserInfo();

    }

    void getUserInfo() {
        if (!TextUtils.isEmpty(avatar)) {
            final int size = getResources().getDimensionPixelSize(R.dimen.dimen_76);
            ImageLoader.getInstance().loadImage(avatar,
                    new ImageSize(size, size),
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                            iv_avatar.setImageResource(R.mipmap.default_avatar);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            SLog.d(TAG, "" + bitmap.getWidth() + "*" + bitmap.getHeight());
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                                    size, size, true);
                            iv_avatar.setImageBitmap(scaledBitmap);
                            iv_mine_bg.getViewTreeObserver().addOnPreDrawListener(
                                    new ViewTreeObserver.OnPreDrawListener() {
                                        @Override
                                        public boolean onPreDraw() {
                                            iv_mine_bg.getViewTreeObserver().removeOnPreDrawListener(this);
                                            //Blurry.with(UserCenterActivity.this).radius(25).sampling(2).onto((ViewGroup) iv_mine_bg.getParent());
                                            Blurry.with(UserCenterActivity.this).capture(iv_mine_bg).into(iv_mine_bg);
                                            return true;
                                        }
                                    });
                            iv_mine_bg.setImageBitmap(scaledBitmap);
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }

        HttpRequest<UserInfoBean> request = Requests.getFriendInfo(uid);

        HttpManager.getInstance().post(this, new JsonHttpHandler<UserInfoBean>(request) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(HttpHandler.HttpResponse response, HttpRequest<UserInfoBean> request, final UserInfoBean bean) {
                infoBean=bean;
                tv_fans_count.setText("粉丝 " + MathUtil.getInt(bean.fanscount));
                tv_trend_count.setText("动态 " + MathUtil.getInt(bean.articlecount));
                tv_attention_count.setText("关注 " + MathUtil.getInt(bean.followcount));
                tv_nick.setCompoundDrawables(null, null,
                        ContextCompat.getDrawable(UserCenterActivity.this, bean.isBoy() ? R.mipmap.ic_boy : R.mipmap.ic_girl)
                        , null);
                setAttentionButtonState(bean.isFriend());


                tv_nick.setText(bean.nickname);
                tv_from.setText("来自 " + bean.community + " 小区");
            }
        });
    }

    void setAttentionButtonState(boolean isFriend) {
        if (isFriend) {
            iv_attention_state.setImageResource(R.mipmap.ic_attentioned);
            tv_attention_state.setText("已关注");
        } else {
            iv_attention_state.setImageResource(R.mipmap.ic_add_attention);
            tv_attention_state.setText("关注");
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int toolBarHeight = toolBar.getHeight();
        int appBarHeight = appBarLayout.getHeight();
        int tabLayoutHeight = tabLayout.getHeight();
        Log.d(TAG, String.format("appbar:%d i:%d toolbar:%d tablayout:%d", appBarHeight, i, toolBarHeight, tabLayoutHeight));
        if (appBarHeight + i == toolBarHeight + tabLayoutHeight) {
            //收缩状态
            if (iv_logo.getVisibility() == View.VISIBLE) {
                return;
            }

            tv_back_text.setTextColor(ContextCompat.getColor(this, R.color.textBlack));
            tv_check_detail.setTextColor(ContextCompat.getColor(this, R.color.textBlack));
            iv_back_icon.setImageResource(R.mipmap.ic_back);
            iv_logo.setVisibility(View.VISIBLE);
            toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.titleBarBackground));

        } else {
            //展开状态
            if (iv_logo.getVisibility() == View.INVISIBLE) {
                return;
            }
            iv_back_icon.setImageResource(R.mipmap.ic_back_white);
            tv_back_text.setTextColor(ViewUtil.getColor(this, R.color.white));
            tv_check_detail.setTextColor(ViewUtil.getColor(this, R.color.white));
            iv_logo.setVisibility(View.INVISIBLE);
            toolBar.setBackgroundColor(0);

        }
    }

    public void onSeeDataClick(View view) {
        Navigator.viewUser(this, Navigator.PATH_DATA, uid);
    }

    public void onAttentionClick(final View view) {

        if (infoBean == null) {
            return;
        }

        HttpRequest<String> request;

        final String status = tv_attention_state.getText().toString();
        switch (status) {
            case "关注":
                request = Requests.setFollow(infoBean.id);
                break;
            case "已关注":
                request = Requests.setUnFollow(infoBean.id);
                break;
            default:
                return;
        }

        view.setEnabled(false);
        HttpManager.getInstance().post(UserCenterActivity.this,
                new JsonHttpHandler<String>(request) {
                    @Override
                    public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                        setAttentionButtonState(status.equals("关注"));
                    }

                    @Override
                    public void onFailure(HttpResponse response) {
                        super.onFailure(response);
                        if ("121".equals(response.code)){
                            setAttentionButtonState(true);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        view.setEnabled(true);
                    }
                });
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return getString(R.string.dong_tai);
                }
                case 1: {
                    return getString(R.string.pictures);
                }
            }
            return "";
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
