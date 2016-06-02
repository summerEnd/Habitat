package com.sumauto.habitat.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.callback.EmptyDecoration;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.utils.Navigator;
import com.sumauto.util.ViewUtil;
import com.sumauto.widget.nav.TabItem;
import com.sumauto.widget.nav.TabLayout;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 首页主Fragment
 */
public class MainFragment extends ListFragment implements ViewPager.OnPageChangeListener {

    TextView tv_shequ, tv_friend;
    ImageView iv_triangle, iv_avatar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FeedSelectWindow mFeedSelectWindow;
    private final ListFragment FRAGMENTS[] =
            new ListFragment[]{new NearbyAndComFragment1(), new MainFriendTrend()};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tv_shequ = (TextView) view.findViewById(R.id.tv_shequ);
        tv_friend = (TextView) view.findViewById(R.id.tv_friend);
        iv_triangle = (ImageView) view.findViewById(R.id.iv_triangle);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);

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

        mTabLayout.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(new MyPageAdapter(getChildFragmentManager()));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager.setCurrentItem(0);
        initData();//主页默认立马刷新数据
    }

    @Override
    public void activityCallRefresh() {
        if (getActivity()==null){
            return;
        }
        initData();
    }

    void initData() {
        try {
            ImageLoader.getInstance().displayImage(HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_AVATAR),
                    iv_avatar, ImageOptions.optionsAvatar());
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String uid = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
                        String avatar = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_AVATAR);
                        Navigator.viewUserHome(v.getContext(),uid,avatar);
                    } catch (NotLoginException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class MyPageAdapter extends FragmentPagerAdapter {
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return FRAGMENTS.length;
        }
    }

    /**
     * 我的圈子和社区动态之间互相切换
     */
    private class FeedSelectWindow extends PopupWindow implements View.OnClickListener {

        private final TextView tv_window_myCircle, tv_window_shequ;

        public FeedSelectWindow(Context context) {
            super(context);
            View contentView = LayoutInflater.from(context).inflate(R.layout.window_feed_select, null);
            setContentView(contentView);
            setBackgroundDrawable(new ColorDrawable());
            setWidth(MATCH_PARENT);
            setHeight(WRAP_CONTENT);
            setFocusable(true);
            tv_window_myCircle = findViewWithOnClick(contentView, R.id.tv_window_myCircle, this);
            tv_window_shequ = findViewWithOnClick(contentView, R.id.tv_window_shequ, this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_window_myCircle) {
                tv_shequ.setText(R.string.near_people);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(), R.color.textBlack));
                ((NearbyAndComFragment1) FRAGMENTS[0]).setType(NearbyAndComFragment1.Type.NearBy);
            } else {
                tv_shequ.setText(R.string.neighbour_feed);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.textBlack));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));
                ((NearbyAndComFragment1) FRAGMENTS[0]).setType(NearbyAndComFragment1.Type.Community);
            }
            mViewPager.setCurrentItem(0);

            dismiss();
        }
    }

    @Override
    public void scrollToPosition(int position) {
        if (mViewPager != null) {
            ListFragment fragment = FRAGMENTS[mViewPager.getCurrentItem()];
            if (fragment != null) {
                fragment.scrollToPosition(position);
            }
        }
    }


    /**
     * 好友动态
     */
    public static class MainFriendTrend extends ListFragment {


        protected RecyclerView mRecyclerView;
        private TrendAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recycler_view, container, false);
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new MyFriendsTrendAdapter((BaseActivity) getActivity());
            adapter.setOnDataSetChangeListener(new LoadMoreAdapter.OnDataSetChangeListener() {
                @Override
                public void onLoadPageStart() {

                }

                @Override
                public void onLoadPageEnd() {

                }
            });
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
            mRecyclerView.addItemDecoration(new EmptyDecoration(){
                @Override
                protected void initEmptyView(View emptyView) {
                    if (adapter.getDataList().size()==0){
                        tv_empty.setText("暂时还没有动态");
                        iv_empty.setImageResource(R.mipmap.image_empty_article);
                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.INVISIBLE);
                    }

                }
            });
            processListBottomMargins(mRecyclerView);
            return view;
        }


        @Override
        public void scrollToPosition(int position) {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(position);
            }
        }


        private class MyFriendsTrendAdapter extends TrendAdapter {

            public MyFriendsTrendAdapter(BaseActivity context) {
                super(context);
            }

            @Override
            public List onLoadData(int page) {
                if (!HabitatApp.getInstance().isLogin()) {
                    return null;
                }
                try {
                    String id = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
                    HttpRequest<List<FeedBean>> request = Requests.getMyFriendActive(id,page);

                    SyncHttpHandler<List<FeedBean>> httpHandler = new SyncHttpHandler<>(request);
                    HttpManager.getInstance().postSync(getContext(), httpHandler);
                    return httpHandler.getResult();
                } catch (NotLoginException e) {
                    return null;
                }
            }
        }

    }


}
