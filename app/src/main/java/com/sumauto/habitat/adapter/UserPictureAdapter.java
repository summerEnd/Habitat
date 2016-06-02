package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.ImageViewHolder;
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

/**
 * Created by Lincoln on 16/3/28.
 * 用户照片
 */
public class UserPictureAdapter extends LoadMoreAdapter {
    String uid;
    public UserPictureAdapter(Context context,String uid) {
        super(context, new ArrayList<JSONObject>());
        this.uid=uid;
    }

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        holder.setData(getDataList().get(position));
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<List<JSONObject>> request = Requests.getImgList(uid, page, 12);
        SyncHttpHandler<List<JSONObject>> httpHandler = new SyncHttpHandler<>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);
        return httpHandler.getResult();
    }

}
