package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.NewsFeedHolder;
import com.sumauto.habitat.bean.FeedBean;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedHolder> {

    private ArrayList<FeedBean> beans=new ArrayList<>();
    public NewsFeedAdapter() {
        for (int i = 0; i < 20; i++) {
            beans.add(new FeedBean());
        }
    }

    @Override
    public NewsFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed, parent, false);
        return new NewsFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsFeedHolder holder, int position) {
        FeedBean feedBean = beans.get(position);
        holder.init(feedBean);

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


}
