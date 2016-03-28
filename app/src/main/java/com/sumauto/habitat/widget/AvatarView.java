package com.sumauto.habitat.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sumauto.common.util.DisplayUtil;
import com.sumauto.habitat.activity.mine.UserHomeActivity;
import com.sumauto.widget.CircleImageView;

/**
 * Created by Lincoln on 16/3/20.
 */
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
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), UserHomeActivity.class));
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2,getHeight() /2,getWidth()/2,mBorderPaint);
    }
}
