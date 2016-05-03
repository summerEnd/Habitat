package com.sumauto.habitat.adapter;

import android.view.ViewGroup;

import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.fragment.TrendListFragment;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrendAdapter extends LoadMoreAdapter {
    private TrendListFragment.Callback callback;

    public TrendAdapter(BaseActivity context, TrendListFragment.Callback callback) {
        super(context, new ArrayList<FeedBean>());
        this.callback = callback;
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TrendHolder(parent, (BaseActivity) getContext());
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        TrendHolder trendHolder = (TrendHolder) holder;
        trendHolder.init((FeedBean) getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<List<FeedBean>> request = Requests.getCommunity(callback.getComId(), page, 5);

        JsonHttpHandler<List<FeedBean>> httpHandler = new JsonHttpHandler<List<FeedBean>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<FeedBean>> request, List<FeedBean> articleEntities) {
            }
        };
        HttpManager.getInstance().postSync(getContext(), httpHandler);
        return httpHandler.getResult();
    }
}
