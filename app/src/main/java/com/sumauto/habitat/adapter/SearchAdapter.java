package com.sumauto.habitat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.CircleHolder;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.CommitBean;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/06 5.6.6 
 */
public class SearchAdapter extends LoadMoreAdapter {

    private final String keyword;
    private final String type;

    public SearchAdapter(Context context, String keyword, String type) {
        super(context, new ArrayList());
        this.keyword = keyword;
        this.type = type;
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new CircleHolder(parent);
            case 2:
                return new UserListHolder(parent);
            case 1:
                return new TrendHolder(parent, null);
        }
        return new CircleHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public int getViewType(int position) {
        Object o = getDataList().get(position);
        int type;
        if (o instanceof CommitBean) {
            type = 0;
        } else if (o instanceof FeedBean) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<ArrayList<Object>> request = Requests.getSearch(keyword, type, page, 5);

        SyncHttpHandler<ArrayList<Object>> httpHandler = new SyncHttpHandler<>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);

        return httpHandler.getResult();
    }
}
