package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.adapter.holders.AboutMeLikeHolder;
import com.sumauto.habitat.adapter.holders.AboutMeListHolder;
import com.sumauto.habitat.bean.AboutBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lincoln on 16/3/25.
 * 关于我
 */
public class AboutMeAdapter extends LoadMoreAdapter {

    ArrayList<Boolean> data = new ArrayList<>();

    public AboutMeAdapter(Context context) {
        super(context, new ArrayList());
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {

        //0->关注，1->赞，2->评论，3->提醒我看
        if (viewType == 0) {
            return new AboutMeListHolder(parent);
        } else {
            return new AboutMeLikeHolder(parent);
        }
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {
        try {
            String uid = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
            HttpRequest<List<AboutBean>> request = Requests.getUserMessage(page + 1, uid);
            SyncHttpHandler<List<AboutBean>> httpHandler = new SyncHttpHandler<>(request);
            HttpManager.getInstance().postSync(getContext(), httpHandler);
            return httpHandler.getResult();
        } catch (NotLoginException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getViewType(int position) {
        AboutBean bean = (AboutBean) getDataList().get(position);
        return bean.getType();
    }
}
