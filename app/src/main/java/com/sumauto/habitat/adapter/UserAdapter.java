package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/22.
 * 用户列表
 */
public class UserAdapter extends RecyclerView.Adapter<UserListHolder>{

    private List<UserInfoBean> beans=new ArrayList<>();

    public UserAdapter() {
        for (int i = 0; i < 20; i++) {
            beans.add(new UserInfoBean());
        }
    }

    @Override
    public UserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListHolder(parent);
    }

    @Override
    public void onBindViewHolder(UserListHolder holder, int position) {
        holder.init(beans.get(position));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
