package com.sumauto.widget.recycler.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.sp.lib.R;

import java.util.List;


/**
 * Created by Lincoln on 2015/10/28.
 * 加载更多adapter
 */
@SuppressWarnings("unused")
public abstract class LoadMoreAdapter extends ListAdapter implements SwipeRefreshLayout.OnRefreshListener {
    private static final int MSG_REFRESH = 0;
    private static final int MSG_MORE = 1;
    private final int TYPE_LOAD_MORE = 10002;
    private boolean mHasMoreData = true;//是否有更多数据
    private int mCurrentPage = -1;
    @Nullable
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreHolder mLoadMoreHolder;
    private RecyclerView mRecyclerView;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            List newData = (List) msg.obj;
            switch (msg.what) {
                case MSG_MORE: {
                    if (newData != null && newData.size() > 0) {
                        mCurrentPage++;
                        List dataList = getDataList();
                        int startPosition = dataList.size();
                        dataList.addAll(newData);
                        setHasMoreData(true);
                        try {
                            notifyItemRangeInserted(startPosition, dataList.size() - startPosition);
                        } catch (Exception ignored) {

                        }
                    } else {
                        setHasMoreData(false);
                    }
                    break;
                }
                case MSG_REFRESH: {
                    getDataList().clear();
                    getDataList().addAll(newData);
                    mCurrentPage = 0;
                    notifyDataSetChanged();
                    break;
                }
            }
            if (swipeRefreshLayout != null) {
                mRecyclerView.setLayoutFrozen(false);
                swipeRefreshLayout.setRefreshing(false);
            }
            mLoadMoreHolder.isLoading=false;
            return false;
        }
    });

    /**
     * 在这里创建ViewHolder
     */
    public abstract BaseHolder onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 在这里绑定数据
     */
    public abstract void onBindHolder(BaseHolder holder, int position);

    /**
     * 加载数据，这里是一个新的线程
     */
    public abstract List onLoadData(int page);


    public final void setHasMoreData(boolean hasMoreData) {
        this.mHasMoreData = hasMoreData;
        try {
            notifyItemChanged(getItemCount() - 1);
        } catch (Exception ignore) {
        }
    }

    public int getCount() {
        return getDataList().size();
    }

    public int getViewType(int position) {
        return 0;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    public LoadMoreAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public final BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOAD_MORE: {
                mLoadMoreHolder = new LoadMoreHolder(getInflater().inflate(R.layout.item_recommend_bottom, parent, false));
                return mLoadMoreHolder;
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
        if (holder == mLoadMoreHolder) {
            if (mHasMoreData) {
                mLoadMoreHolder.dispatchLoading();
            } else {
                mLoadMoreHolder.dispatchNoData();
            }
        } else {
            onBindHolder(holder, position);
        }
    }


    @Override
    public final void onRefresh() {
        mRecyclerView.setLayoutFrozen(true);//刷新后就不准滑动
        new Thread(new Runnable() {
            @Override
            public void run() {
                List list = onLoadData(0);
                mHandler.sendMessage(mHandler.obtainMessage(MSG_REFRESH, list));
            }
        }).start();
    }

    /**
     * 加载更多
     */
    void doLoadMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List list = onLoadData(getCurrentPage() + 1);
                mHandler.sendMessage(mHandler.obtainMessage(MSG_MORE, list));
            }
        }).start();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        ViewParent parent = recyclerView.getParent();
        if (parent instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) parent;
            swipeRefreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(null);
        }
    }

    /**
     * 加载更多
     */
    private class LoadMoreHolder extends BaseHolder {
        View progress;
        TextView loadMoreText;
        boolean isLoading = false;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.loadMoreProgress);
            loadMoreText = (TextView) itemView.findViewById(R.id.loadMoreText);
        }

        private void dispatchLoading() {
            if (isLoading) {
                return;
            }
            progress.setVisibility(View.VISIBLE);
            loadMoreText.setText(R.string.loading);
            isLoading = true;
            doLoadMore();
        }

        private void dispatchNoData() {
            progress.setVisibility(View.GONE);
            loadMoreText.setText(getDataList().size() != 0 ? R.string.noMore : R.string.noData_refresh);
            isLoading = false;
        }
    }
}
