package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AboutMeLikeHolder;
import com.sumauto.habitat.adapter.holders.AboutMeListHolder;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lincoln on 16/3/25.
 * 关于我
 */
public class AboutMeAdapter extends LoadMoreAdapter{

    ArrayList<Boolean> data=new ArrayList<>();

    public AboutMeAdapter(Context context, List data) {
        super(context, data);
    }

    //    public AboutMeAdapter() {
//        Random random=new Random();
//        for (int i = 0; i < 12; i++) {
//            data.add(random.nextBoolean());
//        }
//    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new AboutMeListHolder(parent);
        }else{
            return new AboutMeLikeHolder(parent);
        }
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {

    }

    @Override
    public List onLoadData(int page) {
        return null;
    }


    @Override
    public int getViewType(int position) {
        return data.get(position)?0:1;
    }
}
