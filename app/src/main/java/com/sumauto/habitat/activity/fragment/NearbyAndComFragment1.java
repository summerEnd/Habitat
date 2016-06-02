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
public class NearbyAndComFragment1 extends ListFragment {

    public static final String TAG = "NearbyAndComFragment";

    private RecyclerView mRecyclerView;
    private Type type = Type.Community;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BroadCastManager.LoginStateReceiver receiver = new BroadCastManager.LoginStateReceiver() {
        @Override
        public void onChanged(boolean isLogin) {
            super.onChanged(isLogin);

            if (isLogin) {
                //重新创建adapter
                if (mRecyclerView != null) {
                    ((LoadMoreAdapter) mRecyclerView.getAdapter()).onRefresh();
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
        View view = inflater.inflate(R.layout.main_fragment1, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        initRecyclerView(mRecyclerView);

        setType(type);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((LoadMoreAdapter) mRecyclerView.getAdapter()).onRefresh();
            }
        });
        return view;
    }

    void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new EmptyDecoration() {
            @Override
            protected void initEmptyView(View emptyView) {
                SLog.d(TAG, "initEmptyView " + type);
                LoadMoreAdapter adapter = (LoadMoreAdapter) mRecyclerView.getAdapter();

                if (adapter != null && adapter.getDataList().size() != 0) {
                    emptyView.setVisibility(View.INVISIBLE);
                    return;
                }

                emptyView.setVisibility(View.VISIBLE);

                if (type == Type.Community) {
                    if (TextUtils.isEmpty(getUserData(HabitatApp.ACCOUNT_COMMID))) {
                        iv_empty.setImageResource(R.mipmap.image_empty_commit);
                        tv_empty.setText("您还没有加入社区");
                    }
                } else {
                    tv_empty.setText("暂时还没有动态");
                    iv_empty.setImageResource(R.mipmap.image_empty_article);
                }
            }
        });


        processListBottomMargins(recyclerView);

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
    public void setType(Type type) {
        if (type != this.type||mRecyclerView.getAdapter()==null) {
            swipeRefreshLayout.setRefreshing(false);
            LoadMoreAdapter showAdapter = (LoadMoreAdapter) mRecyclerView.getAdapter();
            if (showAdapter != null) {
                showAdapter.cancelLoading();
            }
            if (type == Type.NearBy) {
                mRecyclerView.setAdapter(new MainTrendAdapter((BaseActivity) getActivity(), 1));
            } else {
                mRecyclerView.setAdapter(new MainTrendAdapter((BaseActivity) getActivity(), 0));
            }
        }
        this.type=type;

    }

    public enum Type {
        NearBy, Community
    }


}
