package com.sumauto.habitat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.sumauto.util.DisplayUtil;
import com.sumauto.widget.CircleImageView;

public class AvatarView extends CircleImageView {
    private final Paint mBorderPaint = new Paint();
    public AvatarView(Context context) {
        super(context);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mBorderPaint.setColor(Color.parseColor("#dfdfdf"));
        mBorderPaint.setStrokeWidth(DisplayUtil.dp(1, getResources()));
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2,getHeight() /2,getWidth()/2,mBorderPaint);
    }
}
