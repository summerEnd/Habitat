package com.sumauto.habitat.activity.search;


import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.common.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseFragment;
import com.sumauto.habitat.adapter.SearchFragmentAdapter;
import com.sumauto.widget.recycler.ItemPaddingDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_search_list = (RecyclerView) view.findViewById(R.id.rv_search_list);
        rv_search_list.setAdapter(new SearchFragmentAdapter());
        rv_search_list.addItemDecoration(new ItemPaddingDecoration(0, 0, 0, (int) DisplayUtil.dp(10, getResources())));

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
    }
}
