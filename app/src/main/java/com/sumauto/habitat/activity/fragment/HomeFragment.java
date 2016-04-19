package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.widget.nav.TabItem;
import com.sumauto.widget.nav.TabLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class HomeFragment extends ListFragment implements ViewPager.OnPageChangeListener{

    TextView tv_shequ, tv_friend;
    ImageView iv_triangle,iv_avatar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FeedSelectWindow mFeedSelectWindow;
    private final TrendListFragment FRAGMENTS[]=new TrendListFragment[]{new TrendListFragment(),new TrendListFragment()};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(position == 0 ? R.string.neighbour_feed : R.string.friends_feed);
            }

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
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnTabClickListener(new TabLayout.OnTabClickListener() {
            @Override
            public boolean onClick(TabItem tab) {
                int index = mTabLayout.getTabIndex(tab);
                if (index == 0) {
                    if (mFeedSelectWindow == null)
                        mFeedSelectWindow = new FeedSelectWindow(tab.getContext());
                    mFeedSelectWindow.showAsDropDown(tab);
                    return true;
                }
                return false;
            }
        });
        tv_shequ = (TextView) view.findViewById(R.id.tv_shequ);
        tv_friend = (TextView) view.findViewById(R.id.tv_friend);
        iv_triangle = (ImageView) view.findViewById(R.id.iv_triangle);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        mViewPager.setCurrentItem(0);
        initData();
    }

    void initData(){
        User user = HabitatApp.getInstance().geUser();
        ImageLoader.getInstance().displayImage(user.getHeadimg(),iv_avatar, ImageOptions.options());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tv_shequ.setTextColor(ViewUtil.getColor(getContext(), R.color.colorPrimary));
            tv_friend.setTextColor(ViewUtil.getColor(getContext(), R.color.textBlack));
            iv_triangle.setImageResource(R.mipmap.ic_triangle_checked);
        } else {
            tv_shequ.setTextColor(ViewUtil.getColor(getContext(), R.color.textBlack));
            tv_friend.setTextColor(ViewUtil.getColor(getContext(), R.color.colorPrimary));
            iv_triangle.setImageResource(R.mipmap.ic_triangle);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class FeedSelectWindow extends PopupWindow implements View.OnClickListener {

        private final TextView tv_window_myCircle,tv_window_shequ;

        public FeedSelectWindow(Context context) {
            super(context);
            View contentView = LayoutInflater.from(context).inflate(R.layout.window_feed_select, null);
            setContentView(contentView);
            setBackgroundDrawable(new ColorDrawable());
            setWidth(MATCH_PARENT);
            setHeight(WRAP_CONTENT);
            setFocusable(true);
            tv_window_myCircle = findViewWithOnClick(contentView,R.id.tv_window_myCircle,this);
            tv_window_shequ = findViewWithOnClick(contentView,R.id.tv_window_shequ,this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.tv_window_myCircle){
                tv_shequ.setText(R.string.my_circle);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(),R.color.textBlack));
            }else{
                tv_shequ.setText(R.string.neighbour_feed);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.textBlack));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));

            }
            mViewPager.setCurrentItem(0);
            dismiss();
        }
    }

    @Override
    public void scrollToPosition(int position) {
        FRAGMENTS[mViewPager.getCurrentItem()].scrollToPosition(position);
    }
}
