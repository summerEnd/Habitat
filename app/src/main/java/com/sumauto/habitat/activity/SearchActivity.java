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

import com.sumauto.habitat.activity.fragment.TrendListFragment;
import com.sumauto.habitat.activity.fragment.UserSearchFragment;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;

public class SearchActivity extends BaseActivity {

    private EditText edit_search;
    private View iv_clear_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));

        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        edit_search = (EditText) findViewById(R.id.edit_search);
        iv_clear_edit = findViewById(R.id.iv_clearEdit);

        iv_clear_edit.setOnClickListener(new View.OnClickListener() {
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
                iv_clear_edit.setVisibility(edit_search.getText().length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.clearFocus();
                    findViewById(R.id.layout_pager).setVisibility(View.VISIBLE);
                    ViewUtil.closeKeyboard(edit_search);
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

    private class Adapter extends FragmentPagerAdapter{

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0){
                return getString(R.string.user);
            }else if (position==1){
                return getString(R.string.circle);
            }else{
                return getString(R.string.content);
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                return new UserSearchFragment();
            }else if (position==1){
                return new TrendListFragment();
            }else{
                return new TrendListFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
