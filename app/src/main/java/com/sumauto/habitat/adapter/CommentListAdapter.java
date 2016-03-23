package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.CommentListHolder;
import com.sumauto.habitat.bean.CommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 评论列表
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListHolder>{

    List<CommentBean> beanList=new ArrayList<>();

    public CommentListAdapter() {
        for (int i = 0; i < 20; i++) {
            beanList.add(new CommentBean());
        }
    }

    @Override
    public CommentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentListHolder(parent);
    }

    @Override
    public void onBindViewHolder(CommentListHolder holder, int position) {
        holder.init(beanList.get(position));
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
}
