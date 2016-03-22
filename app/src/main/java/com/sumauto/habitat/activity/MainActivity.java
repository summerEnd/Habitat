package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.home.HomeFragment;
import com.sumauto.habitat.activity.search.SearchFragment;
import com.sumauto.widget.CheckableLinearLayout;


public class MainActivity extends BaseActivity {

    CheckableLinearLayout checkedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tab_main_home).performClick();
    }

    public void onTabClick(View v) {
        int id = v.getId();
        if (id == R.id.tab_main_publish) {
            return;
        }
        if (v == checkedTab) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = String.valueOf(id);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (fragment == null) {
            switch (id) {
                case R.id.tab_main_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.tab_main_search:
                    fragment = new SearchFragment();
                    break;
                case R.id.tab_main_newsFeed:
                    fragment = new NewsFeedFragment();
                    break;
                case R.id.tab_main_mine:
                    fragment = new MineFragment();
                    break;
            }
            if (fragment != null) ft.add(R.id.layout_fragment_container, fragment, tag);
        }
        else {
            ft.show(fragment);
        }

        CheckableLinearLayout tab = (CheckableLinearLayout) v;
        tab.setChecked(true);
        if (checkedTab != null) {
            Fragment oldFragment=fragmentManager.findFragmentByTag(String.valueOf(checkedTab.getId()));
            if (oldFragment!=null){
                ft.hide(oldFragment);
            }
            checkedTab.setChecked(false);
        }
        ft.commit();
        checkedTab = tab;
    }


}