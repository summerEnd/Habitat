package com.sumauto.habitat.activity.mine;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseFragment;
import com.sumauto.habitat.activity.trend.AboutMeListFragment;
import com.sumauto.habitat.activity.trend.MyAttentionListFragment;


/**
 * A simple {@link Fragment} subclass.
 * 我的
 */
public class MineFragment extends BaseFragment{
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }


}
