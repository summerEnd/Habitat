package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.BlackListHolder;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lincoln on 16/3/22.
 * 通讯录
 */
public class BlackNoteAdapter extends RecyclerView.Adapter implements HeaderDecor.Callback {

    private List<Object> beans = new ArrayList<>();

    public BlackNoteAdapter() {
        for (int i = 0; i < 50; i++) {

            UserInfoBean object = new UserInfoBean();
            beans.add(object);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new BlackListHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object data = beans.get(position);
        if (holder instanceof AddressBookTitleHolder) {
            ((AddressBookTitleHolder) holder).setData(data.toString());
        } else if (holder instanceof BlackListHolder) {
            BlackListHolder blackListHolder = (BlackListHolder) holder;
            blackListHolder.setData(data);
            blackListHolder.btn_remove.setTag(data);
            blackListHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = beans.indexOf(v.getTag());
                    if (index>=0){
                        beans.remove(index);
                        notifyItemRemoved(index);
                    }else{
                        ToastUtil.toast(v.getContext(),"index not found!");
                    }

                }
            });
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


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
