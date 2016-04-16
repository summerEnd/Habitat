package com.sumauto.habitat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.widget.nav.TabItem;


public class TabItemText extends TabItem {

    private TextView mTextView;

    public TabItemText(Context context) {
        super(context);
        init();
    }

    public TabItemText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabItemText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mTextView = new TextView(getContext());
        mTextView.setTextSize(15);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.CENTER;
        mTextView.setLayoutParams(params);
        setCustomView(mTextView);
    }

    @Override
    public void setTabSelect(boolean selected) {
        super.setTabSelect(selected);
        int color = ViewUtil.getColor(getContext(),selected ? R.color.colorPrimary : R.color.textBlack49);
        mTextView.setTextColor(color);
    }
}
