package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.FeedBean;

import java.util.ArrayList;

public class TrendAdapter extends RecyclerView.Adapter<TrendHolder> {

    private ArrayList<FeedBean> beans=new ArrayList<>();
    public TrendAdapter() {
        for (int i = 0; i < 20; i++) {
            beans.add(new FeedBean());
        }
    }

    @Override
    public TrendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed, parent, false);
        return new TrendHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrendHolder holder, int position) {
        FeedBean feedBean = beans.get(position);
        holder.init(feedBean);

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


}
