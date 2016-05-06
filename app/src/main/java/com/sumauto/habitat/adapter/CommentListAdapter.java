package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.CommentListHolder;
import com.sumauto.habitat.bean.CommentBean;
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
 * 评论列表
 */
public class CommentListAdapter extends LoadMoreAdapter {

    private String tid;
    public CommentListAdapter(Context context,String tid) {
        super(context, new ArrayList<CommentBean>());
        this.tid=tid;
    }


    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new CommentListHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<List<CommentBean>> request = Requests.getComment(tid, page, 10);

        SyncHttpHandler<List<CommentBean>> localHandler =new SyncHttpHandler<List<CommentBean>>(request);
        HttpManager.getInstance().postSync(getContext(), localHandler);
        return localHandler.getResult();
    }


}
