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
import com.sumauto.widget.recycler.DividerDecoration;


public class TrendListFragment extends ListFragment {

    private RecyclerView mRecyclerView;
    private String comid;

    public static TrendListFragment newInstance(String comid) {

        Bundle args = new Bundle();
        args.putString("comid", comid);
        TrendListFragment fragment = new TrendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            comid=getArguments().getString("comid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        TrendAdapter adapter = new TrendAdapter((BaseActivity) getActivity(), comid);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        processListBottomMargins(mRecyclerView);
        return view;
    }

    @Override
    public void scrollToPosition(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    public interface Callback {
        String getComId();
    }
}
