package com.sumauto.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.sp.lib.R;

/**
 * Created by Lincoln on 16/3/24.
 * 圆角图片
 */
public class RoundCornerImageView extends CircleImageView{
    RectF rectF=new RectF();
    float x_radius=0;
    float y_radius=0;
    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        x_radius=a.getDimensionPixelSize(R.styleable.RoundCornerImageView_x_radius,5);
        y_radius=a.getDimensionPixelSize(R.styleable.RoundCornerImageView_y_radius,5);
        a.recycle();
    }

    @Override
    protected void drawShape(Canvas canvas, Paint bitmapPaint) {
        rectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF,x_radius,y_radius,bitmapPaint);
    }
}
