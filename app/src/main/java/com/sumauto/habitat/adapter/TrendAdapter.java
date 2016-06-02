package com.sumauto.habitat.adapter;

import android.view.ViewGroup;

import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrendAdapter extends LoadMoreAdapter {

    public TrendAdapter(BaseActivity context) {
        super(context, new ArrayList<FeedBean>());
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        TrendHolder trendHolder = new TrendHolder(parent, (BaseActivity) getContext());
        trendHolder.setCallback(new TrendHolder.Callback() {
            @Override
            public void onDelete(TrendHolder holder) {
                Object data = holder.getData();
                int i = getDataList().indexOf(data);
                getDataList().remove(data);
                notifyItemRemoved(i);
            }
        });
        return trendHolder;
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        TrendHolder trendHolder = (TrendHolder) holder;
        trendHolder.setData(getDataList().get(position));
    }


    @Override
    public List onLoadData(int page) {
        return null;
    }
}
