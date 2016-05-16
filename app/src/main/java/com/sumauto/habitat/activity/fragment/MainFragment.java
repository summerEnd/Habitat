package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.LoginActivity;
import com.sumauto.habitat.adapter.MainFriendTrendAdapter;
import com.sumauto.habitat.adapter.MainTrendAdapter;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.widget.nav.TabItem;
import com.sumauto.widget.nav.TabLayout;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

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
            new ListFragment[]{new MainFragmentCircle(), new MainFriendTrend()};

    @Nullable
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        initData();
    }

    void initData() {
        ImageLoader.getInstance().displayImage(getUserData(HabitatApp.ACCOUNT_AVATAR), iv_avatar, ImageOptions.options());
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
                tv_shequ.setText(R.string.my_circle);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(), R.color.textBlack));
                ((MainFragmentCircle) FRAGMENTS[0]).setType(0);
            } else {
                tv_shequ.setText(R.string.neighbour_feed);
                tv_window_myCircle.setTextColor(ViewUtil.getColor(v.getContext(), R.color.textBlack));
                tv_window_shequ.setTextColor(ViewUtil.getColor(v.getContext(), R.color.colorPrimary));
                ((MainFragmentCircle) FRAGMENTS[0]).setType(1);
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
     * Author: 		朱超
     * Description: 我的圈子＋社区动态
     * History:		2016/05/08 5.6.6
     */
    public static class MainFragmentCircle extends ListFragment {
        private RecyclerView mRecyclerView;
        private MainTrendAdapter mCircleAdapter;
        private MainTrendAdapter mTrendAdapter;
        private LoadMoreAdapter mAdapter;
        int type = 0;
        ImageView iv_empty;
        TextView tv_empty;
        View btn_login;

        private BroadCastManager.LoginStateReceiver receiver = new BroadCastManager.LoginStateReceiver() {
            @Override
            public void onChanged(boolean isLogin) {
                super.onChanged(isLogin);
                initEmptyView();
                if (isLogin) {
                    //重新创建adapter
                    mCircleAdapter.getDataList().clear();
                    mTrendAdapter.getDataList().clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    showContent();
                } else {
                    showEmpty();
                }
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            BroadCastManager.registerLoginState(getContext(), receiver);
            initAdapter();
        }

        private void initAdapter() {

            class AdapterListener implements LoadMoreAdapter.OnDataSetChangeListener {
                MainTrendAdapter adapter;

                public AdapterListener(MainTrendAdapter adapter) {
                    this.adapter = adapter;
                }

                @Override
                public void onLoadPageStart() {

                }

                @Override
                public void onLoadPageEnd() {
                    if (adapter.getDataList().isEmpty()
                            && mAdapter == adapter) {
                        showEmpty();
                    } else {
                        showContent();
                    }
                }
            }

            mCircleAdapter = new MainTrendAdapter((BaseActivity) getActivity());
            mCircleAdapter.setOnDataSetChangeListener(new AdapterListener(mCircleAdapter));
            mTrendAdapter = new MainTrendAdapter((BaseActivity) getActivity());
            mTrendAdapter.setOnDataSetChangeListener(new AdapterListener(mTrendAdapter));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            BroadCastManager.unRegisterReceiver(getContext(), receiver);
        }

        @Nullable
        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recycler_view, container, false);
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
            setType(type);
            processListBottomMargins(mRecyclerView);
            return view;
        }

        @Override
        public View onCreateEmptyView(LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
            View inflate = inflater.inflate(R.layout.empty_image, parent, false);
            iv_empty = (ImageView) inflate.findViewById(R.id.iv_empty);
            tv_empty = (TextView) inflate.findViewById(R.id.tv_empty);
            btn_login = inflate.findViewById(R.id.tv_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), LoginActivity.class));
                }
            });
            initEmptyView();

            return inflate;
        }

        private void initEmptyView() {
            if (btn_login == null) return;
            if (!HabitatApp.getInstance().isLogin()) {
                btn_login.setVisibility(View.VISIBLE);
                iv_empty.setImageResource(R.mipmap.image_empty_need_login);
                tv_empty.setText("您还没有登录，请登录");
            } else {
                btn_login.setVisibility(View.INVISIBLE);
                if (TextUtils.isEmpty(getUserData(HabitatApp.ACCOUNT_COMMID))) {
                    iv_empty.setImageResource(R.mipmap.image_empty_commit);
                    tv_empty.setText("您还没有加入社区");
                } else {
                    tv_empty.setText("暂时还没有动态");
                    iv_empty.setImageResource(R.mipmap.image_empty_article);
                }
            }
        }

        @Override
        public void scrollToPosition(int position) {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(position);
            }
        }

        /**
         * 设置这个列表展示的数据类型
         *
         * @param type 0是圈子 其余数字是社区动态
         */
        public void setType(int type) {
            this.type = type;
            if (type == 0) {
                mAdapter = mCircleAdapter;
            } else {
                mAdapter = mTrendAdapter;
            }
            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mAdapter);
                showContent();
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
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recycler_view, container, false);
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new MainFriendTrendAdapter((BaseActivity) getActivity());
            adapter.setOnDataSetChangeListener(new LoadMoreAdapter.OnDataSetChangeListener() {
                @Override
                public void onLoadPageStart() {

                }

                @Override
                public void onLoadPageEnd() {
                    if (adapter.getDataList().isEmpty()) {
                        showEmpty();
                    } else {
                        showContent();
                    }
                }
            });
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
            processListBottomMargins(mRecyclerView);
            return view;
        }

        @Override
        public View onCreateEmptyView(LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
            View inflate = inflater.inflate(R.layout.empty_image, parent, false);
            ImageView iv_empty = (ImageView) inflate.findViewById(R.id.iv_empty);
            TextView tv_empty = (TextView) inflate.findViewById(R.id.tv_empty);
            View btn_login = inflate.findViewById(R.id.tv_login);
            btn_login.setVisibility(View.INVISIBLE);
            tv_empty.setText("暂时还没有动态");
            iv_empty.setImageResource(R.mipmap.image_empty_attention);

            return inflate;
        }

        @Override
        public void scrollToPosition(int position) {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(position);
            }
        }

    }
}
