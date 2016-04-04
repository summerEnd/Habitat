package com.sumauto.habitat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/4/2.
 * 发短信按钮
 */
public class SmsButton extends TextView implements View.OnClickListener {
    //最高时间
    private int maxTime;
    //当前时间
    private int time;
    //初始文字
    private CharSequence initialText;
    private Runnable mTicker;
    private boolean mTickerStopped;

    private Handler mHandler;
    private OnClickListener listener;
    public SmsButton(Context context) {
        this(context, null);
    }

    public SmsButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmsButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmsButton);
        maxTime = a.getInt(R.styleable.SmsButton_maxTime, 30);
        a.recycle();
        initialText = getText();
        super.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.listener=l;
    }

    @Override
    public void onClick(View v) {
        mTickerStopped=false;
        setEnabled(false);
        time = maxTime;
        initTickerIfNeed();
        mTicker.run();
        if (listener!=null)listener.onClick(this);
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();
        initTickerIfNeed();
    }

    private void initTickerIfNeed() {
        if (mTicker!=null)return;

        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;
                if (time < 0) {
                    setEnabled(true);
                    setText(initialText);
                    mTickerStopped=true;
                    return;
                } else {
                    setText("重新发送(" + time-- + ")");
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }
}
