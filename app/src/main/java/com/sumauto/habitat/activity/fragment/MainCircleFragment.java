package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.habitat.adapter.holders.CircleHolder;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/08 5.6.6 
 */
public class MainCircleFragment extends ListFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCircleAdapter;
    private RecyclerView.Adapter mTrendAdapter;
    private RecyclerView.Adapter mAdapter;
    int type = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userData = getUserData(HabitatApp.ACCOUNT_COMMID);
        mCircleAdapter = new TrendAdapter((BaseActivity) getActivity(), userData);
        mTrendAdapter = new TrendAdapter((BaseActivity) getActivity(), userData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void scrollToPosition(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    public void setType(int type) {
        this.type = type;
        if (type == 0) {
            mAdapter = mCircleAdapter;
        } else {
            mAdapter = mTrendAdapter;
        }
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private class CircleAdapter extends LoadMoreAdapter {
        @Override
        public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new CircleHolder(parent);
        }

        @Override
        public void onBindHolder(BaseHolder holder, int position) {
            holder.setData(getDataList().get(position));
        }

        @Override
        public List onLoadData(int page) {
            HttpRequest<ArrayList<Object>> request = Requests.getSearch("k", "2", page, 5);

            SyncHttpHandler<ArrayList<Object>> httpHandler = new SyncHttpHandler<>(request);
            HttpManager.getInstance().postSync(getContext(), httpHandler);

            return httpHandler.getResult();
        }

        public CircleAdapter(Context context) {
            super(context, new ArrayList());
        }
    }

}
