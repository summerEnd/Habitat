package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.MyAttentionHolder;
import com.sumauto.habitat.bean.DemoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionAdapter extends RecyclerView.Adapter<MyAttentionHolder>{

    List<DemoBean> beans=new ArrayList<>();

    public MyAttentionAdapter() {
        for (int i = 0; i < 20; i++) {
            beans.add(new DemoBean("",""));
        }
    }

    @Override
    public MyAttentionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAttentionHolder(parent);
    }

    @Override
    public void onBindViewHolder(MyAttentionHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
