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
import com.sumauto.habitat.callback.EmptyDecoration;
import com.sumauto.widget.recycler.DividerDecoration;


public class AboutMeListFragment extends ListFragment {


    private RecyclerView recyclerView;
    private AboutMeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);


        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AboutMeAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new EmptyDecoration(){
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
