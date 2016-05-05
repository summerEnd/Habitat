package com.sumauto.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.R;

import java.util.ArrayList;

/**
 * Created by Lincoln on 16/3/22.
 * 超级布局
 */
public class SupperLayout extends ViewGroup {
    public static final int AUTO_ROW_COUNT = -1;

    private int columnCount;
    private int maxY;
    private int space;
    private double unitSize;
    private boolean autoRow;
    ArrayList<View> unMeasureChildren=new ArrayList<View>();
    public SupperLayout(Context context) {
        this(context, null);
    }

    public SupperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SupperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SupperLayout);
        space = a.getDimensionPixelSize(R.styleable.SupperLayout_space, 0);
        columnCount = a.getInt(R.styleable.SupperLayout_layout_width_units, 1);
        maxY = a.getInt(R.styleable.SupperLayout_layout_height_units, AUTO_ROW_COUNT);
        a.recycle();

        autoRow = (maxY == AUTO_ROW_COUNT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (widthMode != MeasureSpec.EXACTLY) {
            width = getResources().getDisplayMetrics().widthPixels;
        }

        int contentWidth = width - paddingLeft - paddingRight;
        int spaceWidth = space * (columnCount - 1);

        //unit height
        unitSize = (contentWidth - spaceWidth) / (double) columnCount;
        unMeasureChildren.clear();
        int maxY = 0;
        for (int i = 0, N = getChildCount(); i < N; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.height == LayoutParams.MATCH_PARENT&&this.maxY<=0) {
                //最大高度还没有计算出来，所以先不计算child的高度
                unMeasureChildren.add(child);
                continue;
            }

            measureChild(child);
            maxY = Math.max(layoutParams.y + layoutParams.heightUnits, maxY);
        }

        if (autoRow) {
            this.maxY = maxY;
        }

        //计算child 为match_parent的高度
        for (int i = 0, N = unMeasureChildren.size(); i < N; i++) {
            measureChild(unMeasureChildren.get(i));
        }
        unMeasureChildren.clear();
        int height = (int) (this.maxY * unitSize + space * (this.maxY - 1) + getPaddingTop() + getPaddingBottom());

        setMeasuredDimension(width, height);
    }

    private void measureChild(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.width == LayoutParams.MATCH_PARENT) {
            lp.widthUnits = columnCount;
        }

        if (lp.height == LayoutParams.MATCH_PARENT) {
            lp.heightUnits = maxY;
        }
        int w = (int) (lp.widthUnits * unitSize + (lp.widthUnits - 1) * space-lp.leftMargin-lp.rightMargin);
        int h = (int) (lp.heightUnits * unitSize + (lp.heightUnits - 1) * space-lp.topMargin-lp.bottomMargin);
        child.measure(
                MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
        );
        lp.width = w;
        lp.height = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0, N = getChildCount(); i < N; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int x = lp.x;
            int y = lp.y;

            int l = (int) (getPaddingLeft() + x * (unitSize + space))+lp.leftMargin;
            int t = (int) (getPaddingTop() + y * (unitSize + space))+lp.topMargin;
            child.layout(
                    l,
                    t,
                    l + lp.width,
                    t + lp.height
            );

        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        int x;
        int y;
        int heightUnits;
        int widthUnits;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.SupperLayout_Layout);

            y = a.getInt(R.styleable.SupperLayout_Layout_y, 0);
            heightUnits = a.getInt(R.styleable.SupperLayout_Layout_heightUnits, 1);
            x = a.getInt(R.styleable.SupperLayout_Layout_x, 0);
            widthUnits = a.getInt(R.styleable.SupperLayout_Layout_widthUnits, 1);

            a.recycle();
        }

        public LayoutParams() {
            this(WRAP_CONTENT, WRAP_CONTENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            position(0, 0);
            units(1, 1);
        }



        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public LayoutParams units(int widthUnits, int heightUnits) {
            this.widthUnits = widthUnits;
            this.heightUnits = heightUnits;
            return this;
        }
    }
}
