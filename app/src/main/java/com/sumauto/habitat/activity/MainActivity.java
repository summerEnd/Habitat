package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.home.HomeFragment;
import com.sumauto.habitat.activity.mine.MineFragment;
import com.sumauto.habitat.activity.publish.PublishActivity;
import com.sumauto.habitat.activity.search.SearchFragment;
import com.sumauto.habitat.activity.trend.TrendFragment;
import com.sumauto.widget.CheckableLinearLayout;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseListFragment.Callback {

    CheckableLinearLayout checkedTab;
    final Fragment FRAGMENTS[] = new Fragment[]{new HomeFragment(), new SearchFragment(), new TrendFragment(), new MineFragment()};
    final int[] TAB_IDS = new int[]{R.id.tab_main_home, R.id.tab_main_search, R.id.tab_main_trend, R.id.tab_main_mine};
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
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
    }

    public void onTabClick(View v) {

        if (v.getId()==R.id.tab_main_publish){
            startActivity(new Intent(this, PublishActivity.class));
            return;
        }

        for (int i = 0; i < TAB_IDS.length; i++) {
            if (v.getId() == TAB_IDS[i]) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    void showTab(int position) {
        if (checkedTab != null) {
            checkedTab.setChecked(false);
        }
        CheckableLinearLayout tab = (CheckableLinearLayout) findViewById(TAB_IDS[position]);
        tab.setChecked(true);
        checkedTab = tab;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        showTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getBottomSpace() {
        return findViewById(R.id.bottom_ll).getHeight();
    }
}