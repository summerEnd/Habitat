package com.sumauto.habitat.activity.fragment;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.LikeGridAdapter;

/**
 * A simple {@link Fragment} subclass.
 * 点赞
 */
public class LikeListFragment extends Fragment {

    public static LikeListFragment newInstance(String tid,int count) {

        Bundle args = new Bundle();
        args.putString("tid", tid);
        args.putInt("count", count);
        LikeListFragment fragment = new LikeListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),6));

        recyclerView.setAdapter(new LikeGridAdapter(getActivity(),getArguments().getString("tid")));
    }
}
