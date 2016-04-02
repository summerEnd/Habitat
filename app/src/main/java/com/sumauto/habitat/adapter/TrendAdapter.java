package com.sumauto.habitat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.sumauto.common.util.JsonUtil;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.ArticleEntity;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        setRefreshDone();
    }

    @Override
    public void onLoadMore() {
        RequestParams params = new RequestParams();
        params.put("commid", commid);
        params.put("pageid", getCurrentPage() + 1);
        params.put("pagesize", 5);
        HttpManager.getInstance().postApi(getContext(), HttpManager.getCommunity, params, new HttpHandler() {
            @Override
            public void onSuccess(HttpResponse response) throws JSONException {
                addPage(JsonUtil.getArray(new JSONObject(response.data).getJSONArray("articlelist"), ArticleEntity.class));
            }
        });
    }


}
