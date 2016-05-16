package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;


public class TrendListFragment extends ListFragment {

    protected RecyclerView mRecyclerView;
    protected String comid;
    private TrendAdapter adapter;

    public static TrendListFragment newInstance(String comid) {

        Bundle args = new Bundle();
        args.putString("comid", comid);
        TrendListFragment fragment = new TrendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comid = getArguments().getString("comid");
        }
    }

    @Nullable
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TrendAdapter((BaseActivity) getActivity(), comid);
        adapter.setOnDataSetChangeListener(new LoadMoreAdapter.OnDataSetChangeListener() {
            @Override
            public void onLoadPageStart() {

            }

            @Override
            public void onLoadPageEnd() {
                if (adapter.getDataList().isEmpty()){
                    showEmpty();
                }else{
                    showContent();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        processListBottomMargins(mRecyclerView);
        return view;
    }

    @Override
    public View onCreateEmptyView(LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.empty_image, parent, false);
        ImageView iv_empty = (ImageView) inflate.findViewById(R.id.iv_empty);
        TextView tv_empty = (TextView) inflate.findViewById(R.id.tv_empty);
        View btn_login = inflate.findViewById(R.id.tv_login);
        btn_login.setVisibility(View.INVISIBLE);
        tv_empty.setText("暂时还没有动态");
        iv_empty.setImageResource(R.mipmap.image_empty_attention);

        return inflate;
    }

    @Override
    public void scrollToPosition(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    public interface Callback {
        String getComId();
    }
}
