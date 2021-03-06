package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.BaseFragment;
import com.sumauto.habitat.activity.fragment.MainFragment;
import com.sumauto.habitat.activity.fragment.MainMineFragment;
import com.sumauto.habitat.activity.fragment.MainSearchFragment;
import com.sumauto.habitat.activity.fragment.MainTrendFragment;
import com.sumauto.habitat.callback.ListCallback;
import com.sumauto.habitat.callback.Scrollable;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.habitat.utils.BroadCastManager.LoginStateReceiver;
import com.sumauto.widget.CheckableLinearLayout;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ListCallback{

    CheckableLinearLayout mCheckedTab;
    final BaseFragment FRAGMENTS[] = new BaseFragment[]{new MainFragment(), new MainSearchFragment(), new MainTrendFragment(), new MainMineFragment()};
    private ViewPager mViewPager;
    private LoginStateReceiver r=new LoginStateReceiver();
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
        findViewById(R.id.bottom_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        BroadCastManager.registerLoginState(this,r);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadCastManager.unRegisterReceiver(this,r);
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
        FRAGMENTS[position].activityCallRefresh();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}