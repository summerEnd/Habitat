package com.sumauto.habitat.activity.mine;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.home.TrendListFragment;

import jp.wasabeef.blurry.Blurry;

public class UserHomeActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {
    private  ImageView iv_mine_bg, iv_back_icon,iv_logo;
    private Toolbar toolbar;
    private TextView tv_back_text, tv_check_detail;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean isBlack = false;
    private Fragment fragments[]=new Fragment[]{new TrendListFragment(),new UserPictureListFragment()};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTintManager().setStatusBarTintResource(R.drawable.no_color);
        setContentView(R.layout.activity_user_home);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        initToolBar();
        iv_mine_bg = (ImageView) findViewById(R.id.iv_mine_bg);
        iv_back_icon = (ImageView) findViewById(R.id.iv_back_icon);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        tv_back_text = (TextView) findViewById(R.id.tv_back_text);
        tv_check_detail = (TextView) findViewById(R.id.tv_check_detail);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        iv_mine_bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_mine_bg.getViewTreeObserver().removeOnPreDrawListener(this);
                Blurry.with(UserHomeActivity.this).capture(iv_mine_bg).into(iv_mine_bg);
                return true;
            }
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        Log.d(TAG, String.format("i:%d toolbar_bottom:%d tablayout_top:%d",i,toolbar.getBottom(),tabLayout.getTop()));
        if (Math.abs(toolbar.getBottom()-tabLayout.getTop())<10) {
            if (isBlack)return;
            tv_back_text.setTextColor(ViewUtil.getColor(this, R.color.textBlack));
            tv_check_detail.setTextColor(ViewUtil.getColor(this, R.color.textBlack));
            iv_back_icon.setImageResource(R.mipmap.ic_back);
            iv_logo.setVisibility(View.VISIBLE);
            isBlack = true;
        } else {
            if (!isBlack) return;
            iv_back_icon.setImageResource(R.mipmap.ic_back_white);
            tv_back_text.setTextColor(ViewUtil.getColor(this, R.color.white));
            tv_check_detail.setTextColor(ViewUtil.getColor(this, R.color.white));
            iv_logo.setVisibility(View.INVISIBLE);
            isBlack = false;
        }
    }

     class PagerAdapter extends FragmentPagerAdapter{

         public PagerAdapter(FragmentManager fm) {
             super(fm);
         }

         @Override
         public CharSequence getPageTitle(int position) {
             switch (position){
                 case 0:{
                     return getString(R.string.dong_tai);
                 }
                 case 1:{
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
