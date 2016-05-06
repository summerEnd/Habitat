package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AvatarNickHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 点赞列表
 */
public class LikeGridAdapter extends LoadMoreAdapter{

    private final String tid;

    public LikeGridAdapter(Context context, String tid) {
        super(context, new ArrayList<UserInfoBean>());
        this.tid = tid;
    }


    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new AvatarNickHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {

        HttpRequest<List<UserInfoBean>> request = Requests.getSubjectNice(tid, page);
        SyncHttpHandler<List<UserInfoBean>> httpHandler = new SyncHttpHandler<>(request);

        HttpManager.getInstance().postSync(getContext(), httpHandler);

        return httpHandler.getResult();
    }


}
