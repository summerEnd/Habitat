package com.sumauto.habitat.activity.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseFragment;
import com.sumauto.habitat.adapter.UserAdapter;
import com.sumauto.widget.recycler.DividerDecoration;

/**
 * Created by Lincoln on 16/3/22.
 * 用户列表
 */
public class UserSearchFragment extends BaseFragment {
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new UserAdapter());
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        if (lp!=null&&lp instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams) lp).topMargin= (int) DisplayUtil.dp(3,getResources());
        }
    }
}
