package com.sumauto.habitat.adapter;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainTrendAdapter extends LoadMoreAdapter {

    private int type;

    public MainTrendAdapter(BaseActivity context, int type) {
        super(context, new ArrayList<FeedBean>());
        this.type = type;
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            getDataList().clear();

        }
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
        if (type == 1) {
            return getNearBy(page);
        } else {
            return getCommunity(page);
        }
    }

    List getNearBy(int page) {
        Location position = HabitatApp.getInstance().getPosition();

        HttpRequest<List<FeedBean>> request = Requests.getNearBy(page, position.getLatitude(), position.getLongitude());

        SyncHttpHandler<List<FeedBean>> httpHandler = new SyncHttpHandler<List<FeedBean>>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);
        return httpHandler.getResult();
    }

    List getCommunity(int page) {
        try {
            String cid = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_COMMID);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(type==1){
            sb.append("nearby");
        }else{
            sb.append("community");
        }
        sb.append("{");
        sb.append(" isLoading = ").append(isLoading());
        sb.append("}");
        return sb.toString();
    }
}
