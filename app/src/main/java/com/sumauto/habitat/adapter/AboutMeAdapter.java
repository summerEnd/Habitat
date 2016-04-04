package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AboutMeLikeHolder;
import com.sumauto.habitat.adapter.holders.AboutMeListHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lincoln on 16/3/25.
 * 关于我
 */
public class AboutMeAdapter extends RecyclerView.Adapter<ViewHolder>{

    ArrayList<Boolean> data=new ArrayList<>();

    public AboutMeAdapter() {
        Random random=new Random();
        for (int i = 0; i < 12; i++) {
            data.add(random.nextBoolean());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new AboutMeListHolder(parent);
        }else{
            return new AboutMeLikeHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position)?0:1;
    }
}
