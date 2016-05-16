package com.sumauto.habitat.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.callback.Scrollable;

/**
 * Created by Lincoln on 16/3/25.
 * 主页的列表
 */
public class ListFragment extends BaseFragment implements Scrollable {

    View mContentView;
    View mEmptyView;

    public void scrollToPosition(int position) {
    }

    @Nullable
    @Override
    public final View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = onCreateContentView(inflater, container, savedInstanceState);
        mEmptyView = onCreateEmptyView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment, container, false);
        if (mEmptyView != null) {
            rootView.addView(mEmptyView);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
        rootView.addView(mContentView);

        return rootView;
    }

    public View onCreateContentView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public View onCreateEmptyView(LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public void showEmpty() {
        if (mContentView != null) mContentView.setVisibility(View.INVISIBLE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.VISIBLE);
    }

    public void showContent() {
        if (mContentView != null) mContentView.setVisibility(View.VISIBLE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.INVISIBLE);
    }
}
