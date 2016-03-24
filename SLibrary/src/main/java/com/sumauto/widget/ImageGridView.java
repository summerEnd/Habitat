package com.sumauto.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.sp.lib.R;
import com.sumauto.common.util.DisplayUtil;

/**
 * Created by Lincoln on 16/3/22.
 */
public class ImageGridView extends ViewGroup{
    int columnCount;
    int rowCount;
    int space;
    double itemCellSize;
    private boolean autoRow;
    public static final int AUTO_ROW_COUNT = -1;

    public ImageGridView(Context context) {
        this(context, null);
    }

    public ImageGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageGridView);
        space = a.getDimensionPixelSize(R.styleable.ImageGridView_space, 0);
        columnCount = a.getInt(R.styleable.ImageGridView_totalColumn, 3);
        rowCount = a.getInt(R.styleable.ImageGridView_totalRows, AUTO_ROW_COUNT);
        a.recycle();

        autoRow = (rowCount == AUTO_ROW_COUNT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = 0;
        if (widthMode != MeasureSpec.EXACTLY) {
            width = DisplayUtil.getScreenWidth(getContext());
        }
        //unit height
        itemCellSize = (width - paddingLeft - paddingRight - space * (columnCount - 1)) / (double)columnCount;

        int maxRow = 0;
        for (int i = 0, N = getChildCount(); i < N; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            int columnSpan = layoutParams.columnSpan;
            int rowSpan = layoutParams.rowSpan;
            int w = (int) (columnSpan * itemCellSize + (columnSpan - 1) * space);
            int h = (int) (rowSpan * itemCellSize + (rowSpan - 1) * space);
            child.measure(
                    MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
            );
            layoutParams.width = w;
            layoutParams.height = h;
            maxRow = Math.max(layoutParams.row + rowSpan, maxRow);
        }
        if (autoRow) {
            rowCount = maxRow;
        }
        height = (int) (rowCount * itemCellSize + space * (rowCount - 1) + paddingTop + paddingBottom);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0, N = getChildCount(); i < N; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            int column = layoutParams.column;
            int row = layoutParams.row;
            int l = (int) (getPaddingLeft() + column * (itemCellSize + space));
            int t = (int) (getPaddingTop() + row * (itemCellSize + space));
            child.layout(
                    l,
                    t,
                    l + layoutParams.width,
                    t + layoutParams.height
            );
        }
    }

    Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setColor(Color.RED);
        p.setTextSize(40);
        canvas.drawText("item:" + itemCellSize, 50, 20, p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

        int column;
        int row;
        int columnSpan;
        int rowSpan;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a =
                    c.obtainStyledAttributes(attrs, R.styleable.ImageGridView_Layout);

            row = a.getInt(R.styleable.ImageGridView_Layout_row, 0);
            rowSpan = a.getInt(R.styleable.ImageGridView_Layout_row_span, 1);
            column = a.getInt(R.styleable.ImageGridView_Layout_column, 0);
            columnSpan = a.getInt(R.styleable.ImageGridView_Layout_column_span, 1);

            a.recycle();
        }
        public LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public void specRow(int row, int rowSpan) {
            this.row = row;
            this.rowSpan = rowSpan;
        }

        public void specColumn(int column, int columnSpan) {
            this.column = column;
            this.columnSpan = columnSpan;
        }
    }
}
