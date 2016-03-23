package com.sumauto.widget.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class DividerDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;
    private int color;
    public DividerDecoration(Drawable d) {
        //在这里我们传入作为Divider的Drawable对象
        mDrawable = d;
    }

    public DividerDecoration(int color)
    {
        this.color = color;
        mDrawable=new ColorDrawable(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
           drawLine(c,parent,state,i);
        }
    }

    protected void drawLine(Canvas c, RecyclerView parent, RecyclerView.State state,int position){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final View child = parent.getChildAt(position);
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        //以下计算主要用来确定绘制的位置
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + 1;
        mDrawable.setBounds(left, top, right, bottom);
        mDrawable.draw(c);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 1);
    }
}
