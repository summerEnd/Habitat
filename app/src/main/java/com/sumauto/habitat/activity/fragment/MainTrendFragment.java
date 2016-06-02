package com.sumauto.habitat.activity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.AboutMeListFragment;
import com.sumauto.habitat.activity.fragment.ListFragment;
import com.sumauto.habitat.activity.fragment.MyAttentionListFragment;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.utils.Navigator;


public class MainTrendFragment extends ListFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView iv_avatar;
    ListFragment[] fragments = new ListFragment[]{new MyAttentionListFragment(), new AboutMeListFragment()};

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
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
        showAvatar();
    }

    void showAvatar(){
        if (iv_avatar==null){
            Log.e(TAG,"view not create yet");
            return;
        }
        String url=getUserData(HabitatApp.ACCOUNT_AVATAR);
        ImageLoader.getInstance().displayImage(url,iv_avatar, ImageOptions.options());
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uid=getUserData(HabitatApp.ACCOUNT_UID);
                    String url=getUserData(HabitatApp.ACCOUNT_AVATAR);
                    if (!TextUtils.isEmpty(uid)){
                        Navigator.viewUserHome(getContext(),uid,url);
                    }

                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
       showAvatar();
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
