package com.sumauto.habitat.activity.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.CommentListAdapter;
import com.sumauto.habitat.http.Requests;
import com.sumauto.widget.recycler.DividerDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends Fragment {
    /**
     * @param tid   帖子id
     * @param count 评论数量
     */
    public static CommentListFragment newInstance(String tid, int count) {

        Bundle args = new Bundle();
        args.putString("tid", tid);
        args.putInt("count", count);
        CommentListFragment fragment = new CommentListFragment();
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.setAdapter(new CommentListAdapter(getActivity(), getArguments().getString("tid")));
    }

}
