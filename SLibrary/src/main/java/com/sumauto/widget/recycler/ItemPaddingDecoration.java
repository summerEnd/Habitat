package com.sumauto.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Lincoln on 16/3/22.
 * item间隔
 */
public class ItemPaddingDecoration extends RecyclerView.ItemDecoration{
    int left,top,right,bottom;

    public ItemPaddingDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left,top,right,bottom);
    }
}
