package com.sumauto.habitat.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.callback.ListCallback;

/**
 * Created by Lincoln on 2016/3/17.
 * 所有Fragment基类
 */
@SuppressWarnings("unchecked")
public class BaseFragment extends Fragment{

    protected String TAG="";

    private String title;

    public String getTitle() {
        return title;
    }

    public BaseFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG=getClass().getSimpleName();
        super.onCreate(savedInstanceState);
    }

    protected <T extends View> T findViewWithOnClick(View finder,int id,View.OnClickListener l){
        T v = (T) finder.findViewById(id);
        v.setOnClickListener(l);
        return v;
    }

    public String getUserData(String key){
        return HabitatApp.getInstance().getUserData(key);
    }

    public void setUserData(String key,String value){
        HabitatApp.getInstance().setUserData(key,value);
    }

    protected void processListBottomMargins(RecyclerView recyclerView){
        if (getActivity() instanceof ListCallback){
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
                        //最后一个Item
                        outRect.set(0, 0, 0, ((ListCallback) getActivity()).getListBottomSpace());
                    }
                }
            });
        }
    }
}
