package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;

import java.util.List;

public class MainTrendAdapter extends TrendAdapter {

    public MainTrendAdapter(BaseActivity context) {
        super(context,"");
    }

    @Override
    public List onLoadData(int page) {
        try {
            String cid = HabitatApp.getInstance().getUserData(HabitatApp.ACCOUNT_COMMID);
            HttpRequest<List<FeedBean>> request = Requests.getCommunity(cid, page, 5);

            SyncHttpHandler<List<FeedBean>> httpHandler = new SyncHttpHandler<List<FeedBean>>(request);
            HttpManager.getInstance().postSync(getContext(), httpHandler);
            return httpHandler.getResult();
        } catch (NotLoginException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
