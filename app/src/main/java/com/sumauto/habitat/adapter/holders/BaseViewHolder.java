package com.sumauto.habitat.adapter.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lincoln on 16/3/22.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder{
    public BaseViewHolder(ViewGroup parent,int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId,parent,false));
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
}