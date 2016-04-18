package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.AboutMeAdapter;
import com.sumauto.widget.recycler.DividerDecoration;


public class AboutMeListFragment extends ListFragment {


    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);


        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new AboutMeAdapter());
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        processListBottomMargins(recyclerView);
        return view;
    }

    @Override
    public void scrollToPosition(int position) {
        if (recyclerView!=null){
            recyclerView.scrollToPosition(position);
        }
    }
}
