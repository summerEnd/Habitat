package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.adapter.holders.SearchHeaderHolder;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.SearchHeaderBean;

import java.util.ArrayList;

/**
 * Created by Lincoln on 16/3/22.
 * 搜索Fragment
 */
public class SearchFragmentAdapter extends RecyclerView.Adapter {

    private ArrayList<FeedBean> beans=new ArrayList<>();
    private SearchHeaderBean mSearchHeader=new SearchHeaderBean();
    public SearchFragmentAdapter() {
        for (int i = 0; i < 20; i++) {
            beans.add(new FeedBean());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new SearchHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_header,parent,false));
        }else{
            return new TrendHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed,parent,false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TrendHolder){
            ((TrendHolder) holder).init(beans.get(position-1));
        }else if (holder instanceof SearchHeaderHolder){
            ((SearchHeaderHolder) holder).init(mSearchHeader);
        }

    }

    @Override
    public int getItemCount() {
        return beans.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }
}
