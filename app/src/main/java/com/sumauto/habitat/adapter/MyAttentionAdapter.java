package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.util.RandomUtils;
import com.sumauto.habitat.adapter.holders.MyAttentionHolder;
import com.sumauto.habitat.bean.AttentionBean;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionAdapter extends LoadMoreAdapter {

    public MyAttentionAdapter(Context context) {
        super(context, new ArrayList());
    }

    //    public void MyAttentionAdapter() {
    //        for (int i = 0; i < 20; i++) {
    //            AttentionBean object = new AttentionBean();
    //            int count= RandomUtils.randomInt(0,8);
    //            ArrayList<String> images = new ArrayList<>(8);
    //            for (int j = 0; j <count; j++) {
    //                images.add("");
    //            }
    //            object.setImages(images);
    //            beans.add(object);
    //        }
    //    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new MyAttentionHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<List<AttentionBean>> request = Requests.getActiveInfo(page);
        SyncHttpHandler<List<AttentionBean>> httpHandler = new SyncHttpHandler<>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);

        return httpHandler.getResult();
    }

}
