package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.home.HomeFragment;
import com.sumauto.habitat.activity.mine.MineFragment;
import com.sumauto.habitat.activity.publish.PublishActivity;
import com.sumauto.habitat.activity.search.SearchFragment;
import com.sumauto.habitat.activity.trend.TrendFragment;
import com.sumauto.habitat.callback.ListCallback;
import com.sumauto.habitat.callback.Scrollable;
import com.sumauto.widget.CheckableLinearLayout;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ListCallback {

    CheckableLinearLayout checkedTab;
    final BaseFragment FRAGMENTS[] = new BaseFragment[]{new HomeFragment(), new SearchFragment(), new TrendFragment(), new MineFragment()};
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FRAGMENTS[position];
            }

            @Override
            public int getCount() {
                return FRAGMENTS.length;
            }
        });

        viewPager.addOnPageChangeListener(this);
        checkedTab = (CheckableLinearLayout) findViewById(R.id.tab_main_home);
        checkedTab.setChecked(true);
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
        if (checkedTab == v) {
            BaseFragment fragment = FRAGMENTS[position];
            if (fragment instanceof Scrollable) {
                ((Scrollable) fragment).scrollToPosition(0);//滑动到顶部
            }
        } else {
            viewPager.setCurrentItem(position);
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
        checkedTab.setChecked(false);
        tab.setChecked(true);
        checkedTab = tab;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getListBottomSpace() {
        int height = findViewById(R.id.bottom_ll).getHeight();
        Log.d("height", "height=" + height);
        return height;
    }
}