package com.sumauto.habitat.activity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.AboutMeListFragment;
import com.sumauto.habitat.activity.fragment.ListFragment;
import com.sumauto.habitat.activity.fragment.MyAttentionListFragment;


public class TrendFragment extends ListFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    ListFragment[] fragments = new ListFragment[]{new MyAttentionListFragment(), new AboutMeListFragment()};


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(position == 0 ? R.string.my_attentions : R.string.about_me);
            }

            @Override
            public Fragment getItem(int position) {

                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }


    @Override
    public void scrollToPosition(int position) {
        ListFragment fragment = fragments[mViewPager.getCurrentItem()];
        fragment.scrollToPosition(position);
    }
}
