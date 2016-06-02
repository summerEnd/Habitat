package com.sumauto.habitat.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.MainTrendAdapter;
import com.sumauto.habitat.callback.EmptyDecoration;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.util.SLog;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	附近的人＋社区动态
 * History:		2016/05/29 5.6.6 
 */
public class NearbyAndComFragment extends ListFragment {

    public static final String TAG="NearbyAndComFragment";

    private RecyclerView rv_nearby;
    private RecyclerView rv_community;
    private Type type;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BroadCastManager.LoginStateReceiver receiver = new BroadCastManager.LoginStateReceiver() {
        @Override
        public void onChanged(boolean isLogin) {
            super.onChanged(isLogin);

            if (isLogin) {
                //重新创建adapter
                if (rv_nearby != null) {
                    ((LoadMoreAdapter) rv_nearby.getAdapter()).onRefresh();
                }

                if (rv_community != null) {
                    ((LoadMoreAdapter) rv_community.getAdapter()).onRefresh();
                }

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadCastManager.registerLoginState(getContext(), receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadCastManager.unRegisterReceiver(getContext(), receiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        rv_nearby = (RecyclerView) view.findViewById(R.id.rv_nearby);
        rv_community = (RecyclerView) view.findViewById(R.id.rv_community);

        initRecyclerView(rv_community, 0);
        initRecyclerView(rv_nearby, 1);

        setType(type);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type == Type.Community) {
                    ((LoadMoreAdapter) rv_community.getAdapter()).onRefresh();
                } else {
                    ((LoadMoreAdapter) rv_nearby.getAdapter()).onRefresh();
                }
            }
        });
        return view;
    }

    void initRecyclerView(RecyclerView recyclerView, int adapterType) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new EmptyDecoration() {
            @Override
            protected void initEmptyView(View emptyView) {
                SLog.d(TAG,"initEmptyView "+type);
                SLog.d(TAG,"nearby visible "+(View.VISIBLE==rv_nearby.getVisibility()));
                SLog.d(TAG,"Community visible "+(View.VISIBLE==rv_community.getVisibility()));

                if (type == Type.Community) {
                    if (((LoadMoreAdapter) rv_community.getAdapter()).getDataList().size() != 0) {
                        emptyView.setVisibility(View.INVISIBLE);
                        return;
                    } else if (TextUtils.isEmpty(getUserData(HabitatApp.ACCOUNT_COMMID))) {
                        iv_empty.setImageResource(R.mipmap.image_empty_commit);
                        tv_empty.setText("您还没有加入社区");
                    }
                } else {
                    if (((LoadMoreAdapter) rv_nearby.getAdapter()).getDataList().size() != 0) {
                        emptyView.setVisibility(View.INVISIBLE);
                        return;
                    }
                    tv_empty.setText("暂时还没有动态");
                    iv_empty.setImageResource(R.mipmap.image_empty_article);
                }
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        MainTrendAdapter adapter = new MainTrendAdapter((BaseActivity) getActivity(), adapterType);
        adapter.setOnDataSetChangeListener(new LoadMoreAdapter.OnDataSetChangeListener() {
            @Override
            public void onLoadPageStart() {

            }

            @Override
            public void onLoadPageEnd() {
                SLog.d(TAG,"onLoadPageEnd");
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        invalidateEmpty();

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        processListBottomMargins(recyclerView);

    }

    @Override
    public void scrollToPosition(int position) {
        if (rv_community != null && type == Type.Community) {
            rv_community.scrollToPosition(position);
        }
        if (rv_nearby != null && type == Type.NearBy) {
            rv_nearby.scrollToPosition(position);
        }
    }

    /**
     * 设置这个列表展示的数据类型
     *
     * @param type 0是圈子 其余数字是社区动态
     */
    public void setType(Type type) {
        if (type != this.type) {
            swipeRefreshLayout.setRefreshing(false);
            LoadMoreAdapter showAdapter;
            LoadMoreAdapter hideAdapter;
            if (type == Type.NearBy) {
                showAdapter = (LoadMoreAdapter) rv_nearby.getAdapter();
                hideAdapter = (LoadMoreAdapter) rv_community.getAdapter();
                rv_nearby.setLayoutFrozen(false);
                rv_nearby.setVisibility(View.VISIBLE);
                rv_community.setVisibility(View.INVISIBLE);
            } else {
                showAdapter = (LoadMoreAdapter) rv_community.getAdapter();
                hideAdapter = (LoadMoreAdapter) rv_nearby.getAdapter();
                rv_community.setLayoutFrozen(false);
                rv_community.setVisibility(View.VISIBLE);
                rv_nearby.setVisibility(View.INVISIBLE);

            }
            hideAdapter.cancelLoading();
            showAdapter.getDataList().clear();
            showAdapter.notifyDataSetChanged();
        }
        this.type = type;


    }

    void invalidateEmpty() {
        if (rv_nearby == null) return;
        if (!rv_community.isComputingLayout()) {
            rv_community.invalidateItemDecorations();
            SLog.d(TAG,"rv_community invalidateItemDecorations");
        }
        if (!rv_nearby.isComputingLayout()) {
            rv_nearby.invalidateItemDecorations();
            SLog.d(TAG,"rv_nearby invalidateItemDecorations");

        }

    }

    public enum Type {
        NearBy, Community
    }



}
