package com.sumauto.habitat.activity.trend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseFragment;
import com.sumauto.habitat.adapter.MyAttentionAdapter;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.widget.recycler.DividerDecoration;


public class MyAttentionListFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyAttentionAdapter());
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        return view;
    }


}