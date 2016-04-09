package com.sumauto.widget.recycler.adapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.R;

import java.util.List;


/**
 * Created by Lincoln on 2015/10/28.
 * 加载更多adapter
 */
@SuppressWarnings("unused")
public abstract class LoadMoreAdapter extends ListAdapter implements SwipeRefreshLayout.OnRefreshListener {
    private final int     TYPE_LOAD_MORE = 10002;
    //是否有更多数据
    private       boolean hasMoreData    = true;
    private       int     currentPage    = 0;
    private SwipeRefreshLayout swipeRefreshLayout;

    public LoadMoreAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public final BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOAD_MORE: {
                return new LoadMoreHolder(getInflater().inflate(R.layout.item_recommend_bottom, parent, false));
            }
        }
        return onCreateHolder(parent, viewType);
    }

    @Override
    public final int getItemCount() {
        return getCount() + 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_LOAD_MORE;
        }
        return getViewType(position);
    }

    @Override
    public final void onBindViewHolder(BaseHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_LOAD_MORE: {
                LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
                if (hasMoreData) {
                    loadMoreHolder.onLoading();
                    onLoadMore();
                }
                else {
                    loadMoreHolder.progress.setVisibility(View.GONE);
                    loadMoreHolder.loadMoreText.setText(getDataList().size() != 0 ? R.string.noMore : R.string.noData_refresh);
                }
                break;
            }
            default:
                onBindHolder(holder, position);
        }
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        try {
            notifyItemChanged(getItemCount() - 1);
        } catch (Exception ignore) {

        }
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public int getCount() {
        return getDataList().size();
    }

    public int getViewType(int position) {
        return 0;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void refreshData(List newData){
        getDataList().clear();
        getDataList().addAll(newData);
        setCurrentPage(1);
        notifyDataSetChanged();
    }

    public void addPage(List newData) {

        if (newData != null && newData.size() > 0) {
            currentPage++;
            List dataList = getDataList();
            int startPosition= dataList.size();
            dataList.addAll(newData);
            setHasMoreData(true);
            try {
                notifyItemRangeInserted(startPosition,dataList.size());
            } catch (Exception ignored) {

            }
        }
        else {
            setHasMoreData(false);
        }
    }

    public abstract BaseHolder onCreateHolder(ViewGroup parent, int viewType);

    public abstract void onBindHolder(BaseHolder holder, int position);

    public abstract void onLoadMore();

    @Override
    public void onRefresh() {

        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            recyclerView.setLayoutFrozen(true);
        }
    }

    public void setRefreshDone() {
        if (swipeRefreshLayout != null) {
            RecyclerView recyclerView = getRecyclerView();
            if (recyclerView != null) {
                recyclerView.setLayoutFrozen(false);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public RecyclerView getRecyclerView() {
        if (swipeRefreshLayout != null) {
            int childCount = swipeRefreshLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = swipeRefreshLayout.getChildAt(i);
                if (child instanceof RecyclerView) {
                    return (RecyclerView) child;
                }
            }
        }
        return null;
    }

    private class LoadMoreHolder extends BaseHolder {
        View     progress;
        TextView loadMoreText;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.loadMoreProgress);
            loadMoreText = (TextView) itemView.findViewById(R.id.loadMoreText);
        }

        protected void onLoading(){
           progress.setVisibility(View.VISIBLE);
           loadMoreText.setText(R.string.loading);
        }

        protected void onNoData(){
            progress.setVisibility(View.GONE);
            loadMoreText.setText(getDataList().size() != 0 ? R.string.noMore : R.string.noData_refresh);
        }
    }
}
