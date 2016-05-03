package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.test.mock.MockApplication;
import android.util.Log;
import android.view.View;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.BaseFragment;
import com.sumauto.habitat.activity.fragment.HomeFragment;
import com.sumauto.habitat.activity.fragment.MineFragment;
import com.sumauto.habitat.activity.fragment.SearchFragment;
import com.sumauto.habitat.activity.fragment.TrendFragment;
import com.sumauto.habitat.activity.fragment.TrendListFragment;
import com.sumauto.habitat.callback.ListCallback;
import com.sumauto.habitat.callback.Scrollable;
import com.sumauto.habitat.utils.Constant;
import com.sumauto.widget.CheckableLinearLayout;
import com.umeng.socialize.UMShareAPI;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ListCallback, TrendListFragment.Callback {

    CheckableLinearLayout mCheckedTab;
    final BaseFragment FRAGMENTS[] = new BaseFragment[]{new HomeFragment(), new SearchFragment(), new TrendFragment(), new MineFragment()};
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FRAGMENTS[position];
            }

            @Override
            public int getCount() {
                return FRAGMENTS.length;
            }
        });

        mViewPager.addOnPageChangeListener(this);
        mCheckedTab = (CheckableLinearLayout) findViewById(R.id.tab_main_home);
        mCheckedTab.setChecked(true);
        mViewPager.setOffscreenPageLimit(4);
    }

    public void onTabClick(View v) {

        int id = v.getId();
        if (id == R.id.tab_main_publish) {
            startActivity(new Intent(this, PublishActivity.class));
            return;
        }
        int position = 0;
        switch (id) {

            case R.id.tab_main_home: {
                position = 0;

                break;
            }
            case R.id.tab_main_search: {
                position = 1;

                break;
            }
            case R.id.tab_main_trend: {
                position = 2;

                break;
            }
            case R.id.tab_main_mine: {
                position = 3;

                break;
            }
        }
        if (mCheckedTab == v) {
            BaseFragment fragment = FRAGMENTS[position];
            if (fragment instanceof Scrollable) {
                ((Scrollable) fragment).scrollToPosition(0);//滑动到顶部
            }
        } else {
            mViewPager.setCurrentItem(position);
            showTab((CheckableLinearLayout) v);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        int id;
        if (position == 0) {
            id = R.id.tab_main_home;

        } else if (position == 1) {
            id = R.id.tab_main_search;

        } else if (position == 2) {
            id = R.id.tab_main_trend;

        } else if (position == 3) {
            id = R.id.tab_main_mine;

        } else {
            return;
        }
        showTab((CheckableLinearLayout) findViewById(id));
    }

    private void showTab(CheckableLinearLayout tab) {
        mCheckedTab.setChecked(false);
        tab.setChecked(true);
        mCheckedTab = tab;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getListBottomSpace() {
        View viewById = findViewById(R.id.bottom_ll);
        if (viewById!=null){
            return viewById.getHeight();
        }
        return 0;
    }

    @Override
    public String getComId() {
        return HabitatApp.getInstance().getUserData(HabitatApp.ACCOUNT_COMMID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }
}