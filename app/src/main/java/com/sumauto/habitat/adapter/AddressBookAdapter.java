package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Lincoln on 16/3/22.
 * 通讯录
 */
public class AddressBookAdapter extends RecyclerView.Adapter {

   private List<Object> beans = new ArrayList<>();

    public AddressBookAdapter() {
        for (int i = 0; i < 250; i++) {
            int j = i % 10;
            String letter = String.valueOf((char) ('A' + i / 10));

            if (j == 0) {
                beans.add(letter);
            } else {
                UserInfoBean object = new UserInfoBean();
                object.setNameFirstLetter(letter);
                beans.add(object);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new UserListHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddressBookTitleHolder) {
            ((AddressBookTitleHolder) holder).init(beans.get(position).toString());
        } else if (holder instanceof UserListHolder) {
            ((UserListHolder) holder).init((UserInfoBean) beans.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return beans.get(position) instanceof String ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public List<Object> getBeans() {
        return beans;
    }

}
