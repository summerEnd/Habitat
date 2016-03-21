package com.sumauto.widget.nav;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.sp.lib.R;


@SuppressWarnings("unused")
public class TabLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    /**
     * the height of the indicator
     */
    private int indicatorHeight;
    /**
     * the drawable used as indicator
     */
    private Drawable indicatorDrawable;


    private ViewPager mViewPager;

    /**
     * the tab current selected
     */
    private TabItem selectedTab;
    /**
     * the listener of the tab click
     */
    private final OnTabClick onTabClick = new OnTabClick();
    private OnTabClickListener onTabClickListener;


    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
        indicatorDrawable = a.getDrawable(R.styleable.TabLayout_indicator);
        if (indicatorDrawable == null) {
            indicatorDrawable = new ColorDrawable(a.getColor(R.styleable.TabLayout_indicator, Color.BLUE));
        }
        indicatorHeight = a.getDimensionPixelOffset(R.styleable.TabLayout_indicateHeight, 3);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
    }


    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        indicatorDrawable.draw(canvas);
    }

    public TabLayout setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(this);
        return this;
    }

    /**
     * <p/>
     * set a ViewPager to control.In this method a {@link ViewPager.OnPageChangeListener OnPageChangeListener}
     * will be set for this ViewPager.
     *
     * @param pager the page to control
     */
    public void initWith(ViewPager pager, PagerAdapter adapter, Callback callback) {
        setViewPager(pager);
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence pageTitle = adapter.getPageTitle(i);
            View view = callback.createTab((String) pageTitle);
            TabItem tabItem = new TabItem(getContext());
            if (view != null) {
                tabItem.setCustomView(view);
            } else {
                tabItem.setText(pageTitle);
            }
            addTab(tabItem);
        }
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        params.width = 0;
        ((LayoutParams) params).weight = 1;
        super.addView(child, index, params);
        if (child instanceof TabItem) {
            child.setOnClickListener(onTabClick);
            TabItem tabItem = (TabItem) child;
            if (tabItem.isTabSelected()) {
                this.selectedTab = tabItem;
                check(getTabIndex(tabItem));
            }
        }
    }

    public void addTab(TabItem tab) {
        LayoutParams layoutParams;
        if (tab.getLayoutParams() == null || !(tab.getLayoutParams() instanceof LayoutParams)) {
            layoutParams = generateDefaultLayoutParams();
        } else {
            layoutParams = (LayoutParams) tab.getLayoutParams();
        }
        addView(tab, layoutParams);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        return layoutParams;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int tabCount = getTabCount();
        View childAt = getTabItem(position);
        if (childAt == null) return;
        int drawableWidth = childAt.getWidth();

        int leftOffset = childAt.getLeft();
        if (position < tabCount) {
            View nextTab = getTabItem(position + 1);
            if (nextTab != null) {
                leftOffset += (nextTab.getLeft() - childAt.getLeft()) * positionOffset;
                drawableWidth += (nextTab.getWidth() - childAt.getWidth()) * positionOffset;
            }
        }

        int bottom = childAt.getBottom();

        indicatorDrawable.setBounds(leftOffset, bottom - indicatorHeight, drawableWidth + leftOffset, bottom);
        adjustTabPosition();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

        check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * check the tab at this position
     *
     * @param position the position of the tab
     */
    public void check(int position) {
        TabItem tabItem = getTabItem(position);
        if (tabItem == null) {
            return;
        }
        if (tabItem == this.selectedTab) return;

        if (this.selectedTab != null) {
            this.selectedTab.setTabSelect(false);
        }

        this.selectedTab = tabItem;

        tabItem.setTabSelect(true);

        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        } else {
            indicatorDrawable.setBounds(selectedTab.getLeft(), selectedTab.getBottom() - indicatorHeight, selectedTab.getRight(), selectedTab.getBottom());
            invalidate();
        }
    }

    /**
     * adjust the tab position;
     */
    void adjustTabPosition() {
        if (getParent() instanceof HorizontalScrollView) {
            HorizontalScrollView scrollView = (HorizontalScrollView) getParent();
            Rect indicator = indicatorDrawable.getBounds();

            int scrollX = scrollView.getScrollX();
            int leftOffset = indicator.left - scrollX;
            int rightOffset = indicator.right - scrollX - scrollView.getWidth();
            if (leftOffset < 0) {
                scrollView.scrollBy(leftOffset, 0);
            } else if (rightOffset > 0) {
                scrollView.scrollBy(rightOffset, 0);
            }
        }
    }

    @Nullable
    public TabItem getTabItem(int position) {
        int count = getChildCount();
        int tabIndex = 0;
        TabItem tabItem = null;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof TabItem) {
                if (tabIndex == position) {
                    tabItem = (TabItem) child;
                    break;
                }
                tabIndex++;
            }
        }
        return tabItem;
    }

    public int getTabCount() {
        int count = getChildCount();
        int tabCount = 0;
        for (int i = 0; i < count; i++) {
            if (getChildAt(i) instanceof TabItem) {
                tabCount++;
            }
        }
        return tabCount;
    }

    public int getTabIndex(TabItem tabItem) {
        int count = getChildCount();
        int tabIndex = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof TabItem) {
                if (child == tabItem) return tabIndex;
                tabIndex++;
            }
        }
        return tabIndex;
    }

    public TabLayout setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
        return this;
    }

    public interface Callback {
        View createTab(String title);
    }

    public interface OnTabClickListener {
        /**
         * @return true拦截点击事件
         */
        boolean onClick(TabItem tab);
    }

    private class OnTabClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            //noinspection SuspiciousMethodCalls
            if (v instanceof TabItem) {
                TabItem tabItem = (TabItem) v;
                if (onTabClickListener != null && onTabClickListener.onClick(tabItem)) return;
                check(getTabIndex(tabItem));
            }
        }
    }

}
