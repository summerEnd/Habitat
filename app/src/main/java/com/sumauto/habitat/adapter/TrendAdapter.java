package com.sumauto.habitat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.ArticleEntity;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrendAdapter extends LoadMoreAdapter {
    private String commid;

    public TrendAdapter(BaseActivity context, String commid) {
        super(context, new ArrayList<ArticleEntity>());
        this.commid = commid;
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TrendHolder(parent, (BaseActivity) getContext());
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        HttpRequest<List<ArticleEntity>> request =  Requests.getCommunity(commid,1,5);

        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<List<ArticleEntity>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<ArticleEntity>> request, List<ArticleEntity> articleEntities) {
                getDataList().clear();
                addPage(articleEntities);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                setRefreshDone();
            }
        });
    }

    @Override
    public void onLoadMore() {

        HttpRequest<List<ArticleEntity>> request =  Requests.getCommunity(commid,getCurrentPage()+1,5);
        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<List<ArticleEntity>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<ArticleEntity>> request, List<ArticleEntity> articleEntities) {
                addPage(articleEntities);
            }
        });

    }


}
