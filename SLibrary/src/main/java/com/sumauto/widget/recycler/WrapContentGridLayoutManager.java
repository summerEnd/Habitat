package com.sumauto.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lincoln on 16/3/25.
 * 高度自适应的GridLayoutManager
 */
public class WrapContentGridLayoutManager extends GridLayoutManager{

    private int verticalSpacing;

    public WrapContentGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapContentGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public WrapContentGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public WrapContentGridLayoutManager setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
        return this;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //super.onMeasure(recycler, state, widthSpec, heightSpec);
        int itemCount = state.getItemCount();

        int width = View.MeasureSpec.getSize(widthSpec);
        int height= View.MeasureSpec.getSize(heightSpec);
        if (itemCount == 0) {
            width = 0;
            height = 0;
        } else {
            View child;
            try {
                child = recycler.getViewForPosition(0);
            } catch (Exception e) {
                //运行删除动画时，0这个位置偏移量为－1，会抛出异常
                child = recycler.getViewForPosition(1);
            }

            if (child != null) {
                measureChild(child, widthSpec, heightSpec);
                int itemHeight = child.getMeasuredHeight() + verticalSpacing ;
                int count = getItemCount();
                height = count <= getSpanCount()
                        ? itemHeight
                        : itemHeight * 2;

            }
        }
        setMeasuredDimension(width, height);

    }
}
