package com.sumauto.habitat.adapter;

import android.view.ViewGroup;

import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.activity.fragment.TrendListFragment;
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

public class CollectAdapter extends LoadMoreAdapter {
    private String collectId;

    public CollectAdapter(BaseActivity context, String collectId) {
        super(context, new ArrayList<FeedBean>());
        this.collectId = collectId;
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
        HttpRequest<List<FeedBean>> request = Requests.getCollect(collectId);
        SyncHttpHandler<List<FeedBean>> httpHandler = new SyncHttpHandler<List<FeedBean>>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);
        return httpHandler.getResult();
    }
}
