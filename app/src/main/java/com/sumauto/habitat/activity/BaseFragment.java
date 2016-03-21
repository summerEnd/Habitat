package com.sumauto.habitat.activity;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Lincoln on 2016/3/17.
 * 所有Fragment基类
 */
@SuppressWarnings("unchecked")
public class BaseFragment extends Fragment{
    protected <T extends View> T findViewWithOnClick(View finder,int id,View.OnClickListener l){
        T v = (T) finder.findViewById(id);
        v.setOnClickListener(l);
        return v;
    }
}
