package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.activity.fragment.TrendListFragment;
import com.sumauto.habitat.activity.fragment.UserPictureListFragment;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.MathUtil;
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
    @ViewId Toolbar toolBar;
    @ViewId TextView tv_back_text;
    @ViewId TextView tv_check_detail;
    @ViewId TextView tv_nick;
    @ViewId TextView tv_from;
    @ViewId TextView tv_trend_count;
    @ViewId TextView tv_fans_count;
    @ViewId TextView tv_attention_count;
    @ViewId TabLayout tabLayout;
    @ViewId ViewPager viewPager;
    @ViewId CollapsingToolbarLayout collapsing_toolbar;
    private String uid;
    private Fragment fragments[] = new Fragment[]{new TrendListFragment(), new UserPictureListFragment()};

    public static void start(Context context, String uid) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "habitat://user/home?"
                + "uid=" + uid;
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        uid = getIntent().getData().getQueryParameter("uid");

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        iv_mine_bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_mine_bg.getViewTreeObserver().removeOnPreDrawListener(this);
                Blurry.with(UserCenterActivity.this).capture(iv_mine_bg).into(iv_mine_bg);
                return true;
            }
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        appBarLayout.addOnOffsetChangedListener(this);
        appBarLayout.setExpanded(true, true);
        getUserInfo();

    }

    void getUserInfo() {
        HttpRequest<UserInfoBean> request = Requests.getFriendInfo(uid);

        HttpManager.getInstance().post(this, new JsonHttpHandler<UserInfoBean>(request) {
            @Override
            public void onSuccess(HttpHandler.HttpResponse response, HttpRequest<UserInfoBean> request, UserInfoBean bean) {
                ImageLoader.getInstance().displayImage(bean.headimg, iv_avatar, ImageOptions.options());
                ImageLoader.getInstance().displayImage(bean.headimg, iv_mine_bg, ImageOptions.options());

                tv_fans_count.setText("粉丝 " + MathUtil.getInt(bean.fanscount));
                tv_trend_count.setText("动态 " + MathUtil.getInt(bean.articlecount));
                tv_attention_count.setText("关注 " + MathUtil.getInt(bean.followcount));
                tv_nick.setText(bean.nickname);
                tv_from.setText("来自 " + bean.community + " 小区");
            }
        });
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
        UserDetailActivity.start(this, uid);
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
