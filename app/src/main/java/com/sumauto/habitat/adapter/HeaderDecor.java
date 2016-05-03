package com.sumauto.habitat.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/30 5.6.6
 */
public class HeaderDecor extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    private static final String TAG = "HeaderDecor";
    private Map<Integer, ViewHolder> mHeaders = new HashMap<>();
    private RecyclerView mRecyclerView;
    private Callback mCallback;


    private void initRecyclerViewIfNeed(RecyclerView recyclerView) {
        if (mRecyclerView == null) {
            Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                throw new IllegalStateException("call recyclerView.setAdapter() first");
            }

            if (!(adapter instanceof Callback)) {
                throw new IllegalStateException(adapter + " must implements " + Callback.class.getName());
            }
            this.mRecyclerView = recyclerView;
            this.mCallback = (Callback) adapter;

            recyclerView.removeOnChildAttachStateChangeListener(this);
            recyclerView.addOnChildAttachStateChangeListener(this);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        initRecyclerViewIfNeed(parent);

        if (state.getItemCount() > 0) {
            //the 1st child's header position
            int headerPosition = headerPosition(0);
            ViewHolder headerHolder = mHeaders.get(headerPosition);
            if (headerHolder == null) return;//no header for current position

            View headView = headerHolder.itemView;
            View childNext = parent.getChildAt(1);
            int headerHeight = headView.getMeasuredHeight();

            if (childNext != null) {
                int offset = 0;
                //the 2nd child's header positon
                int nextHeaderPosition =headerPosition(1);
                if (nextHeaderPosition != headerPosition
                        && childNext.getTop() < headerHeight) {
                    //different header,and the next header come into the first one
                    offset = childNext.getTop() - headerHeight;
                }

                c.save();
                c.translate(0, offset);
                headView.draw(c);
                c.restore();
            }
        }
    }

    /**
     *
     * @param layoutPosition the layout position
     * @return the header position of the layoutPosition
     */
    int headerPosition(int layoutPosition){
        int adapterPosition = mRecyclerView.getChildAdapterPosition(mRecyclerView.getChildAt(layoutPosition));
        return mCallback.getHeaderForPosition(adapterPosition);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        ViewHolder holder = mRecyclerView.getChildViewHolder(view);

        //if the hold is a header，save it
        if (mCallback.isHeader(holder)) {

            int adapterPosition = holder.getAdapterPosition();
            ViewHolder savedViewHolder = mHeaders.get(adapterPosition);
            if (savedViewHolder == null) {
                mHeaders.put(adapterPosition, holder);
                //the holder should not be recycled
                holder.setIsRecyclable(false);
            } else {

                Log.d(TAG, "already added, create a new one? " + (savedViewHolder == holder));
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {

    }

    public interface Callback {
        /**
         * get the header position of this position
         *
         * @param position item position
         * @return the header position of this position
         */
        int getHeaderForPosition(int position);

        /**
         * @return true if the holder is a header
         */
        boolean isHeader(ViewHolder holder);
    }
}
