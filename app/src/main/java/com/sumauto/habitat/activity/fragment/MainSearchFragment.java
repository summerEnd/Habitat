package com.sumauto.habitat.activity.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.activity.AddressBookActivity;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.SearchActivity;
import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.SearchFragmentAdapter;
import com.sumauto.widget.recycler.DividerDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainSearchFragment extends ListFragment {

    private RecyclerView rv_search_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_search_list = (RecyclerView) view.findViewById(R.id.rv_search_list);
        rv_search_list.setAdapter(new SearchFragmentAdapter((BaseActivity) getActivity()));
        rv_search_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) < state.getItemCount() - 1) {
                    outRect.set(0, 0, 0, (int) DisplayUtil.dp(10, getResources()));
                }
            }
        });
        rv_search_list.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));

        view.findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        view.findViewById(R.id.iv_addUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressBookActivity.class));

            }
        });
        processListBottomMargins(rv_search_list);
    }

    @Override
    public void scrollToPosition(int position) {
        if (rv_search_list != null) {
            rv_search_list.scrollToPosition(position);
        }
    }
}
