package com.sumauto.habitat.activity.trend;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;

public class TrendDetailActivity extends BaseActivity {

    private boolean initialTouch=true;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initToolBar(R.id.toolBar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "评论  300";
                } else {
                    return "被赞 100";
                }
            }

            @Override
            public Fragment getItem(int position) {
                if (position==0){
                    return new CommentListFragment();
                }else{
                    return new LikeListFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.iv_avatar).setOnClickListener(this);
        findViewById(R.id.v_comment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.v_comment:{
                new CommentWindow(this).show();
                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (initialTouch){
            //第一次滑动时如果没有触摸ViewPager不会联动
            if (ev.getAction()==MotionEvent.ACTION_DOWN){
                //所以在这里模拟触摸ViewPager
                viewPager.dispatchTouchEvent(ev);
                ev.setAction(MotionEvent.ACTION_UP);
                viewPager.dispatchTouchEvent(ev);
                initialTouch=false;
                ev.setAction(MotionEvent.ACTION_DOWN);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

}
