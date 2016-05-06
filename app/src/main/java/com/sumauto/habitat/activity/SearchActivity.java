package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.BaseFragment;
import com.sumauto.habitat.activity.fragment.SearchFragment;
import com.sumauto.habitat.activity.fragment.TrendListFragment;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.util.ViewUtil;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements TrendListFragment.Callback {

    @ViewId EditText edit_search;
    @ViewId View iv_clearEdit;
    @ViewId ViewPager viewPager;
    @ViewId TabLayout tabLayout;

    ArrayList<BaseFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        iv_clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search.setText("");
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iv_clearEdit.setVisibility(edit_search.getText().length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.clearFocus();
                    findViewById(R.id.layout_pager).setVisibility(View.VISIBLE);
                    ViewUtil.closeKeyboard(edit_search);
                    doSearch();
                }
                return false;
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //    @Override
    //    public boolean dispatchKeyEvent(KeyEvent event) {
    //        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_UP) {
    //            doSearch();
    //            return true;
    //        }
    //        return super.dispatchKeyEvent(event);
    //    }

    void doSearch() {
        mFragments.clear();
        String keyword = edit_search.getText().toString();
        //mFragments.add(createFragment("全部", keyword, "0"));
        mFragments.add(createFragment(getString(R.string.user), keyword, "1"));
        mFragments.add(createFragment(getString(R.string.circle), keyword, "2"));
        mFragments.add(createFragment(getString(R.string.content), keyword, "3"));
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    SearchFragment createFragment(String title, String keyword, String type) {
        SearchFragment searchFragment = SearchFragment.newInstance(keyword, type);
        searchFragment.setTitle(title);
        return searchFragment;
    }

    @Override
    public String getComId() {
        return getUserData(HabitatApp.ACCOUNT_COMMID);
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
