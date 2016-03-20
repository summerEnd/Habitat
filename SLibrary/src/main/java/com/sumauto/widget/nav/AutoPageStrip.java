package com.sumauto.widget.nav;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by user1 on 2015/7/20.
 */
public class AutoPageStrip extends PageStrip {
    private Callback callback;

    public AutoPageStrip(Context context) {
        super(context);
    }

    public AutoPageStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoPageStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Deprecated
    @Override
    public void setViewPager(ViewPager pager) {
        super.setViewPager(pager);


    }

    public void initWith(ViewPager pager, Callback callback) {
        this.callback = callback;
        PagerAdapter adapter = pager.getAdapter();
        if (adapter == null) {
            throw new RuntimeException("set Adapter first");
        }
        super.setViewPager(pager);
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            addTab(callback.createTab((String) adapter.getPageTitle(i)));
        }
    }

    public interface Callback {
        ITab createTab(String title);
    }
}
