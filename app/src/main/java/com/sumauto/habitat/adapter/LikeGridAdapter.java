package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AvatarNickHolder;
import com.sumauto.habitat.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 点赞列表
 */
public class LikeGridAdapter extends RecyclerView.Adapter<AvatarNickHolder>{
    List<UserInfoBean> beans=new ArrayList<>();

    public LikeGridAdapter() {
        for (int i = 0; i < 38; i++) {
            beans.add(new UserInfoBean());
        }
    }

    @Override
    public AvatarNickHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarNickHolder(parent);
    }

    @Override
    public void onBindViewHolder(AvatarNickHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
