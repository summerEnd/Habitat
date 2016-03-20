package com.sumauto.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class FloatHeadRecyclerView extends FrameLayout {

    public static final String TAG = "headView";
    private RecyclerView mRecyclerView;
    private ViewHolder   mHeaderHolder;
    private Callback     callback;

    public FloatHeadRecyclerView(Context context) {
        this(context, null);
    }

    public FloatHeadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatHeadRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                moveHeader();
            }
        });
        addView(mRecyclerView);

    }

    private void moveHeader() {
        if (mHeaderHolder == null) return;
        if (mRecyclerView.getChildCount()==0)return;
        View mHeaderView = mHeaderHolder.itemView;

        View firstChild = mRecyclerView.getChildAt(0);
        View secondChild = mRecyclerView.getChildAt(1);
        int adapterPosition = mRecyclerView.getChildAdapterPosition(firstChild);
        if (callback != null) callback.invalidateHeader(mHeaderHolder, adapterPosition);

        if (secondChild != null) {
            int top = secondChild.getTop();

            int height = mHeaderView.getHeight();
            Log.d(TAG, String.format("height:%d top:%d", height, top));
            if (top < height) mHeaderView.setTranslationY(top - height);
            else mHeaderView.setTranslationY(0);
        }
        else {
            mHeaderView.setTranslationY(0);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setHeaderViewHolder(ViewHolder headerHolder) {
        this.mHeaderHolder = headerHolder;
        if (mHeaderHolder != null) {
            removeView(mHeaderHolder.itemView);
        }
        addView(headerHolder.itemView);
        moveHeader();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void invalidateHeader(ViewHolder holder, int position);
    }

}
