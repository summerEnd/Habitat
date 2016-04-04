package com.sumauto.habitat.activity.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.ListFragment;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.widget.recycler.DividerDecoration;


public class TrendListFragment extends ListFragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipe_layout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TrendAdapter adapter = new TrendAdapter(getActivity(), "2");
        adapter.setSwipeRefreshLayout(swipe_layout);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        processListBottomMargins(recyclerView);

    }

    @Override
    public void scrollToPosition(int position) {
        if (recyclerView!=null){
            recyclerView.scrollToPosition(position);
        }
    }
}
