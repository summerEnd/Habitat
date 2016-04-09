package com.sumauto.habitat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.sumauto.common.util.JsonUtil;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.ArticleEntity;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrendAdapter extends LoadMoreAdapter {
    private String commid;

    public TrendAdapter(Context context, String commid) {
        super(context, new ArrayList<ArticleEntity>());
        this.commid = commid;
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TrendHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        Requests.GetCommunity request = new Requests.GetCommunity(commid,1);
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

        Requests.GetCommunity request = new Requests.GetCommunity(commid,getCurrentPage()+1);
        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<List<ArticleEntity>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<ArticleEntity>> request, List<ArticleEntity> articleEntities) {
                addPage(articleEntities);
            }
        });

    }


}
