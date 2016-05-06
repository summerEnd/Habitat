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

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.SearchAdapter;
import com.sumauto.widget.recycler.DividerDecoration;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/06 5.6.6 
 */
public class SearchFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private String keyword;
    private String type;

    public static SearchFragment newInstance(String keyword,String type) {

        Bundle args = new Bundle();
        args.putString("key", keyword);
        args.putString("type", type);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyword = getArguments().getString("key");
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        SearchAdapter adapter = new SearchAdapter(getActivity(), keyword,type);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        return view;
    }
}
