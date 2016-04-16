package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.util.RandomUtils;
import com.sumauto.habitat.adapter.holders.MyAttentionHolder;
import com.sumauto.habitat.bean.AttentionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionAdapter extends RecyclerView.Adapter<MyAttentionHolder>{

    List<AttentionBean> beans=new ArrayList<>();

    public MyAttentionAdapter() {
        for (int i = 0; i < 20; i++) {
            AttentionBean object = new AttentionBean();
            int count= RandomUtils.randomInt(0,8);
            ArrayList<String> images = new ArrayList<>(8);
            for (int j = 0; j <count; j++) {
                images.add("");
            }
            object.setImages(images);
            beans.add(object);
        }
    }

    @Override
    public MyAttentionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAttentionHolder(parent);
    }

    @Override
    public void onBindViewHolder(MyAttentionHolder holder, int position) {
        holder.init(beans.get(position));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
