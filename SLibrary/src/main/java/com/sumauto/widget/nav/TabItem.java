package com.sumauto.widget.nav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

import com.sp.lib.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by Lincoln on 2016/3/21.
 * 作用:TabLayout的Item
 */
public class TabItem extends FrameLayout {
    View         customView;
    CheckedTextView     mTextView;
    boolean      isTabSelected;

    public TabItem(Context context) {
        super(context);
        init(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        mTextView = new CheckedTextView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabItem);
        ColorStateList colorList = a.getColorStateList(R.styleable.TabItem_itemTextColors);
        if (colorList != null) {
            mTextView.setTextColor(colorList);
        }
        mTextView.getPaint().setTextSize(a.getDimension(R.styleable.TabItem_itemTextSize, 15));
        mTextView.setText(a.getString(R.styleable.TabItem_itemText));
        LayoutParams params = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        params.gravity=Gravity.CENTER;
        mTextView.setLayoutParams(params);
        mTextView.setGravity(Gravity.CENTER);
        isTabSelected = a.getBoolean(R.styleable.TabItem_itemChecked, false);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            addView(mTextView);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() > 1) {//保证只有一个child
            for (int i = 1; i < getChildCount(); i++) {
                removeView(getChildAt(i));
            }
        }
    }

    public void setCustomView(View customView) {
        this.customView = customView;
        if (customView != null) {
            removeAllViews();
            addView(customView);
        }
    }

    public void setText(CharSequence text){
        mTextView.setText(text);
    }

    public void setTabSelect(boolean selected) {
        this.isTabSelected = selected;
        mTextView.setChecked(true);
    }

    public boolean isTabSelected() {
        return isTabSelected;
    }
}
