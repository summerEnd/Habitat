package com.sumauto.widget.recycler.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.sp.lib.R;
import com.sumauto.util.SLog;

import java.util.List;


/**
 * Created by Lincoln on 2015/10/28.
 * 加载更多adapter
 */
public abstract class LoadMoreAdapter extends ListAdapter implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "LoadMoreAdapter";
    private final int TYPE_LOAD_MORE = 10002;
    private boolean mHasMoreData = true;//是否有更多数据
    private int mCurrentPage = -1;
    private OnDataSetChangeListener mOnDataSetChangeListener;
    @Nullable
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    LoadingTask mLoadingTask;
    /**
     * 页面最大加载量
     */
    private int maxPageCount = Integer.MAX_VALUE;


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
     *
     * @param page 从0开始
     */
    public abstract List onLoadData(int page);

    public LoadMoreAdapter setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
        return this;
    }

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

    public LoadMoreAdapter(Context context, List data) {
        super(context, data);
        mLoadingTask = new LoadingTask();
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
        if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder mLoadMoreHolder = (LoadMoreHolder) holder;
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
        if (mLoadingTask.isLoading()) {
            log("is loading and return");
            return;
        }
        mLoadingTask = new LoadingTask();
        AsyncTaskCompat.executeParallel(mLoadingTask, 0, LoadingTask.REFRESH);
    }

    public boolean isLoading() {
        return mLoadingTask.isLoading();
    }

    public void cancelLoading() {
        log( "call cancelLoading");
        mLoadingTask.cancel();
    }

    /**
     * 加载更多
     */
    void doLoadMore() {
        if (mLoadingTask.isLoading()) {
            log( "is loading and return");
            return;
        }
        mLoadingTask = new LoadingTask();
        AsyncTaskCompat.executeParallel(mLoadingTask, getCurrentPage() + 1, LoadingTask.LOAD_MORE);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

        ViewParent parent = recyclerView.getParent();
        if (parent instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) parent;
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            final SpanSizeLookup sizeLookup = manager.getSpanSizeLookup();
            manager.setSpanSizeLookup(new SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == getItemCount() - 1) {
                        //last item
                        return manager.getSpanCount();
                    }
                    return sizeLookup.getSpanSize(position);
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(null);
            swipeRefreshLayout = null;
        }
    }

    public void setOnDataSetChangeListener(OnDataSetChangeListener l) {
        this.mOnDataSetChangeListener = l;
    }

    /**
     * 加载更多
     */
    private class LoadMoreHolder extends BaseHolder {
        View progress;
        TextView loadMoreText;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.loadMoreProgress);
            loadMoreText = (TextView) itemView.findViewById(R.id.loadMoreText);
        }

        private void dispatchLoading() {
            log("dispatchLoading");
            progress.setVisibility(View.VISIBLE);
            loadMoreText.setText(R.string.loading);
            doLoadMore();
        }

        private void dispatchNoData() {
            log("dispatchNoData");
            progress.setVisibility(View.GONE);
            loadMoreText.setText(getDataList().size() != 0 ? R.string.noMore : R.string.noData_refresh);
        }
    }

    public interface OnDataSetChangeListener {
        void onLoadPageStart();

        void onLoadPageEnd();
    }


    private class LoadingTask extends AsyncTask<Integer, Void, List> {
        public static final int REFRESH = 0;
        public static final int LOAD_MORE = 1;
        private boolean isRefresh;
        private boolean isLoading = false;
        private boolean mCanceled = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mRecyclerView.isComputingLayout())
                mRecyclerView.setLayoutFrozen(true);//刷新后就不准滑动
            if (mOnDataSetChangeListener != null) {
                mOnDataSetChangeListener.onLoadPageStart();
            }
        }

        @Override
        protected List doInBackground(Integer... params) {
            log( "onRefreshing");
            isLoading = true;
            isRefresh = (params[1] == REFRESH);
            List list = onLoadData(params[0]);
            if (isCancelled() || mCanceled) {
                log("is cancelled");
                list = null;
            }
            log( "refreshDone");
            return list;
        }

        public void cancel() {
            mCanceled = true;
            isLoading = false;
            cancel(true);
        }

        public boolean isLoading() {
            return isLoading;
        }

        @Override
        protected void onCancelled(List list) {
            super.onCancelled(list);
            if (mOnDataSetChangeListener != null) {
                mOnDataSetChangeListener.onLoadPageEnd();
            }
            log( "task is cancelled");
        }


        @Override
        protected void onPostExecute(List list) {
            boolean hasMoreData;
            if (!isRefresh) {
                if (list != null && list.size() > 0) {
                    mCurrentPage++;
                    List dataList = getDataList();
                    int startPosition = dataList.size();
                    dataList.addAll(list);
                    hasMoreData = mCurrentPage < maxPageCount - 1;
                    setHasMoreData(true);
                    if (startPosition == 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyItemRangeInserted(startPosition, dataList.size() - startPosition);
                    }

                } else {
                    hasMoreData = false;
                }
                setHasMoreData(hasMoreData);
            } else {
                getDataList().clear();

                if (list != null) {
                    getDataList().addAll(list);
                    setHasMoreData(true);
                    log("newData count " + list.size());
                } else {
                    log("newData=null");
                }

                mCurrentPage = 0;
                notifyDataSetChanged();
            }
            if (swipeRefreshLayout != null) {
                mRecyclerView.setLayoutFrozen(false);
                swipeRefreshLayout.setRefreshing(false);
            }

            isLoading = false;
            if (mOnDataSetChangeListener != null) {
                mOnDataSetChangeListener.onLoadPageEnd();
            }
        }
    }

    void log(String logs) {
        SLog.d(TAG, this + "" + logs);
    }
}
