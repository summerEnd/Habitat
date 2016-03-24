package com.sumauto.habitat.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/23.
 * 列表基类
 */
public class BaseListFragment extends BaseFragment {

    public static final String ARG_COLUMN="columns";

    int column_num;
    Callback callback;
    public static BaseListFragment newInstance(int column_num) {

        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN, column_num);

        BaseListFragment fragment = new BaseListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        column_num=getArguments().getInt(ARG_COLUMN);
       if (getActivity() instanceof Callback){
           callback= (Callback) getActivity();
       }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        FragmentActivity activity = getActivity();
        if (column_num>1){
            mRecyclerView.setLayoutManager(new GridLayoutManager(activity,column_num));
        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }

        if (callback!=null){
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
                        //最后一个Item
                        outRect.set(0, 0, 0, callback.getBottomSpace());
                    }
                }

            });
        }

        return view;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public interface Callback{
        /**
         * 获取列表底部的空白
         */
        int getBottomSpace();
    }
}
