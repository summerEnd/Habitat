package com.sumauto.habitat.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.fragment.BaseFragment;
import com.sumauto.habitat.adapter.UserPictureAdapter;
import com.sumauto.habitat.http.Requests;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

public class UserPictureListFragment extends BaseFragment{
    RecyclerView recyclerView;
    private String uid;
    public static UserPictureListFragment newInstance(String uid) {

        Bundle args = new Bundle();
        args.putString("uid", uid);
        UserPictureListFragment fragment = new UserPictureListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            uid=getArguments().getString("uid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new UserPictureAdapter(getActivity(),uid));
        recyclerView.addItemDecoration(new ItemPaddingDecoration(1,1,1,1));
    }
}
