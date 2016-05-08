package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.ChooseUserHolder;
import com.sumauto.habitat.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lincoln on 16/3/22.
 * 选择用户
 */
public class ChooseUserAdapter extends RecyclerView.Adapter implements ChooseUserHolder.Listener,HeaderDecor.Callback{


    ArrayList<Object> beans=new ArrayList<>();
    Set<UserInfoBean> selectedUser=new HashSet<>();
    public ChooseUserAdapter(Map<String, List<?>> data) {
        Set<String> strings = data.keySet();
        for (String key :strings) {
            beans.add(key);
            beans.addAll(data.get(key));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new ChooseUserHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object bean = beans.get(position);
        if (holder instanceof AddressBookTitleHolder) {
            ((AddressBookTitleHolder) holder).setData(bean.toString());
        } else if (holder instanceof ChooseUserHolder) {
            ((ChooseUserHolder) holder).init((UserInfoBean) bean, this, selectedUser.contains(bean));
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

    public Set<UserInfoBean> getSelectedUser() {
        return selectedUser;
    }

    @Override
    public void onUserSelectChanged(UserInfoBean bean, boolean isSelect) {
        if (isSelect)selectedUser.add(bean);
        else selectedUser.remove(bean);
    }

    @Override
    public int getHeaderForPosition(int position) {
        int count = beans.size();
        if (position < count) {
            for (int i = position; i >= 0; i--) {
                if (getItemViewType(i) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isHeader(RecyclerView.ViewHolder holder) {
        return holder instanceof AddressBookTitleHolder;
    }
}
